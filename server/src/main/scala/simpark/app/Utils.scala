package simpark.app

import java.net.InetAddress
import scala.util.Try
import better.files._
import simpark.shared.Data._

object Utils {

  def fixHostName =
    if (System.getProperty("os.name").toLowerCase.contains("mac")) "localhost"
    else Try {
      InetAddress.getLocalHost().getHostName()
    }.getOrElse("localhost")


  def file(p: KernelParameters, emin: Double, emax: Double, zmin: Double, zmax: Double): File =
    Settings.defaultViaducDirectory /
      s"Cmin${p.Cmin}Cmax${p.Cmax}Amin${p.Amin}Amax${p.Amax}Tmin${p.Tmin}Tmax${p.Tmax}Emin${emin}Emax${emax}__Zmin${zmin}Zmax${zmax}.txt"
}