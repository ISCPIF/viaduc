package shared



trait Api {
  def CalcKernel(parameters: Data.KernelParameters): Data.KernelResult
}