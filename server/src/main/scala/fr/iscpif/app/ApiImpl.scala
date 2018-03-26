package fr.iscpif.app

import better.files._

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
        parc.h = parameters.h
        parc.mp = parameters.mp
        parc.mt = parameters.mt

        val rng = new util.Random(42)
        val vk = KernelComputation(
          dynamic = parc.dynamic,
          depth = 12,
          zone = Vector((parameters.Cmin, parameters.Cmax), (parameters.Amin, parameters.Amax), (parameters.Tmin, parameters.Tmax)),
          // controls = Vector((0.02 to 0.4 by 0.02 ))
          controls = (x: Vector[Double]) =>
            for {
              c1 <- (0.03 to 0.031 by 0.001)
              c2 <- (0.0 to 10.0 by 10.0)
            } yield Control(c1, c2)

        )

        val (ak, steps) = approximate(vk, rng)

        //saveVTK3D(ak, Settings.tmpDirectory / s"resparc3_2DDepth${vk.depth}2controls_TRYWeb2.vtk")
        val kernelFile = Utils.file(parameters)
        saveHyperRectangles(vk)(ak, kernelFile)

        println(steps)

        KernelResult(kernelFile.toJava.getAbsolutePath, kernelFile.isEmpty)
    }
  }

}