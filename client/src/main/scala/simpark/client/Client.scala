package simpark.client

import java.nio.ByteBuffer

import CAT_RK4._
import SlidersUtils._
import File2shapes._
import boopickle.Default._
import org.scalajs.dom

import scala.Array.concat
import scala.List
import scala.concurrent.Future
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel, ScalaJSDefined}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}
import com.definitelyscala.plotlyjs.PlotData
import org.scalajs.dom.html
import org.scalajs.dom.document

import scala.scalajs
import scala.scalajs.js
import scalatags.JsDom.all.{div, _}

import scala.util.matching._
import scala.scalajs.js.JSConverters._
import com.definitelyscala.plotlyjs._
import com.definitelyscala.plotlyjs.all._
import com.definitelyscala.plotlyjs.PlotlyImplicits._
import com.definitelyscala.plotlyjs.plotlyConts._
import org.querki.jsext.{JSOptionBuilder, noOpts}
import org.scalajs.dom
import org.scalajs.dom.raw._
import autowire._
import scaladget.tools.JsRxTags._

import Array._
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global
import boopickle.Default._
import com.definitelyscala.plotlyjs.Shape
import com.definitelyscala.plotlyjs
import com.definitelyscala.plotlyjs.Plotly.Datum
//import com.fasterxml.jackson.annotation.JsonFormat.Shape

import scala.util.{Failure, Success}
import rx._
import scaladget.bootstrapnative.bsn._
import Utils._

import KernelStatus._

import simpark.shared.Api
import simpark.shared.Data._
import simpark.shared._


import scaladget.bootstrapslider._

object Client {


  @JSExportTopLevel("run")
  def run() {
    implicit val ctx: Ctx.Owner = Ctx.Owner.safe()
    lazy val rng = scala.util.Random

    def randomDoubles(nb: Int = 100, ratio: Int = 1000) = Seq.fill(nb)(rng.nextDouble * ratio).toJSArray

    def randomInts(nb: Int = 100, ratio: Int = 1000) = Seq.fill(nb)(rng.nextInt(ratio)).toJSArray

    /** *************** Sliders *****************/

    /** * parametres ***/

    val slidersParam = Array(/*g 0: */ Array(0.5, 0.0, 0.208, 0.001), /* l 1: */ Array(0.001, 0.0, 0.00052, 0.00001), /*p 2*/ Array(0.03, 0.0, 0.0136, 0.0001),
      /*zeta controle*/
      /*M 3*/ Array(50000.0, 10000.0, 36000.0, 1000.0), /*eta 4*/ Array(0.001, 0.0, 0.0008, 0.0001),
      /*eps controle*/
      /*delta 5:*/ Array(0.1, 0.0, 0.01, 0.001), /*mp 6*/ Array(100.0, 0.0, 14.0, 1.0), /*ip 7*/ Array(1.0, 0.0, 0.01, 0.01),
      /*mt 8*/ Array(100.0, 0.0, 25.0, 1.0), /*it 9*/ Array(1.0, 0.0, 0.01, 0.01), /*alpha 10*/ Array(2.0, 0.0, 0.9, 0.1), /*a 11*/ Array(15000.0, 5000.0, 10000.0, 0.1),
      /*phi 12*/ Array(3000.0, 1000.0, 1833.0, 10.0), /*e 13*/ Array(100.0, 0.0, 50.0, 10.0), /*phi2 14*/ Array(50000.0, 0.0, 10000.0, 1000.0))
    var sliders: List[Slider] = List()
    var foos: List[scalatags.JsDom.Modifier] = List()


    for (set <- slidersParam) {
      // val set = Array(0.01, 0.0, 0.00052)

      val sliderValue = Var(set(2).toString)
      val aDiv = div(marginTop := 30).render

      val options = SliderOptions
        .max(set(0))
        .min(set(1))
        .value(set(2))
        .step(set(3))
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

      sliders = slider :: sliders
      foos = foo :: foos

    }

    sliders = sliders.reverse
    foos = foos.reverse

    /** * controles ***/


    val slidersControl = Array(Array(30000.0, 0.0, 100.0), Array(30000.0, 0.0, 100.0), Array(30000.0, 0.0, 100.0), Array(0.2, 0.01, 0.01), Array(100.0, 0.0, 10.0))
    var slidersDouble: List[Slider] = List()
    var foosDouble: List[scalatags.JsDom.Modifier] = List()
    // val aDiv = div(marginTop := 30).render


    for (set <- slidersControl) {
      val aDiv = div(marginTop := 30).render
      val sliderValue = Var(s"${set(1)} , ${set(0)}")

      val optionsDouble = SliderOptions
        .max(set(0))
        .min(set(1))
        .value(js.Array[scala.Double](set(0), set(1)))
        .step(set(2))
        .tooltip(SliderOptions.ALWAYS)

      val sliderDouble = Slider(aDiv, optionsDouble)
      sliderDouble.on(Slider.CHANGE, () => {
        sliderValue() = sliderDouble.getValue.toString
      })


      val slide = span(
        aDiv,
        span(Rx {
          sliderValue()
        },
          paddingLeft := 10
        )
      )

      //println(sliderDouble.getValue())

      slidersDouble = sliderDouble :: slidersDouble
      foosDouble = slide :: foosDouble
    }

    val Cmax = sliderDoubleToDouble(slidersDouble, 3, 0)
    val Cmin = sliderDoubleToDouble(slidersDouble, 3, 1)

    val Amax = sliderDoubleToDouble(slidersDouble, 2, 0)
    val Amin = sliderDoubleToDouble(slidersDouble, 2, 1)

    val Tmax = sliderDoubleToDouble(slidersDouble, 4, 0)
    val Tmin = sliderDoubleToDouble(slidersDouble, 4, 1)

    val Emax = sliderDoubleToDouble(slidersDouble, 0, 0)
    val Emin = sliderDoubleToDouble(slidersDouble, 0, 1)

    val Zmax = sliderDoubleToDouble(slidersDouble, 1, 0)
    val Zmin = sliderDoubleToDouble(slidersDouble, 1, 1)
    //  controles :

    val check_Eps = input(
      `type` := "checkbox",
      value := "Eps"
    ).render

    val check_zeta = input(
      `type` := "checkbox",
      value := "Zeta"
    ).render

    // box conditions initiales :


    val box_Cinit = input(
      `type` := "text",
      value := "10000.0"
    ).render

    val box_Ainit = input(
      `type` := "text",
      value := "5000.0"
    ).render

    val box_Tinit = input(
      `type` := "text",
      value := "5000.0"
    ).render


    /* Calcul de CAT RK4 */

    // controles et valeur init:

    var eps = 100.0
    var zeta = 0.1
    var Cinit = box_Cinit.value.toDouble
    var Ainit = box_Ainit.value.toDouble
    var Tinit = box_Tinit.value.toDouble
    var t_max = 1000 //10


    var Cval = Array(Cinit / 100)
    var Aval = Array(Ainit)
    var Tval = Array(Tinit)
    var time = Array(0)

    // calcul:
    for (t <- 1 to t_max) {

      val Catinit = compute_CAT_RK4(zeta,
        sliders(1).getValue().toString.toDouble,
        sliders(0).getValue().toString.toDouble,
        sliders(3).getValue().toString.toDouble,
        sliders(10).getValue().toString.toDouble,
        sliders(2).getValue().toString.toDouble,
        sliders(11).getValue().toString.toDouble,
        sliders(13).getValue().toString.toDouble,
        sliders(4).getValue().toString.toDouble,
        eps, sliders(12).getValue().toString.toDouble,
        sliders(14).getValue().toString.toDouble,
        sliders(5).getValue().toString.toDouble,
        sliders(6).getValue().toString.toDouble,
        sliders(8).getValue().toString.toDouble,
        sliders(7).getValue().toString.toDouble,
        sliders(9).getValue().toString.toDouble,
        Cval.last * 100, Aval.last, Tval.last)

      Cval = concat(Cval, Array(Catinit(0) / 100))
      Aval = concat(Aval, Array(Catinit(1)))
      Tval = concat(Tval, Array(Catinit(2)))
      time = concat(time, Array(t))

    }

    // plot avec definitivly scala :


    val plotDiv = div.render

    val layout = Layout
      .title("C, A and T in fuction of time")
      .showlegend(true)
      .xaxis(plotlyaxis.title("Time"))
      .yaxis(plotlyaxis.title("C, A and T value"))

    val data = PlotData
      .set(plotlymode.markers.lines)
      .set(plotlymarker.set(plotlysymbol.square))

    val data1 = data
      .x(time.toJSArray)
      .y(Cval.toJSArray)
      .set(plotlymarker.size(1.0).set(plotlycolor.rgb(180, 0, 0)))
      .name("C")

    val data2 = data
      .x(time.toJSArray)
      .y(Aval.toJSArray)
      .set(plotlymarker.size(1.0).set(plotlycolor.rgb(0, 136, 170)).set(plotlysymbol.cross))
      .name("A")

    val data3 = data
      .x(time.toJSArray)
      .y(Tval.toJSArray)
      .set(plotlymarker.size(1.0).set(plotlycolor.rgb(50, 205, 50)).set(plotlysymbol.cross))
      .name("T")

    // val config = Config.displayModeBar(false)

    val plot = Plotly.newPlot(plotDiv,
      js.Array(data1, data2, data3),
      layout)


    /** ************* Bouton  Crise ***************/


    val check_crise = input(
      `type` := "checkbox"
    ).render


    /** ******** Bouton Show Constraints ****************/

    val buttonShowConstraints = button("Show Constraints on above graph").render

    /** ******** Bouton Try with valid set :  ****************/


    val buttonTryThis = button("Try this !").render
    val buttonTryThis2 = button("Try this !").render


    /** ******** bouton pour rafraichir le graphe : ************/

    val buttonPlot = button("Plot").render


    buttonPlot.onclick = (e: dom.Event) => {
      println("********************* PLOT ***************")

      val eps = 100.0
      val zeta = 0.1
      val t_max = 1000 // 10

      var Cinit = box_Cinit.value.toDouble
      var Ainit = box_Ainit.value.toDouble
      var Tinit = box_Tinit.value.toDouble

      var Cval = Array(Cinit)
      var Aval = Array(Ainit)
      var Tval = Array(Tinit)
      var time = Array(0)

      val coefAc = 100 // coeficient Animaux crise
      val Tc = 10 // temps t crise

      val Cat = compute_CAT_RK4(zeta, sliders(1).getValue().toString.toDouble,
        sliders(0).getValue().toString.toDouble,
        sliders(3).getValue().toString.toDouble,
        sliders(10).getValue().toString.toDouble,
        sliders(2).getValue().toString.toDouble,
        sliders(11).getValue().toString.toDouble,
        sliders(13).getValue().toString.toDouble,
        sliders(4).getValue().toString.toDouble,
        eps, sliders(12).getValue().toString.toDouble,
        sliders(14).getValue().toString.toDouble,
        sliders(5).getValue().toString.toDouble,
        sliders(6).getValue().toString.toDouble,
        sliders(8).getValue().toString.toDouble,
        sliders(7).getValue().toString.toDouble,
        sliders(9).getValue().toString.toDouble,
        Cinit, Ainit, Tinit)
      // calcul:
      for (t <- 1 to t_max) {

        val Catinit = compute_CAT_RK4(zeta, sliders(1).getValue().toString.toDouble,
          sliders(0).getValue().toString.toDouble,
          sliders(3).getValue().toString.toDouble,
          sliders(10).getValue().toString.toDouble,
          sliders(2).getValue().toString.toDouble,
          sliders(11).getValue().toString.toDouble,
          sliders(13).getValue().toString.toDouble,
          sliders(4).getValue().toString.toDouble,
          eps, sliders(12).getValue().toString.toDouble,
          sliders(14).getValue().toString.toDouble,
          sliders(5).getValue().toString.toDouble,
          sliders(6).getValue().toString.toDouble,
          sliders(8).getValue().toString.toDouble,
          sliders(7).getValue().toString.toDouble,
          sliders(9).getValue().toString.toDouble,
          Cval.last , Aval.last, Tval.last)

        if ((t == Tc) && (check_crise.checked)) {
          println("crisis")
          Cval = concat(Cval, Array(Cat(0)))
          Aval = concat(Aval, Array(Cat(1) / coefAc))
          Tval = concat(Tval, Array(Cat(2)))
          time = concat(time, Array(t))

        } else {

          Cval = concat(Cval, Array(Cat(0)))
          Aval = concat(Aval, Array(Cat(1)))
          Tval = concat(Tval, Array(Cat(2)))
          time = concat(time, Array(t))

        }

      }


      /** ******** Plot C, A, T : ***********/
      val data1 = data
        .x(time.toJSArray)
        .y(Cval.toJSArray)
        .set(plotlymarker.size(1.0).set(plotlycolor.rgb(180, 0, 0)))
        .name("C")

      val data2 = data
        .x(time.toJSArray)
        .y(Aval.toJSArray)
        .set(plotlymarker.size(1.0).set(plotlycolor.rgb(0, 136, 170)).set(plotlysymbol.cross))
        .name("A")

      val data3 = data
        .x(time.toJSArray)
        .y(Tval.toJSArray)
        .set(plotlymarker.size(1.0).set(plotlycolor.rgb(50, 205, 50)).set(plotlysymbol.cross))
        .name("T")



      /** ******** Limites sur C, A, T : ***********/
      var CmaxVal = Array(Cmax)
      CmaxVal = CmaxVal.padTo(t_max,Cmax)

      var CminVal = Array(Cmin)
      CminVal = CminVal.padTo(t_max,Cmin)

      var AmaxVal = Array(Amax)
      AmaxVal = AmaxVal.padTo(t_max,Amax)

      var AminVal = Array(Amin)
      AminVal = AminVal.padTo(t_max,Amin)

      var TmaxVal = Array(Tmax)
      TmaxVal = TmaxVal.padTo(t_max,Tmax)

      var TminVal = Array(Tmax)
      TminVal = TminVal.padTo(t_max,Tmax)


      val dataCmax = data
        .x(time.toJSArray)
        .y(CmaxVal.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(249, 149, 128)))
        .name("Cmax")

      val dataCmin = data
        .x(time.toJSArray)
        .y(CminVal.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(249, 149, 128)))
        .name("Cmin")

      val dataAmax = data
        .x(time.toJSArray)
        .y(AmaxVal.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(128, 170, 249)))
        .name("Amax")

      val dataAmin = data
        .x(time.toJSArray)
        .y(AminVal.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(128, 170, 249)))
        .name("Amin")

      val dataTmax = data
        .x(time.toJSArray)
        .y(TmaxVal.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(200, 235, 89)))
        .name("Tmax")

      val dataTmin = data
        .x(time.toJSArray)
        .y(TminVal.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(200, 235, 89)))
        .name("Tmin")


      val plot = Plotly.newPlot(plotDiv,
        js.Array(dataTmin, dataTmax, dataAmin, dataAmax, dataCmin, dataCmax, data1, data2, data3),
        layout)

    }


    /** *************** Noyau *********************/

    val kernelStatus: Var[KernelStatus] = Var(KernelStatus.NOT_COMPUTED_YET)

    val addButtonVideOrNot = button("Show Kernel").render

    lazy val addButtonCalc = button("Compute Kernel",
      onclick := { () =>
        kernelStatus() = KernelStatus.COMPUTING_KERNEL

        Post[Api].CalcKernel(sliders2kernelParam(sliders, slidersDouble),check_Eps.checked,check_zeta.checked).call()
          .foreach { kr: KernelResult =>
            kernelStatus() = KernelStatus.computedKernel(kr)

          }
      }

    )


    lazy val addButtonClosestKernel = button("Closest Kernel",
      onclick := { () =>
        kernelStatus() = KernelStatus.COMPUTING_KERNEL
        Post[Api].ClosestKernel(sliders2kernelParam(sliders, slidersDouble)).call()
          .foreach { kr: KernelResult =>
            kernelStatus() = KernelStatus.computedKernel(kr)

          }
          }
    )


    lazy val addButtonIntersection = button("Intersect Kernels",
      onclick := { () =>
     //   kernelStatus() = KernelStatus.COMPUTING_KERNEL
        val file1 = document.getElementById("file_inter1").asInstanceOf[HTMLInputElement].files.item(0)
        val file2 = document.getElementById("file_inter2").asInstanceOf[HTMLInputElement].files.item(0)
        val f1 = file1.name
        val f2 = file2.name

        Post[Api].IntersectionKernels(/*f1,f2*/).call()
      }
    )



    lazy val addButtonInter = button("Intersect Kernels",
      onclick := { () =>
   //     kernelStatus() = KernelStatus.COMPUTING_KERNEL

        val parameters = sliders2kernelParam(sliders, slidersDouble)

        Post[Api].InterKernels(parameters,parameters).call().foreach { kr: KernelResult =>
     //     kernelStatus() = KernelStatus.computedKernel(kr)
        }
      }
    )

    val onoff = Var(false)

    // affichage du noyau :

    val KernelDiv0 = div.render
    val KernelDiv1 = div.render
    val KernelDiv2 = div.render
    val KernelDiv3 = div.render
    val KernelDiv4 = div.render
    val KernelDiv5 = div.render
    val KernelDiv6 = div.render

    lazy val showKernelbutton = button("Show 3D Kernel from file",
      onclick := { () =>
        val file = document.getElementById("file_input").asInstanceOf[HTMLInputElement].files.item(0)

        var reader = new FileReader()
        reader.onload = (_: UIEvent) => {
          val text = s"${reader.result}"
          val content = document.getElementById("content")

          val layoutKernel1 = Layout //  1 : case Animaux Touristes
            .title("My Kernel")
            .showlegend(true)
            .yaxis(plotlyaxis.title("Tourists"))
            .xaxis(plotlyaxis.title("Turtles"))
            .shapes(File2shapes.FileToshapes(text, 1))

          val layoutKernel2 = Layout //  2 : case Capital Animaux
            .title("My Kernel")
            .showlegend(true)
            .yaxis(plotlyaxis.title("Capital"))
            .xaxis(plotlyaxis.title("Turtles"))
            .shapes(File2shapes.FileToshapes(text, 2))

          val layoutKernel3 = Layout //  3 : case Capital Animaux
            .title("My Kernel")
            .showlegend(true)
            .yaxis(plotlyaxis.title("Capital"))
            .xaxis(plotlyaxis.title("Tourists"))
            .shapes(File2shapes.FileToshapes(text, 3))

          val data = PlotData
            .set(plotlymode.markers.lines)
            .set(plotlymarker.set(plotlysymbol.square))

          val dataKernel = data
            .x((Array(1.5, 4.5)).toJSArray)
            .y((Array(0.75, 0.75)).toJSArray)
            .text("Kernel")

          val plotshapes1 = Plotly.newPlot(KernelDiv1, js.Array(dataKernel), layoutKernel1)
          val plotshapes2 = Plotly.newPlot(KernelDiv2, js.Array(dataKernel), layoutKernel2)
          val plotshapes3 = Plotly.newPlot(KernelDiv3, js.Array(dataKernel), layoutKernel3)
        }
        reader.readAsText(file)
      })

    lazy val show2Kernelsbutton = button("Show multiple kernels from files",
      onclick := { () =>
        val file1 = document.getElementById("file_inter1").asInstanceOf[HTMLInputElement].files.item(0)
        var reader1 = new FileReader()
        val file2 = document.getElementById("file_inter2").asInstanceOf[HTMLInputElement].files.item(0)
        var reader2 = new FileReader()

        reader2.onload = (_: UIEvent) => {
          val text1 = s"${reader1.result}"
       //   println(text1.split(" ")(0))
          val text2 = s"${reader2.result}"
      //    println(text2.split(" ")(0))
          val content = document.getElementById("content")

          val layoutKernel4 = Layout //  1 : case Animaux Touristes
            .title("My Kernel")
            .showlegend(true)
            .yaxis(plotlyaxis.title("Tourists"))
            .xaxis(plotlyaxis.title("Turtles"))
            .shapes(File2shapes.FileToshapes2(text1,text2, 1))

          println("tout va bien 0")

          val layoutKernel5 = Layout //  2 : case Capital Animaux
            .title("My Kernel")
            .showlegend(true)
            .yaxis(plotlyaxis.title("Capital"))
            .xaxis(plotlyaxis.title("Turtles"))
            .shapes(File2shapes.FileToshapes2(text1,text2, 2))

          println("tout va bien 1")

          val layoutKernel6 = Layout //  3 : case Capital Animaux
            .title("My Kernel")
            .showlegend(true)
            .yaxis(plotlyaxis.title("Capital"))
            .xaxis(plotlyaxis.title("Tourists"))
            .shapes(File2shapes.FileToshapes2(text1,text2, 3))

          println("tout va bien 2")

          val data = PlotData
            .set(plotlymode.markers.lines)
            .set(plotlymarker.set(plotlysymbol.square))

          val dataKernel = data
            .x((Array(1.5, 4.5)).toJSArray)
            .y((Array(0.75, 0.75)).toJSArray)
            .text("Kernel")

          val plotshapes1 = Plotly.newPlot(KernelDiv4, js.Array(dataKernel), layoutKernel4)
          val plotshapes2 = Plotly.newPlot(KernelDiv5, js.Array(dataKernel), layoutKernel5)
          val plotshapes3 = Plotly.newPlot(KernelDiv6, js.Array(dataKernel), layoutKernel6)
        }
        reader1.readAsText(file1)
        reader2.readAsText(file2)
      })


    /* HTML tags : */

    val equationVisible = Var(false)
    val paramVisible = Var(false)



    dom.document.body.appendChild(
      div(width := "80%", height := "80%")(
        h1("  Welcome to Viaduc: "),
        h2("  What is Viaduc ? "),
        p(panel("Viaduc is a Viability Expert Agent based on a viability analysis (Wei et al., 2012). It is based on the Viability theory described by Aubin (1992)"
          + " During a game session, the player is able to choose constraints, controls (i.e how to control the system in order to keep it within the constraints)" +
          " and will be able to change the value of some parameter if he or she doesn’t agree with the dynamic." +
          "capacity of the viability expert agent to help a player to analyze one viability kernel corresponding to a set of constraints that he himself decided, but also to compare with alternative kernels/constraints explored by himself or proposed by other players during the negotiation."
          + " Therefore, this provides the players with a basic way to quantify and analyze the degree of feasibility and viability of proposals. Instead of just comparing the constraint sets, the viability expert compares the viability kernels, which are based on the link between the dynamics and the constraints.\n."
          + " Small changes in constraint sets can have a broad range of impacts depending on the dynamics.")(width := 800)),
        p(
          button("About the equations", btn_primary, onclick := { () => equationVisible.update(!equationVisible.now) }),
          equationVisible.expand(panel("C represents the fishermen's capital (boats, income...)," +
            " A represents the number of Animals (turtles) in the parc, T represents the number of tourists in the parc")(width := 400))),
        p(
          buttonIcon("Scheme", btn_primary).expandOnclick(img(src := "img/CAT_Schemat.png")(width := 400))),
        p(
          buttonIcon("Equation", btn_primary).expandOnclick(img(src := "img/CAT_Equation.png")(width := 400))),



        h2("  Step 1 : Set up the parameters :"),
        h3(
          " Here you can change the parameters :"
        ),

        h4("Parameters for Equation A:"),
        p("g: is the growth rate of the turtle population, from [2]"),
        div(foos(0)),
        p("l: corresponds to the mortality rate of turtles related to direct interaction with tourists, from [1] "),
        div(foos(1)),
        p("p: is the deaths caused by traditionnal fishing, from [4]"),
        div(foos(2)),
        p("M: is the maximum capacity of the environment, from [3]"),
        div(foos(3)),
        p("eta: Is the dammages caused by a tourist on the environment, from [5]"),
        div(foos(4)),


        h4("Parameters for Equation C:"),
        p("delta: is the fishermen's infrastructures depreciation rate, from [6]"),
        div(foos(5)),
        p("mp: is the price for the number of fishes caught for each turtle caught, from [7]"),
        div(foos(6)),
        p("ip : is the proportion of fishing income that is invested in infrastructure"),
        div(foos(7)),
        p("mp: is the price for the number of fishes caught for each turtle caught, from [7]"),
        div(foos(8)),
        p("it : is the proportion of tourism-related income that is invested in infrastructure [8]"),
        div(foos(9)),


        h4("Parameters for Equation T:"),
        p("alpha: is the congestion parameter"),
        div(foos(10)),
        p("a: is the attractiveness associated with high number of turtles"),
        div(foos(11)),
        p("e: is the attractiveness associated with high quality of fishermen’s infrastructures"),
        div(foos(13)),
        p("phi: is the half saturation constant, namely the number of turtles at which tourist satisfaction is half maximum"),
        div(foos(12)),
        p("phi2: is the half-maximum saturation constraint related to fishing infrastructure"),
        div(foos(14)),


        p(
          buttonIcon("References for the parameters", btn_primary).expandOnclick(panel(
            "[1] Christina A.D. Semeniuk, Wolfgang Haider, and Kristina. Cooper, Andrew D. Rothley." +
              " A linked model of animal ecology and human behavior for the management of wildlife tourism. Ecological Modelling, 2010.\n" +

              "[2] JOHN R. HENDRICKSON. The green sea turtle, chelonia mydas (linn.) in malaya and sarawak." +
              " Proceedings of the Zoological Society of London, 130(4):455–535. \n" +

              "[3] Brendan Godley, Annette C Broderick, and Graeme. Hays. Nesting of green turtles (chelonia mydas) at ascension island, south atlantic." +
              " biological conservation. Biological conservation, 97(22):151–158, februay 2001.\n" +

              "[4] Bugonia Leandro, do Valle (UFRJ) Rogerio, de Aragao Bastos, and Maria Virgınia Petryb. Marine debris and human impacts on sea turtles " +
              "in southern brazil. Marine Pollution Bulletin, Volume 42, Issue 12, pages 1330–1334, December 2001. \n" +

              " [5] LARS BEJDER, AMY SAMUELS, HAL WHITEHEAD, NICK GALES, JANET MANN, RICHARD CONNOR, MIKE HEITHAUS, JANA WATSON-CAPPS, CINDY FLAHERTY, " +
              "and MICHAEL KRÜTZEN. De- cline in relative abundance of bottlenose dolphins exposed to long-term disturbance. Conservation Biology, 20(6):1791–1798.\n" +

              " [6] Lee Bun, Song. Measurement of capital depreciation within the japanese fishing fleet. The Review of Economics and Statistics," +
              " Vol. 60, No. 2, pages 225–237, Apr. 1978. \n" +


              " [7] Fabricio Molica de Mendonca (UFSJ), Lıgia Krausea, and Ri- cardo Coutinho (UFF). A cadeia produtiva da pesca artesanal em arraial do cabo:" +
              " Analise e propostas de melhoria. ENCONTRO NACIONAL DE ENGENHARIA DE PRODUCAO Maturidade e desafios da Engenharia de Producao: competitividade das" +
              " empresas, condicoes de trabalho, meio ambiente, pages 1330–1334, October 2010.\n" +

              " [8] Valeria G. da Vinha (UFRJ), Peter May (CPDA/UFRRJ), and Liandra Peres Caldasso (CPDA/UFRRJ). Sustentabilidade da reserva " +
              "extrativista marinha de arraial do cabo, rj: tecnicas de pesquisa e resultados. 2008.\n"

          )(width := 800))),

        p(" "),
        p(check_crise, "Add a crisis to the scenario (for example, an oil spill)"),

        h3(
          " Here you can choose the initial conditions:"
        ),

        p("Number of Animal at the begining of the simulation :"),
        div(box_Ainit),
        p("Amount of the Fishermen's infrasctructures at the begining of the simulation :"),
        div(box_Cinit),
        p("Number of Tourists at the begining of the simulation :"),
        div(box_Tinit),


        h3("Choose the limits on A, C and T: "),
        div(p("Limits on the number of animals :")),
        div(foosDouble(2)),
        div(p("Limits on the infrastructure's capital :")),
        div(foosDouble(3)),
        div(p("Limits on the number of tourist :")),
        div(foosDouble(4)),
        div(buttonShowConstraints),


        h3(
          " Here you can plot :"
        ),

        div(buttonPlot),

        h2("Capital, number of Animals and number of tourists in function of time for the above parameters (no controls):"),
        div(plotDiv.render),

        h2("Step 1 :Choose the controls: "),
        div(p(check_Eps, "Use the control epsilon")),
        p("Epsilon represents the effort to restore the environment, 100 is a maximum effort and 0 is no effort at all"),
        div(foosDouble(0)),
        div(p(check_zeta, "Use the control zeta")),
        p("Zeta represents the areas reachable by the tourists, at 0.2 we consiter that the parc is completely open, " +
          "at 0.01 we consider that only a small portion of the parc is open to tourism"),
        div(foosDouble(1)),

        h2("Step 3 Compute your viability kernel: "),
        div(addButtonCalc),
        p(
          Rx {
            kernelStatus() match {
              case kr@(KernelStatus.NOT_COMPUTED_YET | KernelStatus.COMPUTING_KERNEL) =>
                kr.message
              case ks: KernelStatus =>
                ks.kernelResult match {
                  case None => "Weird case ..."
                  case Some(kr: KernelResult) =>
                     val ks = kernelStatus.now

                    s"${ks.message} :: " + {
                      if (kr.isResultEmpty) "Your Kernel is empty, please try again by changing your controls and/or your constraints."
                      else s"Congratulation, your Kernel is not empty ! ${kr.resultPath}"
                    }
                }
            }
          }
        ),
        div(input(`type` := "file", id := "file_input").render),
        div(showKernelbutton),
        p(""),
        div(addButtonClosestKernel),
        p(""),
        div(KernelDiv1.render),
        p(""),
        div(KernelDiv2.render),
        p(""),
        div(KernelDiv3.render),
        p(""),
        div(input(`type` := "file", id := "file_inter1").render, input(`type` := "file", id := "file_inter2").render),
        p(""),
        div(show2Kernelsbutton),
        p(""),
        div(KernelDiv4.render),
        div(KernelDiv5.render),
        div(KernelDiv6.render)

      ).render
    )
  }
}

object Post extends autowire.Client[ByteBuffer, Pickler, Pickler] {

  override def doCall(req: Request): Future[ByteBuffer] = {
    dom.ext.Ajax.post(
      url = req.path.mkString("/"),
      data = Pickle.intoBytes(req.args),
      responseType = "arraybuffer",
      headers = Map("Content-Type" -> "application/octet-stream")
    ).map(r => TypedArrayBuffer.wrap(r.response.asInstanceOf[ArrayBuffer]))
  }

  override def read[Result: Pickler](p: ByteBuffer) = Unpickle[Result].fromBytes(p)

  override def write[Result: Pickler](r: Result) = Pickle.intoBytes(r)
}