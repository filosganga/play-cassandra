package com.github.filosganga.play.cassandra;

import play.api.{Configuration, Logger, Plugin, Application}
import com.datastax.driver.core.{LatencyTracker, Session, Cluster}

import collection.JavaConversions._
import com.google.common.cache._
import java.util.concurrent.{Callable, TimeUnit}
import com.datastax.driver.core.Host.StateListener

/**
 *
 * @author Filippo De Luca - me@filippodeluca.com
 */
class CassandraPlugin(app: Application) extends Plugin {

  private lazy val cfg = app.configuration.getConfig("cassandra").getOrElse(Configuration.empty)

  // should be accessed in onStart first
  private lazy val cassandraApi: CassandraApi = new CassandraApi(cfg, clusterFactory)

  protected def clusterFactory: ClusterFactory = new DefaultClusterFactory

  def api: CassandraApi = cassandraApi

  override def onStart() {

    // To init
    val noOfUpHosts = cassandraApi.clusters.values.foldLeft(0){(s, c)=>
      s + c.getMetadata.getAllHosts.count(_.isUp)
    }

    Logger.info("Cassandra plugin started with the following clusters: " + cassandraApi.clusters.map{case(name, cluster)=>
      val metadata = cluster.getMetadata
      "{name: " + name + ", clusterName: " + metadata.getClusterName + ", hosts: " + metadata.getAllHosts.map{host=>
        "{address: " + host.getAddress + ", dataCenter: " + host.getDatacenter + ", rack: " + host.getRack + ", isUp: " + host.isUp + "}"
      }.mkString("[",",","]")+ "}"
    })

  }

  override def onStop() {
    cassandraApi.sessions.invalidateAll()
    cassandraApi.clusters.foreach{ case(name, cluster) =>
      Logger.info("Shutting down cassandra client: " + name + " ...")
      cluster.shutdown()
    }

    Logger.info("Cassandra plugin stopped.")
  }

}
