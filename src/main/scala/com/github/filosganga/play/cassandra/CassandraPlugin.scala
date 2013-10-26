/*
 * Copyright 2013 Filippo De Luca - http://filippodeluca.com
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
