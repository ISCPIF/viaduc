package fr.iscpif.app

import viabilitree.export.{saveHyperRectangles, saveVTK3D}
import viabilitree.viability._
import viabilitree.viability.kernel._

object Parc3DViabilityKernelCalculator extends App {

  val parc = Parc3D()
  val rng = new util.Random(42)

  val vk = KernelComputation(
    dynamic = parc.dynamic,
    depth = 12,
    zone = Vector((0.0, 100000.0),(0.0, 5000.0), (2500.0, 4000.0)),
    // controls = Vector((0.02 to 0.4 by 0.02 ))
    controls = (x: Vector[Double]) =>
      for {
        c1 <- (0.03 to 0.031 by 0.001 )
        c2 <- (0.0 to 10.0 by 10.0)
      } yield Control(c1, c2)



  )

  val (ak, steps) = approximate(vk, rng)

  println(steps)

  /*

  val b = ApiImpl.VideOrnot("/Users/laetitiazaleski/Desktop/results/resparc2DBWithControlD14_TRY1_BeaucoupAnim.txt")

  if(b){
    println("vide")
  }else{
    println("non vide")
  }
*/
  saveVTK3D(ak, Settings.tmpDirectory / s"resparc3_2DDepth${vk.depth}2controls_TRYWeb2.vtk")
  saveHyperRectangles(vk)(ak, Settings.tmpDirectory / s"resparc3DBWithControlD${vk.depth}_TRYWeb.txt")

}
