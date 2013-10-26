package com.github.filosganga.play.cassandra


import com.datastax.driver.core.Host.StateListener
import com.datastax.driver.core.{LatencyTracker, Session}

import play.api.Application

/**
 *
 * @author Filippo De Luca - me@filippodeluca.com
 */
object Cassandra {

  /** The exception we are throwing. */
  private def pluginNotRegisteredError() = throw new Exception("Cassandra plugin is not registered.")

  def withSession[A](cfgName: String = "default")(block: Session => A)(implicit app: Application): A = {
    withPlugin(app)(_.api.withSession(cfgName)(block))
  }

  def withSessionKeyspace[A](keyspace: String, cfgName: String = "default")(block: Session => A)(implicit app: Application): A = {
    withPlugin(app)(_.api.withSession(cfgName, keyspace)(block))
  }

  def register(toRegister: LatencyTracker)(implicit app: Application) {
    withPlugin(app)(_.api.register()(toRegister))
  }

  def register(dbName: String)(toRegister: LatencyTracker)(implicit app: Application) {
    withPlugin(app)(_.api.register(dbName)(toRegister))
  }

  def register(toRegister: StateListener)(implicit app: Application) {
    withPlugin(app)(_.api.register()(toRegister))
  }

  def register(dbName: String)(toRegister: StateListener)(implicit app: Application) {
    withPlugin(app)(_.api.register(dbName)(toRegister))
  }

  def unregister(dbName: String)(toUnregister: LatencyTracker)(implicit app: Application) {
    withPlugin(app)(_.api.unregister(dbName)(toUnregister))
  }

  def unregister(toUnregister: LatencyTracker)(implicit app: Application) {
    withPlugin(app)(_.api.unregister()(toUnregister))
  }

  def unregister(dbName: String)(toUnregister: StateListener)(implicit app: Application) {
    withPlugin(app)(_.api.unregister(dbName)(toUnregister))
  }

  def unregister(toUnregister: StateListener)(implicit app: Application) {
    withPlugin(app)(_.api.unregister()(toUnregister))
  }

  private def withPlugin[A](app: Application)(block: CassandraPlugin => A): A =
    app.plugin[CassandraPlugin].map {
      plugin =>
        block(plugin)
    }.getOrElse(pluginNotRegisteredError())

}
