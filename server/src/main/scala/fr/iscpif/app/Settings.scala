package fr.iscpif.app

import better.files._

object Settings {

  val defaultViaducDirectory = File(System.getProperty("user.home")) / s".viaduc/${Utils.fixHostName}/"

  val tmpDirectory = defaultViaducDirectory / "tmp"
}
