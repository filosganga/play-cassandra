package com.github.filosganga.play.cassandra

import play.api.Configuration
import com.datastax.driver.core.Cluster

import collection.JavaConversions._

/**
 *
 * @author Filippo De Luca - me@filippodeluca.com
 */
class DefaultClusterFactory extends ClusterFactory {


  def initCluster(dbName: String, cfg: Configuration): Cluster = {

    def error(db: String, message: String = "") = throw cfg.reportError(db, message)

    val builder = Cluster.builder()

    val nodes = cfg.getStringList(dbName + ".nodes").getOrElse(error(dbName, "Missing configuration [cassandra." + dbName + ".nodes]"))

    nodes.foreach(builder.addContactPoint)

    builder.build()
  }
}
