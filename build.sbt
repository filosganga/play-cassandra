name := "play-cassandra"

organization := "com.github.filosganga"

version := "1.0-SNAPSHOT"

licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

scalaVersion := "2.10.2"

crossScalaVersions := Seq("2.10.2")

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += "Typesafe snapshots" at "http://repo.typesafe.com/typesafe/snapshots/"

libraryDependencies ++= {
   val playVersion = "2.2.0"
   val slf4jVersion = "1.7.5"
   Seq(
     "com.typesafe.play" %% "play" % playVersion % "provided",
     "com.datastax.cassandra" % "cassandra-driver-core" % "1.0.4"
        exclude("org.slf4j", "slf4j-log4j12")
        exclude("log4j", "log4j")
        exclude("commons-logging", "commons-logging"),
     "org.slf4j" % "slf4j-api" % slf4jVersion,
     "org.slf4j" % "jcl-over-slf4j" % slf4jVersion,
     "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
     "com.twitter" %% "util-collection" % "6.6.0",
     "com.google.guava" % "guava" % "15.0",
     "com.google.code.findbugs" % "jsr305" % "2.0.2",
     "org.specs2" %% "specs2" % "1.14" % "test",
     "org.mockito" % "mockito-all" % "1.9.5" % "test",
     "com.typesafe.play" %% "play-test" % playVersion % "test",
     "ch.qos.logback" % "logback-classic" % "1.0.13" % "test"
   )
}

