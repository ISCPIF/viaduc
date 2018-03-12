package fr.iscpif.app

import java.net.InetAddress
import scala.util.Try
object Utils {

  def fixHostName =
    if (System.getProperty("os.name").toLowerCase.contains("mac")) "localhost"
    else Try { InetAddress.getLocalHost().getHostName() }.getOrElse("localhost")

}
