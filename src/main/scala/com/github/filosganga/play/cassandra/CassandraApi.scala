package com.github.filosganga.play.cassandra

import play.api.{Logger, Configuration}
import com.datastax.driver.core.{LatencyTracker, Session, Cluster}
import com.google.common.cache.{RemovalNotification, RemovalListener, CacheBuilder, Cache}
import java.util.concurrent.{Callable, TimeUnit}
import com.datastax.driver.core.Host.StateListener

/**
 * The Cassandra API layer.
 *
 * @param cfg The play configuration containing cassandra settings (usually cassandra.*),
 * @param clusterFactory The ClusterFactory needed to build the Cassandra Cluster.
 */
class CassandraApi(cfg: Configuration, clusterFactory: ClusterFactory) {

  private val names = cfg.subKeys

  protected[cassandra] val clusters: Map[String, Cluster] = names.map{dbName=>
    dbName->clusterFactory.initCluster(dbName, cfg)
  }.toMap

  protected[cassandra] val sessions: Cache[String, Session] = CacheBuilder
    .newBuilder()
    .expireAfterAccess(5, TimeUnit.MINUTES)
    .removalListener(removalListener)
    .build()

  private def removalListener: RemovalListener[String, Session] = {
    new RemovalListener[String, Session] {
      def onRemoval(notification: RemovalNotification[String, Session]) {
        Logger.info("Shutting down Cassandra Session...")
        notification.getValue.shutdown()
      }
    }
  }

  private def withCluster[A](dbName: String)(block: Cluster => A): A = clusters.get(dbName).map{c=>
    block(c)
  }.getOrElse {
    throw new IllegalArgumentException("Cassandra database[" + dbName + "] not available")
  }

  def withSession[A](dbName: String)(block: Session => A): A = withCluster(dbName){c=>

    val session = sessions.get(dbName, new Callable[Session]{
      def call() = {
        c.connect()
      }
    })

    block(session)
  }

  def withSession[A](dbName: String, keyspace: String)(block: Session => A): A = withCluster(dbName){c=>

    val session = sessions.get(dbName + ":" + keyspace, new Callable[Session]{
      def call() = {
        c.connect()
      }
    })

    block(session)
  }

  def register[T: Registrable](clusterName: String = "default")(toRegister: T) {
    withCluster(clusterName){c=> toRegister match {
      case x: LatencyTracker => c.register(x)
      case x: StateListener => c.register(x)
    }}
  }

  def unregister[T: Registrable](clusterName: String = "default")(toUnregister: T) {
    withCluster(clusterName){c=> toUnregister match {
      case x: LatencyTracker => c.unregister(x)
      case x: StateListener => c.unregister(x)
    }}
  }

}

sealed class Registrable[T]

object Registrable {
  implicit object LatencyTrackerRegistrable extends Registrable[LatencyTracker]
  implicit object StateListenerRegistrable extends Registrable[StateListener]
}

