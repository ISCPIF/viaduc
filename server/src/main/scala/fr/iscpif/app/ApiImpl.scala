package fr.iscpif.app

import viabilitree.export._
import viabilitree.viability._
import viabilitree.viability.kernel._

object ApiImpl extends shared.Api {

  def uuid(): String = {
    println("UUUUUID")
    java.util.UUID.randomUUID.toString
  }

  def CalcKernel(Cmax: Double, Cmin: Double, Amax: Double, Amin: Double, Tmax: Double, Tmin: Double): Int = {

    val parc = Parc2D_B()
    val rng = new util.Random(42)
    val vk = KernelComputation(
      dynamic = parc.dynamic,
      depth = 10,
      zone = Vector((Amin, Amax), (Tmin, Tmax)),
      // controls = Vector((0.02 to 0.4 by 0.02 ))
      controls = (x: Vector[Double]) =>
        for {
          c1 <- (0.03 to 0.031 by 0.001)
          c2 <- (0.0 to 10.0 by 10.0)
        } yield Control(c1, c2)

    )

    val (ak, steps) = approximate(vk, rng)

    saveVTK2D(ak, s"/Users/laetitiazaleski/Desktop/results/resparc2_2DDepth${vk.depth}2controls_TRYWeb2.vtk")
    saveHyperRectangles(vk)(ak, s"/Users/laetitiazaleski/Desktop/results/resparc2DBWithControlD${vk.depth}_TRYWeb.txt")

    println(steps)

    steps

  }
}