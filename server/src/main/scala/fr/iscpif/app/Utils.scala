package fr.iscpif.app

import java.net.InetAddress
import scala.util.Try
object Utils {

  def fixHostName =
    if (System.getProperty("os.name").toLowerCase.contains("mac")) "localhost"
    else Try { InetAddress.getLocalHost().getHostName() }.getOrElse("localhost")

  def fileName(Cmax: Double, Cmin: Double, Amax: Double, Amin: Double, Tmax: Double, Tmin: Double, l:Double, g:Double,
               M:Int, c:Double, p:Double, a:Double, e:Double, eta: Double, phi:Double, phi2:Double, d:Double,
               del:Double, h:Double, mp:Double, mt:Double): String =
    s"${Cmax}_${Cmin}_${Amax}_${Amin}_${Tmax}_${Tmin}_${l}_${g}_${M}_${c}_${p}_${a}_${e}_${eta}_${phi}_${phi2}_${d}_${del}_${h}_${mp}_${mt}"
}
