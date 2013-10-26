package com.github.filosganga.play.cassandra

import org.specs2.mutable._
import org.specs2.specification.Scope
import play.api.Configuration
import com.typesafe.config.ConfigFactory

import collection.JavaConversions._
/**
 *
 * @author Filippo De Luca - me@filippodeluca.com
 */
class DefaultClusterFactorySpec extends Specification {

  "DefaultClusterFactory" should {
    "read nodes" in new Base {
      val cluster = toTest.initCluster("foo", Configuration(ConfigFactory.parseString("""foo.nodes=["localhost"]""")))
      val hosts = cluster.getMetadata.getAllHosts.toSeq

      hosts.size shouldEqual  1
      hosts(0).getAddress.getHostName shouldEqual "localhost"
    }
  }

  trait Base extends Scope {

    val toTest = new DefaultClusterFactory

  }

}
