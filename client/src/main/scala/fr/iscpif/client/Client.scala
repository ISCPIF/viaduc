package fr.iscpif.client

import java.nio.ByteBuffer

import fr.iscpif.client.CAT_RK4._
import fr.iscpif.client.File2shapes._
import boopickle.Default._
import org.scalajs.dom

import scala.Array.concat
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


//import shared.Api
//import shared.Data._
import shared.Api
import shared.Data._

import scaladget.bootstrapslider._

object Client {


  @JSExportTopLevel("run")
  def run() {
    implicit val ctx: Ctx.Owner = Ctx.Owner.safe()
    lazy val rng = scala.util.Random

    def randomDoubles(nb: Int = 100, ratio: Int = 1000) = Seq.fill(nb)(rng.nextDouble * ratio).toJSArray

    def randomInts(nb: Int = 100, ratio: Int = 1000) = Seq.fill(nb)(rng.nextInt(ratio)).toJSArray

    /* Definition des parametres */


    val box_l = input(
      `type` := "text",
      value := "0.00052"
    ).render

    /*  val output_l = span.render

      box_zeta.onkeyup = (e: dom.Event) => {
        output_l.textContent =
          box_l.value
      }
*/
    val box_g = input(
      `type` := "text",
      value := "0.37"
    ).render

    val box_M = input(
      `type` := "text",
      value := "36036"
    ).render

    /*  val box_h = input(
        `type` := "text",
        value := "0.00001"
      ).render */

    /*  val box_c = input(
        `type` := "text",
        value := "0.01"
      ).render */

    val box_p = input(
      `type` := "text",
      value := "0.0004"
    ).render

    val box_a = input(
      `type` := "text",
      value := "100000.0"
    ).render

    val box_e = input(
      `type` := "text",
      value := "100"
    ).render

    val box_eta = input(
      `type` := "text",
      value := "0.0008"
    ).render

    val box_phi = input(
      `type` := "text",
      value := "1833"
    ).render

    val box_phi2 = input(
      `type` := "text",
      value := "10000"
    ).render


    val box_delta = input(
      `type` := "text",
      value := "0.01"
    ).render

    val box_mp = input(
      `type` := "text",
      value := "14.0"
    ).render

    val box_mt = input(
      `type` := "text",
      value := "25"
    ).render

    val box_alpha = input(
      `type` := "text",
      value := "0.5"
    ).render

    //  controles :

    val check_Eps = input(
      `type` := "checkbox",
      value := "Eps"
    ).render

    val check_zeta = input(
      `type` := "checkbox",
      value := "Zeta"
    ).render

    val box_MaxEps = input(
      `type` := "text",
      value := "100"
    ).render

    val box_MinEps = input(
      `type` := "text",
      value := "0"
    ).render

    val box_MaxZeta = input(
      `type` := "text",
      value := "1"
    ).render

    val box_MinZeta = input(
      `type` := "text",
      value := "20"
    ).render

    // box etats souhaitables :

    val box_MaxC = input(
      `type` := "text",
      value := "100000"
    ).render

    val box_MinC = input(
      `type` := "text",
      value := "5000"
    ).render

    val box_MaxA = input(
      `type` := "text",
      value := "10000"
    ).render

    val box_MinA = input(
      `type` := "text",
      value := "1000"
    ).render

    val box_MaxT = input(
      `type` := "text",
      value := "15000"
    ).render

    val box_MinT = input(
      `type` := "text",
      value := "1000"
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

    // box inutiles : à retirer

    val box_h = input(
      `type` := "text",
      value := "0"
    ).render

    val box_c = input(
      `type` := "text",
      value := "1"
    ).render

    val box_d = input(
      `type` := "text",
      value := "1.0"
    ).render

    /* Calcul de CAT RK4 */

    // controles et valeur init:

    var eps = 100.0
    var zeta = 0.1
    var Cinit = box_Cinit.value.toDouble
    var Ainit = box_Ainit.value.toDouble
    var Tinit = box_Tinit.value.toDouble
    var t_max = 500

    var Cval = Array(Cinit / 10)
    var Aval = Array(Ainit)
    var Tval = Array(Tinit)
    var time = Array(0)
    // calcul:
    for (t <- 1 to t_max) {

      val Catinit = compute_CAT_RK4(zeta, box_l.value.toDouble, box_g.value.toDouble, box_M.value.toDouble, box_h.value.toDouble,
        box_c.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
        eps, box_phi.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_mp.value.toDouble,
        box_mt.value.toDouble, Cval.last * 10, Aval.last, Tval.last)

      Cval = concat(Cval, Array(Catinit(0) / 10))
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


    /* val clicked = Var("None")

     val buttonStyle: ModifierSeq = Seq(
       marginRight := 5,
       marginTop := 5
     )

     lazy val theRadios: SelectableButtons = radios()(
       selectableButton("No Crisis", onclick = radioAction),
       selectableButton("Oil Spill", true, onclick = radioAction)
     )
      def radioAction = () => active() = theRadios.active
     */
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
      val t_max = 10000

      var Cinit = box_Cinit.value.toDouble
      var Ainit = box_Ainit.value.toDouble
      var Tinit = box_Tinit.value.toDouble

      var Cval = Array(Cinit)
      var Aval = Array(Ainit)
      var Tval = Array(Tinit)
      var time = Array(0)

      val coefAc = 100 // coeficient Animaux crise
      val Tc = 1000 // temps t crise

      var Cat = compute_CAT_RK4(zeta, box_l.value.toDouble, box_g.value.toDouble, box_M.value.toDouble, box_h.value.toDouble,
        box_c.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
        eps, box_phi.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_mp.value.toDouble,
        box_mt.value.toDouble, Cinit, Ainit, Tinit)

      // calcul:
      for (t <- 1 to t_max) {


        Cat = compute_CAT_RK4(zeta, box_l.value.toDouble, box_g.value.toDouble, box_M.value.toDouble, box_h.value.toDouble,
          box_c.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
          eps, box_phi.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_mp.value.toDouble,
          box_mt.value.toDouble, Cval.last * 10, Aval.last, Tval.last)

        if ((t == Tc) && (check_crise.checked)) {
          println("crisis")
          Cval = concat(Cval, Array(Cat(0) / coefAc))
          Aval = concat(Aval, Array(Cat(1)))
          Tval = concat(Tval, Array(Cat(2)))
          time = concat(time, Array(t))

          /* Cat = compute_CAT_RK4(zeta, box_l.value.toDouble, box_g.value.toDouble, box_M.value.toDouble, box_h.value.toDouble,
            box_c.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
            eps, box_phi.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_mp.value.toDouble,
            box_mt.value.toDouble, Cval.last * 10, Aval.last / coefAc, Tval.last) */

        } else {

          Cval = concat(Cval, Array(Cat(0) / 10))
          Aval = concat(Aval, Array(Cat(1)))
          Tval = concat(Tval, Array(Cat(2)))
          time = concat(time, Array(t))

        }

      }
      /** ******** Limites sur C, A, T : ***********/
      var Cmax = Array(box_MaxC.value.toDouble)
      println(Cmax.length)
      Cmax = Cmax.padTo(t_max, box_MaxC.value.toDouble)


      var Cmin = Array(box_MaxC.value.toDouble)
      println(Cmin.length)
      Cmin = Cmin.padTo(t_max, box_MinC.value.toDouble)


      var Amax = Array(box_MaxC.value.toDouble)
      println(Amax.length)
      Amax = Amax.padTo(t_max, box_MaxA.value.toDouble)


      var Amin = Array(box_MaxC.value.toDouble)
      println(Amin.length)
      Amin = Amin.padTo(t_max, box_MinA.value.toDouble)


      var Tmax = Array(box_MaxC.value.toDouble)
      println(Tmax.length)
      Tmax = Tmax.padTo(t_max, box_MaxT.value.toDouble)


      var Tmin = Array(box_MaxC.value.toDouble)
      println(Tmin.length)
      Tmin = Tmin.padTo(t_max, box_MinT.value.toDouble)


      /** ******** Plot limites sur C, A, T : ***********/
      val dataCmax = data
        .x(time.toJSArray)
        .y(Cmax.toJSArray)
        .set(plotlymarker.size(0.8).set(plotlycolor.rgb(249, 149, 128)))
        .name("Cmax")

      val dataCmin = data
        .x(time.toJSArray)
        .y(Cmin.toJSArray)
        .set(plotlymarker.size(0.8).set(plotlycolor.rgb(249, 149, 128)))
        .name("Cmin")

      val dataAmax = data
        .x(time.toJSArray)
        .y(Amax.toJSArray)
        .set(plotlymarker.size(0.8).set(plotlycolor.rgb(128, 170, 249)))
        .name("Amax")

      val dataAmin = data
        .x(time.toJSArray)
        .y(Amin.toJSArray)
        .set(plotlymarker.size(0.8).set(plotlycolor.rgb(128, 170, 249)))
        .name("Amin")

      val dataTmax = data
        .x(time.toJSArray)
        .y(Tmax.toJSArray)
        .set(plotlymarker.size(0.8).set(plotlycolor.rgb(200, 235, 89)))
        .name("Tmax")

      val dataTmin = data
        .x(time.toJSArray)
        .y(Tmin.toJSArray)
        .set(plotlymarker.size(0.8).set(plotlycolor.rgb(200, 235, 89)))
        .name("Tmin")

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

      // val config = Config.displayModeBar(false)

      val plot = Plotly.newPlot(plotDiv,
        js.Array(dataTmin, dataTmax, dataAmin, dataAmax, dataCmin, dataCmax, data1, data2, data3),
        layout)

    }


    /** *************** Noyau *********************/

    val kernelStatus: Var[KernelStatus] = Var(KernelStatus.NOT_COMPUTED_YED)

    val addButtonVideOrNot = button("Show Kernel").render

    lazy val addButtonCalc = button("Compute Kernel",
      onclick := { () =>
        kernelStatus() = KernelStatus.COMPUTING_KERNEL

        Console.print("********************* Eps ***************")

        if (!check_Eps.checked) {
          box_MaxEps.value = "0"
          box_MinEps.value = "0"
          println("********************* Eps ***************")
        }
        if (!check_zeta.checked) {
          box_MaxZeta.value = "0"
          box_MinZeta.value = "0"
          println("********************* Zeta ***************")
        }

        Post[Api].CalcKernel(KernelParameters(box_MaxC.value.toDouble, box_MinC.value.toDouble, box_MaxA.value.toDouble, box_MinA.value.toDouble,
          box_MaxT.value.toDouble, box_MinT.value.toDouble, box_l.value.toDouble, box_g.value.toDouble, box_M.value.toInt,
          box_c.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
          box_phi.value.toDouble, box_phi.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_h.value.toDouble,
          box_mp.value.toDouble, box_mt.value.toDouble, box_MaxEps.value.toDouble, box_MinEps.value.toDouble, box_MaxZeta.value.toDouble,
          box_MinZeta.value.toDouble)).call().foreach { kr: KernelResult =>
          kernelStatus() = KernelStatus.computedKernel(kr)

        }
        println("********************* PARAM ***************")
        println(box_MaxEps.value)
        println(box_MaxZeta.value)
      }


    )

    val onoff = Var(false)

    // affichage du noyau :

    val KernelDiv0 = div.render
    val KernelDiv1 = div.render
    val KernelDiv2 = div.render
    val KernelDiv3 = div.render

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

          /*      val layoutKernel = Layout
                  .title("My Kernel")
                  .showlegend(true)
                  .yaxis(plotlyaxis.title("Tourists"))
                  .xaxis(plotlyaxis.title("Turtles"))
                  .shapes(myKernel) */

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

    lazy val show2DKernelbutton = button("Show 2D Kernel from file",
      onclick := { () =>
        val file = document.getElementById("file_input").asInstanceOf[HTMLInputElement].files.item(0)

        var reader = new FileReader()
        reader.onload = (_: UIEvent) => {
          val text = s"${reader.result}"
          val content = document.getElementById("content")

          val layoutKernel = Layout //  1 : case Animaux Touristes
            .title("My Kernel")
            .showlegend(true)
            .yaxis(plotlyaxis.title("Tourists"))
            .xaxis(plotlyaxis.title("Turtles"))
            .shapes(File2shapes.FileToshapes(text, 0))

          /*      val layoutKernel = Layout
                  .title("My Kernel")
                  .showlegend(true)
                  .yaxis(plotlyaxis.title("Tourists"))
                  .xaxis(plotlyaxis.title("Turtles"))
                  .shapes(myKernel) */

          val dataKernel = data
            .x((Array(1.5, 4.5)).toJSArray)
            .y((Array(0.75, 0.75)).toJSArray)
            .text("Kernel")

          val plotshapes = Plotly.newPlot(KernelDiv0, js.Array(dataKernel), layoutKernel)
        }

        reader.readAsText(file)


      })





    //"/Users/laetitiazaleski/Desktop"
    //dom.File


    //val myKernel = File2shapes.FileToshapes()

    /*
        val rect = js.Dynamic.literal(
          `type` = "rect",
          x0 = 3,
          y0 = 1,
          x1 = 6,
          y1 = 2,
          fillcolor = "rgba(128, 0, 128, 0.7)").asInstanceOf[Shape]

      val layoutKernel = Layout
          .title("My Kernel")
          .showlegend(true)
          .yaxis(plotlyaxis.title("Tourists"))
          .xaxis(plotlyaxis.title("Turtles"))
          .shapes(scalajs.js.Array(rect))

      /*      val layoutKernel = Layout
              .title("My Kernel")
              .showlegend(true)
              .yaxis(plotlyaxis.title("Tourists"))
              .xaxis(plotlyaxis.title("Turtles"))
              .shapes(myKernel) */

            val dataKernel = data
              .x((Array(1.5, 4.5)).toJSArray)
              .y((Array(0.75, 0.75)).toJSArray)
              .text("Kernel")

            val plotshapes = Plotly.newPlot(KernelDiv, js.Array(dataKernel), layoutKernel)
    */

    /* HTML tags : */

    val equationVisible = Var(false)

    val sliderValue = Var("None")
    val myDiv = input(marginTop := 200).render
    org.scalajs.dom.document.body.appendChild(
      span(
        myDiv,
        span(Rx {
          sliderValue()
        },
          paddingLeft := 10
        )
      ))

    val options = SliderOptions
      .max(100)
      .min(0.0)
      .value(14.0)
      .tooltip(SliderOptions.ALWAYS)

    val slider = new Slider(myDiv, options._result)
    slider.on(Slider.CHANGE, () => {
      sliderValue() = slider.getValue.toString
    })


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
        div(
          button("About the equations", btn_primary, onclick := { ()=> equationVisible.update(!equationVisible.now)} ),
          equationVisible.expand(panel("C represents the fishermen's capital (boats, income...)," +
            " A represents the number of Animals (turtles) in the parc, T represents the number of tourists in the parc")(width := 400))),
        p(
          buttonIcon("About the equations", btn_primary).expandOnclick(panel("C represents the fishermen's capital (boats, income...)," +
            " A represents the number of Animals (turtles) in the parc, T represents the number of tourists in the parc")(width := 400))),
        p(
          buttonIcon("Scheme", btn_primary).expandOnclick(img(src := "img/CAT_Schemat.png")(width := 400))),
        p(
          buttonIcon("Equation", btn_primary).expandOnclick(img(src := "img/CAT_Equation.png")(width := 400))),


      /*   img(src := "img/CAT_Schemat.png"),

        div(
           buttonIcon("Equation", btn_primary ).expandOnclick(panel("C represents the fishermen's capital (boats, income...), A represents the number of Animals (turtles) in the parc, T represents the number of tourists in the parc")(width := 400)),
           onoff.expand(div(backgroundColor := "pink", onoff.now.toString), button("Set/Unset", onclick := {() => onoff() = !onoff.now}, btn_danger))),

         img(src := "img/CAT_Equation.png"), */
      h2("  Step 1 : Set up the parameters :"),
      h3(
        " Here you can change the parameters :"
      ),
      p("l: is the depreciation of the fishing infrastructures, from [1]"),
      div(box_l),
      p("g: is the growth rate of the turtle population, from [2]"),
      div(box_g),
      p("M: is the maximum capacity of the environment, from [3]"),
      div(box_M),
      /*  p("h:"),
        div(box_h),
        p("c:"),
        div(box_c), */
      p("p: is the deaths caused by traditionnal fishing, from [4]"),
      div(box_p),
      p("a: is the attractiveness associated with high number of turtles"),
      div(box_a),
      p("e: is the attractiveness associated with high quality of fishermen’s infrastructures"),
      div(box_e),
      p("eta: Is the dammages caused by a tourist on the environment, from [5]"),
      div(box_eta),
      p("phi: is the half saturation constant, namely the number of turtles at which tourist satisfaction is half maximum"),
      div(box_phi),
      /* p("d:"),
       div(box_d),*/
      p("delta: is the fishermen's infrastructures depreciation rate, from [6]"),
      div(box_delta),
      p("mp: is the price for the number of fishes caught for each turtle caught, from [7]"),
      div(box_mp),
      p("mt : is the price paid by each tourists that goes to the fisherman's infrastrures, from [8]"),
      div(box_mt),

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
        " Here you can hoose the initial conditions:"
      ),

      p("Number of Animal at the begining of the simulation :"),
      div(box_Ainit),
      p("Amount of the Fishermen's infrasctructures at the begining of the simulation :"),
      div(box_Cinit),
      p("Number of Tourists at the begining of the simulation :"),
      div(box_Tinit),


      h3("Choose the limits on C, A and T: "),
      div(p("Maximum on fisherman's capital :",
        box_MaxC,
        "   Minimum on fisherman's capital :",
        box_MinC)),
      div(p("Maximum on the number of animals :",
        box_MaxA,
        "   Minimum on the number of animals :",
        box_MinA)),
      div(p("Maximum on the number of tourists :",
        box_MaxT,
        "   Minimum on the number of tourists :",
        box_MinT)),
      div(buttonShowConstraints),


      h3(
        " Here you can plot :"
      ),

      div(buttonPlot),

      h2("Capital, number of Animals and number of tourists in function of time for the above parameters (no controls):"),
      div(plotDiv.render),

      h2("Step 1 :Choose the controls: "),
      div(p(check_Eps,
        "I want to clean the beaches from",
        box_MaxEps,
        "% of the time, to ",
        box_MinEps,
        "% of the time")),
      div(p(check_zeta,
        "I want to have the possibility to close from",
        box_MaxZeta,
        "% of the parc, to ",
        box_MinZeta,
        "% of the parc")),

      h2("Step 3 Compute your viability kernel: "),
      div(addButtonCalc),
      p(
        Rx {
          kernelStatus() match {
            case kr@(KernelStatus.NOT_COMPUTED_YED | KernelStatus.COMPUTING_KERNEL) =>
              kr.message
            case ks: KernelStatus =>
              ks.kernelResult match {
                case None => "Weird case ..."
                case Some(kr: KernelResult) =>

                  // val kr = kernelStatus.now
                  s"${ks.message} :: eps max : ${box_MaxEps.value} " + {
                    if (kr.isResultEmpty) "Your Kernel is empty, please try again by changing your controls and/or your constraints."
                    else s"Congratulation, your Kernel is not empty ! ${kr.resultPath}"
                  }
              }
          }
        }
      ),
      div(input(`type` := "file", id := "file_input").render),
      div(p("If your kernel is empty, here are sevral sets of control and constraints you can try :")),
      div(p("1000<C<100 000, 200<A<10000, 2000<T<20000, every controls possible:")),
      div(buttonTryThis),
      div(p("0<C<100000, 5000<A<100000, 0<T<10000, only restoring the environment:")),
      div(buttonTryThis2),

      div(showKernelbutton),
      div(show2DKernelbutton),
      // pre(id := "content"),
      div(KernelDiv0.render),
      div(KernelDiv1.render),
      div(KernelDiv2.render),
      div(KernelDiv3.render)
      //  div(addButtonVideOrNot),

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
