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

  def file(p: KernelParameters): File =
    Settings.tmpDirectory /
      s"${p.Cmax}_${p.Cmin}_${p.Amax}_${p.Amin}_${p.Tmax}_${p.Tmin}_${p.l}_${p.g}_${p.M}_${p.p}_${p.a}_${p.eta}_${p.phi}_${p.phi2}_${p.del}_${p.mp}_${p.mt}_${p.epsMin}_${p.epsMax}_${p.zetaMin}_${p.zetaMax}.txt"

}