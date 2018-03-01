package shared



trait Api {
  def uuid(): String

  def CalcKernel(Cmax: Double, Cmin: Double, Amax: Double, Amin: Double, Tmax: Double, Tmin: Double): Int
}