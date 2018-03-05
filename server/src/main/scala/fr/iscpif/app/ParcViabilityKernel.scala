package fr.iscpif.app

import viabilitree.viability._
import viabilitree.viability.kernel._

object Parc2DViabilityKernel extends App {

  val parc = Parc2D_B()
  val rng = new util.Random(42)

  val vk = KernelComputation(
    dynamic = parc.dynamic,
    depth = 14,
    zone = Vector((0.0, 5000.0), (2500.0, 4000.0)),
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

  // saveHyperRectangles(vk)(ak,s"/tmp/resparcWithControlD${vk.depth}.txt")

  //println(volume(res))

  /*

    val pointA=Vector(100.0,3000.0)

    val u2=Vector(0.001)
    val pointB=parc.dynamic(pointA,u2)
    println(pointB)
  */


  // noyau vide avec    controls = Vector((0.00 to 0.1 by 0.01 ),(0.0 to 100.0 by 10.0)) et    zone = Vector((3000.0, 5000.0), (5000.0, 7500.0)),
}
