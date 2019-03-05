package simpark.app

import simpark.app.Parc2DViabilityKernel._
import simpark.model.Parc3D
import simpark.shared
import simpark.shared.Data._
import viabilitree.approximation.{learnIntersection, volume}
import viabilitree.export._
import viabilitree.kdtree._
import viabilitree.viability._
import viabilitree.viability.kernel.{KernelComputation, _}

import scala.math._

object ApiImpl extends shared.Api {

  // paramètres : max et min sur CAT, parametres de la dynamique, controles
  def CalcKernel(parameters: KernelParameters): KernelResult = {

/*    val resFile = Utils.file(parameters)
    resFile.exists match {
      case true => KernelResult(resFile.toJava.getAbsolutePath, resFile.isEmpty)
      case false => */
        val parc = Parc3D(
          alpha = parameters.alpha,
          ip = parameters.ip,
          it = parameters.it,
          l = parameters.l,
          g = parameters.g,
          M = parameters.M,
          p = parameters.p,
          a = parameters.a,
          eta = parameters.eta,
          phi = parameters.phi,
          phi2 = parameters.phi2,
          del = parameters.del,
          mp = parameters.mp,
          mt = parameters.mt,
          e = parameters.e)

        val rng = new util.Random(42)


    var zmax = max(parameters.zetaMax,parameters.zetaMin)
    var zmin = min(parameters.zetaMax,parameters.zetaMin)
    var emax = max(parameters.epsMax,parameters.epsMin)
    var emin = min(parameters.epsMax,parameters.epsMin)
    var Amax = max(parameters.Amax, parameters.Amin)
    var Amin = min(parameters.Amax, parameters.Amin)
    var Cmax = max(parameters.Cmax, parameters.Cmin)
    var Cmin = min(parameters.Cmax, parameters.Cmin)
    var Tmax = max(parameters.Tmax, parameters.Tmin)
    var Tmin = min(parameters.Tmax, parameters.Tmin)


    println(s"alpha : ${parc.alpha} emin : $emin, emax: $emax, zmin: $zmin, zmax : $zmax, C : ${Cmin}, ${Cmax}," +
      s" A: ${Amin},  A: ${Amax}, T: ${Tmin},  T: ${Tmax}")

    val vk = KernelComputation(
      dynamic = parc.dynamic,
      depth = 18,
      zone = Vector((Cmin, Cmax), (Amin, Amax ), (Tmin, Tmax)),
          controls = (x: Vector[Double]) =>
            for {
              c1 <- (zmin to zmax by 0.001)
              c2 <- (emin to emax by 10.0)
            } yield Control(c1, c2)
        )

        val (ak, steps) = approximate(vk, rng)
        val fileName = s"KernelBin_Cmin${parameters.Cmin}Cmax${parameters.Cmax}Amin${parameters.Amin}Amax${parameters.Amax}" +
          s"Tmin${parameters.Tmin}Tmax${parameters.Tmax}Emin${emin}Emax${emax}__depth${vk.depth}" +
          s"Zmin${zmin}Zmax${zmax}"
       // println(Settings.defaultViaducDirectory)
        println("done approximating")
        save(ak, s"/Users/laetitiazaleski/Desktop/Kernelresults/ParcBinary_${fileName}.bin")
        println("done saving bin")
        //saveVTK3D(ak, Settings.tmpDirectory / s"resparc3_2DDepth${vk.depth}2controls_TRYWeb2.vtk")
        val kernelFile = Utils.file(parameters)
        saveHyperRectangles(vk)(ak, s"/Users/laetitiazaleski/Desktop/Kernelresults/ParcRectangles_${fileName}.txt")
        println("done saving hyperRectangle")
        saveVTK3D(ak, s"/Users/laetitiazaleski/Desktop/Kernelresults/ParcVtk_${fileName}.vtk")
        println("done saving vtk")

        println(s"steps : ${steps}")

        KernelResult(kernelFile.toJava.getAbsolutePath, kernelFile.isEmpty)
    }
  //}

  def InterKernels(parameters1: KernelParameters, parameters2: KernelParameters): KernelResult = {

    val resFile = Utils.file(parameters1)

    resFile.exists match {
      case true => KernelResult(resFile.toJava.getAbsolutePath, resFile.isEmpty)
      case false =>
        val parc = Parc3D(
          l = parameters1.l,
          g = parameters1.g,
          M = parameters1.M,
          p = parameters1.p,
          a = parameters1.a,
          eta = parameters1.eta,
          phi = parameters1.phi,
          phi2 = parameters1.phi2,
          del = parameters1.del,
          mp = parameters1.mp,
          mt = parameters1.mt)

        implicit val rng = new util.Random(42)


        var zmax = parameters1.zetaMax //* 0.01
        var zmin = parameters1.zetaMin //* 0.01

        if(parameters1.zetaMin > parameters1.zetaMax){
          zmin = parameters1.zetaMax //* 0.01
          zmax = parameters1.zetaMin // * 0.01
        }
        var emax = parameters1.epsMax
        var emin = parameters1.epsMin

        if(parameters1.epsMin > parameters1.epsMax) {
          emax = parameters1.epsMin
          emin = parameters1.epsMax
        }



        val vk1 = KernelComputation(
          dynamic = parc.dynamic,
          depth = 15,
          zone = Vector((parameters1.Cmin, parameters1.Cmax), (parameters1.Amin, parameters1.Amax), (parameters1.Tmin, parameters1.Tmax)),
          // controls = Vector((0.02 to 0.4 by 0.02 ))
          controls = (x: Vector[Double]) =>
            for {
              c1 <- (zmin to zmax by 0.001)
              c2 <- (emin to emax by 10.0)
            } yield Control(c1, c2)

        )

         zmax = parameters2.zetaMax  //* 0.01
         zmin = parameters2.zetaMin //* 0.01

        if(parameters1.zetaMin > parameters1.zetaMax){
          zmin = parameters2.zetaMax //* 0.01
          zmax = parameters2.zetaMin //* 0.01
        }
         emax = parameters2.epsMax
         emin = parameters2.epsMin

        if(parameters2.epsMin > parameters1.epsMax) {
          emax = parameters2.epsMin
          emin = parameters2.epsMax
        }

        val vk2 = KernelComputation(
          dynamic = parc.dynamic,
          depth = 12,
          zone = Vector((parameters2.Cmin, parameters2.Cmax), (parameters2.Amin, parameters2.Amax), (parameters2.Tmin, parameters2.Tmax)),
          // controls = Vector((0.02 to 0.4 by 0.02 ))
          controls = (x: Vector[Double]) =>
            for {
              c1 <- (zmin to zmax by 0.001)
              c2 <- (emin to emax by 10.0)
            } yield Control(c1, c2)

        )

        var (ak1, steps1) = approximate(vk1, rng)
        var (ak2, steps2) = approximate(vk2, rng)

        val inter = learnIntersection(ak1, ak2)

        val vol = volume(inter)
        println(s"volume inter $vol")

        //saveVTK3D(inter, "/tmp/inter.vtk")

        val kernelFile = Utils.file(parameters1)
       // saveHyperRectangles(vk1)(inter, kernelFile)
        saveVTK3D(inter, Settings.defaultViaducDirectory / s"inter_3D_D${vk1.depth}_V_${c_min}_${c_max}_${a_min}_" +
          s"${a_max}_${t_min}_${t_max}_C_${eps_min}_${eps_max}_${zeta_min}_${zeta_max}.vtk")
        println(steps1)

        KernelResult(kernelFile.toJava.getAbsolutePath, kernelFile.isEmpty)

    }
  }


  def IntersectionKernels(f1 : String, f2 : String): Unit ={
    implicit val rng = new util.Random(42)
    val ak1 = load[Tree[KernelContent]](f1)
    val ak2 = load[Tree[KernelContent]](f2)
    val result = learnIntersection(ak1,ak2)
    saveVTK3D(result, Settings.defaultViaducDirectory / s"inter_3D_D${f1}_AND_${f2}.vtk")
   // saveHyperRectangles(result, s"inter_3D_D${f1}_AND_${f2}.txt")
    saveVTK3D(result, s"inter_3D_D${f1}_AND_${f2}.vtk")

  }


}
