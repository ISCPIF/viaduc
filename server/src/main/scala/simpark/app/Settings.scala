package simpark.app

import better.files._

object Settings {

 // val defaultViaducDirectory = File(System.getProperty("user.home")) / s".viaduc/${Utils.fixHostName}/"

  val defaultViaducDirectory = File(System.getProperty("user.home")) / s"/Desktop/Kernelresults/"

  val tmpDirectory = defaultViaducDirectory / "tmp"
}
