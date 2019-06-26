package simpark.app

import simpark.model.Parc3D
import viabilitree.export.{saveHyperRectangles, saveVTK3D}
import viabilitree.viability._
import viabilitree.viability.kernel._

object Parc2DViabilityKernel extends App {

  val parc = Parc3D()
  val rng = new util.Random(42)

  // definition des variables min et mas pour chaque valeure C,A,T et controles:
  val CMAX = 10000.0
  val CMIN = 1000.0
  val CSTEP = 1000.0 // de 1000 a 10000 pas par de 1000

  val AMAX = 5000.0
  val AMIN = 100.0
  val ASTEP = 100.0

  val TMAX = 10000.0
  val TMIN = 0.0
  val TSTEP = 500.0

  var c_max = CMAX
  var c_min = CMIN

  var a_max = AMAX
  var a_min = AMIN

  var t_max = TMAX
  var t_min = TMIN

// definition des controles eps () et zeta :
  val EPSMIN = 0.0
  val EPSMAX = 100.0
  val EPSSTEP = 10.0

  val ZETAMIN = 0.0
  val ZETAMAX = 0.1
  val ZETASTEP = 0.01

  var eps_min = EPSMIN
  var eps_max = EPSMAX

  var zeta_min = ZETAMIN
  var zeta_max = ZETAMAX


  while (c_max > CMIN) {

    while(c_min < c_max) {
      println("c "+c_min+" "+c_max)

      while (a_max > AMIN) {

        while (a_min < a_max) {
          println("a "+a_min+" "+a_max)

          while (t_max > TMIN) {

            while (t_min < t_max) {
              println("t "+t_min+" "+t_max)

              while (zeta_max > ZETAMIN) {

                while (zeta_min < zeta_max) {
                  println("zeta "+zeta_min+" "+zeta_max)

                  while (eps_max > EPSMIN) {

                    while (eps_min < eps_max) {
                      println("eps "+eps_min+" "+eps_max)

                      val vk = KernelComputation(
                        dynamic = parc.dynamic,
                        depth = 12,
                        zone = Vector((c_min, c_max), (a_min, a_max), (t_min, t_max)),
                        // controls = Vector((0.02 to 0.4 by 0.02 ))
                        controls = (x: Vector[Double]) =>
                          for {
                            c1 <- (zeta_min to zeta_max by ZETASTEP)
                            c2 <- (eps_min to eps_max by EPSSTEP)
                          } yield Control(c1, c2)


                      )

                      val (ak, steps) = approximate(vk, rng)

                      println(steps)

                      saveVTK3D(ak, Settings.defaultViaducDirectory / s"resparc_3D_D${vk.depth}_V_${c_min}_${c_max}_${a_min}_${a_max}_${t_min}_${t_max}_C_${eps_min}_${eps_max}_${zeta_min}_${zeta_max}.vtk")
                      saveHyperRectangles(vk)(ak, Settings.defaultViaducDirectory / s"resparc3D_D${vk.depth}_V_${c_min}_${c_max}_${a_min}_${a_max}_${t_min}_${t_max}_C_${eps_min}_${eps_max}_${zeta_min}_${zeta_max}.txt")

                      eps_min = eps_min + EPSSTEP
                    }
                    eps_min = EPSMIN
                    eps_max = eps_max - EPSSTEP
                  }
                  eps_max = EPSMAX
                  zeta_min = zeta_min + ZETASTEP
                }
                zeta_min = ZETAMIN
                zeta_max = zeta_max - ZETASTEP
              }
              zeta_max= ZETAMAX
              t_min = t_min + TSTEP
            }
            t_min= TMIN
            t_max = t_max - TSTEP
          }
          t_max= TMAX
          a_min = a_min + ASTEP
        }
        a_min = AMIN
        a_max = a_max - ASTEP
      }
      a_max = AMAX
      c_min = c_min + CSTEP
    }
    c_min= CMIN
    c_max = c_max - CSTEP
  }

/*
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
*/
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
