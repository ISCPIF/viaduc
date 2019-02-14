package shared

object Data {

  case class KernelParameters(Cmax: Double,
                              Cmin: Double,
                              Amax: Double,
                              Amin: Double,
                              Tmax: Double,
                              Tmin: Double,
                              alpha: Double,
                              ip:Double,
                              it:Double,
                              l: Double,
                              g: Double,
                              M: Int,
                              p: Double,
                              a: Double,
                              eta: Double,
                              phi: Double,
                              phi2: Double,
                              del: Double,
                              mp: Double,
                              mt: Double,
                              e: Double,
                              epsMax: Double,
                              epsMin: Double,
                              zetaMax: Double,
                              zetaMin: Double,
                             )

  case class KernelResult(resultPath: String, isResultEmpty: Boolean)

}