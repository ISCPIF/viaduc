package fr.iscpif.client



import scalatags.JsDom.all.{div, _}
import scaladget.tools.JsRxTags._
import rx.{Rx, Var}
import scaladget.bootstrapslider.{Slider, SliderOptions}
import scalatags.JsDom
import scalatags.JsDom.all.{div, marginTop, paddingLeft, span}

object SlidersUtils {

  def createSlider(max: Double, min : Double,  value: Double, step: Double): Slider = {

    val sliderValue = Var("None")
    val aDiv = div(marginTop := 20).render

    val options = SliderOptions
      .max(max)
      .min(min)
      .value(value)
      .step(step)
      .tooltip(SliderOptions.ALWAYS)

    val slider = Slider(aDiv, options)
    slider.on(Slider.CHANGE, () => {
      sliderValue() = slider.getValue.toString
    })


   val foo = span(
      aDiv,
      span(Rx {
        sliderValue()
      },
        paddingLeft := 10
      )
    )

    slider
  }

  /*val sliderValue1 = Var("None")

    val options1 = SliderOptions
      .max(100)
      .min(0)
      .value(js.Array[scala.Double](14.0, 92.0))
      .tooltip(SliderOptions.ALWAYS)

    val slider1 = Slider(aDiv, options1)
    slider1.on(Slider.CHANGE, () => {
      sliderValue1() = slider1.getValue.toString
    })


    val epsslide = span(
      aDiv,
      span(Rx {
        sliderValue1()
      },
        paddingLeft := 10
      )
    )*/

}

