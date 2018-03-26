package fr.iscpif.app

import java.net.InetAddress
import scala.util.Try
import better.files._
import shared.Data._

object Utils {

  def fixHostName =
    if (System.getProperty("os.name").toLowerCase.contains("mac")) "localhost"
    else Try { InetAddress.getLocalHost().getHostName() }.getOrElse("localhost")

  def file(p: KernelParameters): File =
    Settings.tmpDirectory /
      s"${p.Cmax}_${p.Cmin}_${p.Amax}_${p.Amin}_${p.Tmax}_${p.Tmin}_${p.l}_${p.g}_${p.M}_${p.c}_${p.p}_${p.a}_${p.e}_${p.eta}_${p.phi}_${p.phi2}_${p.d}_${p.del}_${p.h}_${p.mp}_${p.mt}.txt"
}
