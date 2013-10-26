package com.github.filosganga.play.cassandra

import com.datastax.driver.core.Cluster
import play.api.Configuration

/**
 *
 * @author Filippo De Luca - me@filippodeluca.com
 */
trait ClusterFactory {

  def initCluster(dbName: String, cfg: Configuration): Cluster

}
