package shared



trait Api {
  def CalcKernel(parameters: Data.KernelParameters): Data.KernelResult

  def InterKernels(parameters1: Data.KernelParameters,parameters2: Data.KernelParameters): Data.KernelResult
}