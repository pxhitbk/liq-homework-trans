package exception

/**
 * Generic exception.
 */
case class ApplicationException(code: String, message: String, cause: Throwable = null) extends Exception(message, cause)

