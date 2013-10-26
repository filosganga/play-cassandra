Cassandra Plugin for Play! Framework
====================================
This plugin provide the [Apache Cassandra](http://cassandra.apache.org/) support for the [Play! Framework]
(http://www.playframework.com/) trough the [DataStax Java Driver](https://github.com/datastax/java-driver).

Add play-cassandra to your dependencies
---------------------------------------
    libraryDependencies ++= Seq(
      "com.github.filosganga" %% "play-cassandra" % "1.0"
    )
If you want to use the latest snapshot, add the following instead:

    resolvers += "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"

    libraryDependencies ++= Seq(
     "com.github.filosganga" %% "play-cassandra" % "1.0-SNAPSHOT"
    )

Add Cassandra Plugin to the Play! configuration
-----------------------------------------------
Add the following to the ``conf/play.plugins`` file:

    9000:com.github.filosganga.play.cassandra.CassandraPlugin

Configure the database access within Play! configuration
--------------------------------------------------------
Add the list of nodes to the ``conf/application.conf``

    cassandra.default.nodes: ["10.0.0.1", "10.0.0.2"]

Enjoy Cassandra
---------------
You can use cassandra trough the ``Cassandra`` object:

    val result = Cassandra.withSessionKeyspace("my-keyspace"){session=>
       session.execute("...some cql...")
    }

If you want to use a specific database, configured in ``application.conf``with the following:

    cassandra.foo.nodes: ["10.0.0.1", "10.0.0.2"]

You can use the following code:

    val result = Cassandra.withSessionKeyspace("foo")("my-keyspace"){session=>
       session.execute("...some cql...")
    }

Enjoy your code!



