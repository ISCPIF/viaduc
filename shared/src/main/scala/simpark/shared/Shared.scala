package simpark.shared

trait Api {
  def CalcKernel(parameters: Data.KernelParameters, boolEps:  Boolean, boolZeta: Boolean): Data.KernelResult
  def InterKernels(parameters1: Data.KernelParameters,parameters2: Data.KernelParameters): Data.KernelResult
  def IntersectionKernels(f1 : String, f2 : String): Unit
}