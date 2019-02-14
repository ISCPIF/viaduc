package fr.iscpif.client

import java.awt.image.Kernel

import org.scalajs.dom.svg.A
import scaladget.tools.JsRxTags._
import rx.{Rx, Var}
import scaladget.bootstrapslider.{Slider, SliderOptions}
import scalatags.JsDom
import scalatags.JsDom.all.{div, marginTop, paddingLeft, span}
import scaladget.bootstrapslider._
import collection._
import simpark.shared.Api
import simpark.shared.Data._

object Utils {

  def sliders2kernelParam(sliders : List[Slider], slidersdouble : List[Slider] ): KernelParameters =
    {

      val alpha = sliders(10).getValue().toString.toDouble
      val ip = sliders(7).getValue().toString.toDouble
      val it = sliders(9).getValue().toString.toDouble
      val l = sliders(1).getValue().toString.toDouble
      val g = sliders(0).getValue().toString.toDouble
      val M = sliders(3).getValue().toString.toInt
      val p = sliders(2).getValue().toString.toDouble
      val a = sliders(11).getValue().toString.toDouble
      val eta = sliders(4).getValue().toString.toDouble
      val phi = sliders(12).getValue().toString.toDouble
      val phi2 = sliders(14).getValue().toString.toDouble
      val e = sliders(13).getValue().toString.toDouble
      val del = sliders(5).getValue().toString.toDouble
      val mp = sliders(6).getValue().toString.toDouble
      val mt = sliders(8).getValue().toString.toDouble

      val Cmax = sliderDoubleToDouble(slidersdouble, 3, 0)
      val Cmin = sliderDoubleToDouble(slidersdouble, 3, 1)
      val Amax =  sliderDoubleToDouble(slidersdouble, 2, 0)
      val Amin = sliderDoubleToDouble(slidersdouble, 2, 1)
      val Tmax = sliderDoubleToDouble(slidersdouble, 4, 0)
      val Tmin = sliderDoubleToDouble(slidersdouble, 4, 1)

      val epsMax = sliderDoubleToDouble(slidersdouble, 0, 0)
      val epsMin = sliderDoubleToDouble(slidersdouble, 0, 1)
      val zetaMax = sliderDoubleToDouble(slidersdouble, 1, 0)
      val zetaMin = sliderDoubleToDouble(slidersdouble, 1, 1)


      KernelParameters(Cmax: Double, Cmin, Amax, Amin, Tmax, Tmin, alpha, ip, it, l,
        g, M, p, a, eta, phi, phi2, del, mp, mt,e, epsMax, epsMin, zetaMax,zetaMin)
  }

  def sliderDoubleToDouble(slidersdouble : List[Slider], i : Int /*indice*/, j: Int /* 0 ou 1 */): Double =
  {

    val Cstring = slidersdouble(i).getValue().toString.split(",")
    Cstring(j).toDouble

  }

}
