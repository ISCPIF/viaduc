package fr.iscpif.client

import simpark.shared.Data._

sealed trait KernelStatus {
  def message: String

  def kernelResult: Option[KernelResult]
}

object KernelStatus {
  def apply(m: String, kR: Option[KernelResult] = None) = new KernelStatus {

    def message = m

    def kernelResult = kR
  }

  val NOT_COMPUTED_YED = KernelStatus("Kernel not computed yet")
  val COMPUTING_KERNEL = KernelStatus("Computing kernel, please wait")

  def computedKernel(result: KernelResult) = KernelStatus("Kernel computed", Some(result))
}