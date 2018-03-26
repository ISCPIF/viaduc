package shared

object Data {

  case class KernelParameters(Cmax: Double,
                              Cmin: Double,
                              Amax: Double,
                              Amin: Double,
                              Tmax: Double,
                              Tmin: Double,
                              l: Double,
                              g: Double,
                              M: Int,
                              c: Double,
                              p: Double,
                              a: Double,
                              e: Double,
                              eta: Double,
                              phi: Double,
                              phi2: Double,
                              d: Double,
                              del: Double,
                              h: Double,
                              mp: Double,
                              mt: Double
                             )

  case class KernelResult(resultPath: String, isResultEmpty: Boolean)

}