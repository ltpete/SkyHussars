package com.codebetyars.skyhussars.utils

import org.slf4j.{Logger, LoggerFactory}

trait Logging {

  lazy val logger: Logger = LoggerFactory.getLogger(getClass)

  def error(message: => String) { logger.error(message) }
  def error(message: => String, throwable: Throwable) { logger.error(message, throwable) }

  def warn(message: => String) { logger.warn(message) }
  def warn(message: => String, throwable: Throwable) { logger.warn(message, throwable) }

  def info(message: => String) { logger.info(message) }
  def info(message: => String, throwable: Throwable) { logger.info(message, throwable) }

  def debug(message: => String) { logger.debug(message) }
  def debug(message: => String, throwable: Throwable) { logger.debug(message, throwable)}

}
