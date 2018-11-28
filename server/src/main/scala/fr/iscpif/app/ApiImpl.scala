package fr.iscpif.app

import better.files._
import fr.iscpif.app.Parc2DViabilityKernel._
import scalaz.Alpha.M
import viabilitree.export._
import viabilitree.viability._
import viabilitree.viability.kernel._
import viabilitree.viability.kernel.KernelComputation
import shared.Data._
import scala.io.Source


object ApiImpl extends shared.Api {

  // paramètres : max et min sur CAT, parametres de la dynamique, controles
  def CalcKernel(parameters: KernelParameters): KernelResult = {

    val resFile = Utils.file(parameters)

    resFile.exists match {
      case true => KernelResult(resFile.toJava.getAbsolutePath, resFile.isEmpty)
      case false =>
        val parc = Parc3D()
        parc.l = parameters.l
        parc.g = parameters.g
        parc.M = parameters.M
        parc.c = parameters.c
        parc.p = parameters.p
        parc.a = parameters.a
        parc.e = parameters.e
        parc.eta = parameters.eta
        parc.phi = parameters.phi
        parc.phi2 = parameters.phi2
        parc.d = parameters.d
        parc.del = parameters.del
        parc.h = 0.0
        parc.mp = parameters.mp
        parc.mt = parameters.mt

        val rng = new util.Random(42)

        var zmax = parameters.zetaMax * 0.01
        var zmin = parameters.zetaMin * 0.01

        if(parameters.zetaMin > parameters.zetaMax){
           zmin = parameters.zetaMax * 0.01
           zmax = parameters.zetaMin * 0.01
        }
        var emax = parameters.epsMax
        var emin = parameters.epsMin

        if(parameters.epsMin > parameters.epsMax) {
          emax = parameters.epsMin
          emin = parameters.epsMax
        }

        println(s"emin : $emin, emax: $emax, zmin: $zmin, zmax : $zmax")

        val vk = KernelComputation(
          dynamic = parc.dynamic,
          depth = 12,
          zone = Vector((parameters.Cmin, parameters.Cmax), (parameters.Amin, parameters.Amax), (parameters.Tmin, parameters.Tmax)),
          // controls = Vector((0.02 to 0.4 by 0.02 ))
          controls = (x: Vector[Double]) =>
            for {
              c1 <- (zmin to zmax by 0.001)
              c2 <- (emin to emax by 10.0)
            } yield Control(c1, c2)

        )

        val (ak, steps) = approximate(vk, rng)

        //saveVTK3D(ak, Settings.tmpDirectory / s"resparc3_2DDepth${vk.depth}2controls_TRYWeb2.vtk")
        val kernelFile = Utils.file(parameters)
        saveHyperRectangles(vk)(ak, kernelFile)
        saveVTK3D(ak, Settings.defaultViaducDirectory / s"resparc_3D_D${vk.depth}_V_${c_min}_${c_max}_${a_min}_${a_max}_${t_min}_${t_max}_C_${eps_min}_${eps_max}_${zeta_min}_${zeta_max}.vtk")


        println(steps)

        KernelResult(kernelFile.toJava.getAbsolutePath, kernelFile.isEmpty)
    }
  }

}