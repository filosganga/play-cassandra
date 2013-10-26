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
