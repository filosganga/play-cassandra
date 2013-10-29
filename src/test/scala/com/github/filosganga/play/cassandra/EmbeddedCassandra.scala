package com.github.filosganga.play.cassandra

import java.util.concurrent.{Callable, Executors}

import com.google.common.io.Files
import com.google.common.io.Resources
import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.apache.cassandra.service.CassandraDaemon
import java.io.File
import com.github.filosganga.play.cassandra.EmbeddedCassandra._
import org.slf4j.{LoggerFactory, Logger}
import java.nio.charset.Charset

/**
 *
 * @author Filippo De Luca - me@filippodeluca.com
 */
class EmbeddedCassandra(dataDir: File = createTempDir(), clusterName: String = "TestCluster", port: Int = 9160, nativePort: Int =  9042, storagePort: Int = 7000) {

  val logger: Logger = LoggerFactory.getLogger(getClass)

  val executorService = Executors.newSingleThreadExecutor(
    new ThreadFactoryBuilder()
      .setDaemon(true)
      .setNameFormat("EmbeddedCassandra-%d")
      .build()
  )

  val cassandra = initCassandra()

  def start() {
    executorService.submit(new Callable[Unit] {
      def call() {
        cassandra.start()
      }
    })

  }

  def stop() {
    executorService.shutdownNow()
    cassandra.deactivate()

  }

  private def initCassandra() = {
    logger.info("Starting cassandra daemon on port=" + port + ", storagePort=" + storagePort +", clusterName=" + clusterName + ", dataDir=" + dataDir + " ...")

    dataDir.mkdirs()

    val templateURL = Resources.getResource("cassandra-template.yml")
    val baseFile = Resources.toString(templateURL, Charset.defaultCharset())

    val newFile = baseFile
      .replace("$DIR$", dataDir.getPath)
      .replace("$PORT$", Integer.toString(port))
      .replace("$NATIVE_PORT$", Integer.toString(nativePort))
      .replace("$STORAGE_PORT$", Integer.toString(storagePort))
      .replace("$CLUSTER$", clusterName)

    val configFile = new File(dataDir, "cassandra.yaml")

    Files.write(newFile, configFile, Charset.defaultCharset())

    logger.info("Cassandra config file: " + configFile.getPath)
    System.setProperty("cassandra.config", "file:" + configFile.getPath)

    val cassandra = new CassandraDaemon
    cassandra.init(Array.empty[String])

    logger.info("Cassandra started.")

    cassandra
  }


}

object EmbeddedCassandra {

  def apply() = new EmbeddedCassandra()

  private def createTempDir(): File = {
    val tmpDir = Files.createTempDir()
    tmpDir.deleteOnExit()
    tmpDir
  }

}
