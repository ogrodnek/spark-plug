package com.bizo.hive.sparkplug.auth
import com.amazonaws.auth.BasicAWSCredentials

class SystemPropertyOrEnvCredentials extends BasicAWSCredentials(Env("AWS_ACCESS_KEY_ID"), Env("AWS_SECRET_ACCESS_KEY"))

object Env {
  /** Returns the system property or environment variable with given name, or default if undefined. */
  def apply(name: String, default: String = null): String = {
    Option(System.getProperty(name, System.getenv(name))) getOrElse {
      Option(default) getOrElse { throw new MissingRequiredProperty(name) }
    }
  }

  class MissingRequiredProperty(name: String) extends RuntimeException("Missing Required Property: " + name)
}