name := "play-cassandra"

organization := "com.github.filosganga"

organizationHomepage := Some(url("http://filippodeluca.com"))

version := "1.0-SNAPSHOT"

homepage := Some(url("http://github.com/filosganga/play-cassandra"))

licenses := Seq("Apache 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html"))

scmInfo := Some(ScmInfo(
    url("http://github.com/filosganga/play-cassandra"),
    "scm:git:git@github.com:filosganga/play-cassandra.git",
    Some("scm:git:git@github.com:filosganga/play-cassandra.git")
))


startYear := Some(2013)

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
     "com.google.guava" % "guava" % "13.0.1",
     "com.google.code.findbugs" % "jsr305" % "2.0.2",
     "org.specs2" %% "specs2" % "1.14" % "test",
     "org.mockito" % "mockito-all" % "1.9.5" % "test",
     "com.typesafe.play" %% "play-test" % playVersion % "test",
     "ch.qos.logback" % "logback-classic" % "1.0.13" % "test"
   )
}

publishMavenStyle := true

publishArtifact in Test := false

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (version.value.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomIncludeRepository := { _ => false }

pomExtra := (
  <url>https://github.com/filosganga/play-cassandra</url>
  <licenses>
    <license>
      <name>Apache 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:filosganga/play-cassandra.git</url>
    <connection>scm:git:git@github.com:filosganga/play-cassandra.git</connection>
  </scm>
  <developers>
    <developer>
      <id>filosganga</id>
      <name>Filippo De Luca</name>
      <url>http://filippodeluca.com</url>
    </developer>
  </developers>)



