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

  def uuid(): String = {
    println("UUUUUID")
    java.util.UUID.randomUUID.toString
  }
  // paramètres : max et min sur CAT, parametres de la dynamique, controles
  def CalcKernel(Cmax: Double, Cmin: Double, Amax: Double, Amin: Double, Tmax: Double, Tmin: Double, l:Double, g:Double,
                 M:Int, c:Double, p:Double, a:Double, e:Double, eta: Double, phi:Double, phi2:Double, d:Double,
                 del:Double, h:Double, mp:Double, mt:Double): KernelResult = {

    val parc = Parc3D()
    parc.l = l
    parc.g = g
    parc.M = M
    parc.c = c
    parc.p = p
    parc.a = a
    parc.e = e
    parc.eta = eta
    parc.phi = phi
    parc.phi2 = phi2
    parc.d = d
    parc.del = del
    parc.h = h
    parc.mp = mp
    parc.mt = mt

    val rng = new util.Random(42)
    val vk = KernelComputation(
      dynamic = parc.dynamic,
      depth = 12,
      zone = Vector((Cmin, Cmax),(Amin, Amax), (Tmin, Tmax)),
      // controls = Vector((0.02 to 0.4 by 0.02 ))
      controls = (x: Vector[Double]) =>
        for {
          c1 <- (0.03 to 0.031 by 0.001)
          c2 <- (0.0 to 10.0 by 10.0)
        } yield Control(c1, c2)

    )

    val (ak, steps) = approximate(vk, rng)

    saveVTK3D(ak, Settings.tmpDirectory / s"resparc3_2DDepth${vk.depth}2controls_TRYWeb2.vtk")
    saveHyperRectangles(vk)(ak, Settings.tmpDirectory / s"resparc3DBWithControlD${vk.depth}_TRYWeb.txt")

    println(steps)

    KernelResult(steps, File("results/resparc2DBWithControlD10_TRYWeb.txt").isEmpty)

  }

  def VideOrnot(fileName: String): Boolean = {

    val file = Source.fromFile(fileName)

    val fileContents = file.getLines.mkString

    file.close()

    fileContents.isEmpty()

  }
  
}