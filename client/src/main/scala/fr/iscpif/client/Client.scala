package fr.iscpif.client

import java.nio.ByteBuffer

import fr.iscpif.client.CAT_RK4._
import fr.iscpif.client.SlidersUtils._
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

    /*** Sliders ***/
    /* ordre : box_g.value.toDouble, box_M.value.toDouble, box_h.value.toDouble,
        box_alpha.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
        eps, box_phi.value.toDouble, box_phi2.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_mp.value.toDouble,
        box_mt.value.toDouble, box_ip.value.toDouble, box_it.value.toDouble */
    val slidersParam = Array(/*g : */ Array(0.5, 0.0, 0.208, 0.001), /* l: */Array(0.001, 0.0, 0.00052, 0.00001), /*p*/Array(0.03, 0.0, 0.0136, 0.0001),
      /*zeta controle*/  /*M*/ Array(50000.0, 10000.0, 36000.0, 1000.0), /*eta*/ Array( 0.001,0.0, 0.0008, 0.0001),
      /*eps controle*/   /*delta :*/ Array(0.1, 0.0, 0.01, 0.001),/*mp*/ Array(100.0, 0.0, 14.0, 1.0), /*ip*/ Array(1.0, 0.0, 0.01, 0.01),
      /*mt*/ Array(100.0, 0.0, 25.0, 1.0), /*it*/  Array(1.0, 0.0, 0.01, 0.01),  /*alpha*/ Array(2.0, 0.0, 0.9, 0.1), /*a*/ Array(15000.0, 5000.0, 10000.0, 0.1),
      /*phi*/Array(3000.0,1000.0 , 1833.0, 10.0), /*e*/ Array(100.0,0.0 , 50.0, 10.0), /*phi2*/ Array(50000.0,0.0 , 10000.0, 1000.0) )
    var sliders : List[Slider] = List()
    var foos : List[scalatags.JsDom.Modifier] = List()


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

    /* Definition des parametres */

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
    var t_max = 10000


    var Cval = Array(Cinit / 100)
    var Aval = Array(Ainit)
    var Tval = Array(Tinit)
    var time = Array(0)
    // calcul:
    for (t <- 1 to t_max) {

      val Catinit = compute_CAT_RK4(zeta, sliders(1).getValue().toString.toDouble , sliders(0).getValue().toString.toDouble, sliders(3).getValue().toString.toDouble, box_h.value.toDouble,
        sliders(10).getValue().toString.toDouble, sliders(2).getValue().toString.toDouble, sliders(11).getValue().toString.toDouble, sliders(13).getValue().toString.toDouble, sliders(4).getValue().toString.toDouble,
        eps, sliders(12).getValue().toString.toDouble, sliders(14).getValue().toString.toDouble, box_d.value.toDouble, sliders(5).getValue().toString.toDouble, sliders(6).getValue().toString.toDouble,
        sliders(8).getValue().toString.toDouble, sliders(7).getValue().toString.toDouble, sliders(9).getValue().toString.toDouble, Cval.last * 100, Aval.last, Tval.last)

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
      val Tc = 100 // temps t crise

      var Cat = compute_CAT_RK4(zeta, sliders(1).getValue().toString.toDouble , sliders(0).getValue().toString.toDouble, sliders(3).getValue().toString.toDouble, box_h.value.toDouble,
        sliders(10).getValue().toString.toDouble, sliders(2).getValue().toString.toDouble, sliders(11).getValue().toString.toDouble, sliders(13).getValue().toString.toDouble, sliders(4).getValue().toString.toDouble,
        eps, sliders(12).getValue().toString.toDouble, sliders(14).getValue().toString.toDouble, box_d.value.toDouble, sliders(5).getValue().toString.toDouble, sliders(6).getValue().toString.toDouble,
        sliders(8).getValue().toString.toDouble, sliders(7).getValue().toString.toDouble, sliders(9).getValue().toString.toDouble, Cinit, Ainit, Tinit)


      //  println(s"zeta $zeta")

      /*  println(s"zeta $zeta, l : ${box_l.value.toDouble}, g: ${box_g.value.toDouble}, M: ${box_M.value.toDouble}, h: ${box_h.value.toDouble}"+
          s"alpha: ${box_alpha.value.toDouble}, p: ${box_p.value.toDouble}, a: ${box_a.value.toDouble},e: ${box_e.value.toDouble}, eta: ${box_eta.value.toDouble}"+
          s"e: ${box_e.value.toDouble}, phi: ${box_phi.value.toDouble}, phi2: ${box_phi2.value.toDouble}, d: ${box_d.value.toDouble}, delta: ${box_delta.value.toDouble}, mp: ${box_mp.value.toDouble}"+
          s"mt: ${box_mt.value.toDouble},ip: ${box_ip.value.toDouble},it: ${box_it.value.toDouble}, Cinit: ${Cinit} , Ainit: ${Ainit}, Tinit: ${Tinit}")
  */
      // calcul:
      for (t <- 1 to t_max) {

        Cat = compute_CAT_RK4(zeta, sliders(1).getValue().toString.toDouble , sliders(0).getValue().toString.toDouble, sliders(3).getValue().toString.toDouble, box_h.value.toDouble,
          sliders(10).getValue().toString.toDouble, sliders(2).getValue().toString.toDouble, sliders(11).getValue().toString.toDouble, sliders(13).getValue().toString.toDouble, sliders(4).getValue().toString.toDouble,
          eps, sliders(12).getValue().toString.toDouble, sliders(14).getValue().toString.toDouble, box_d.value.toDouble, sliders(5).getValue().toString.toDouble, sliders(6).getValue().toString.toDouble,
          sliders(8).getValue().toString.toDouble, sliders(7).getValue().toString.toDouble, sliders(9).getValue().toString.toDouble, Cval.last, Aval.last, Tval.last)

        //       println(s" C: ${Cat(0)} , A: ${Cat(1)}, T: ${Cat(2)}")

        if ((t == Tc) && (check_crise.checked)) {
          println("crisis")
          Cval = concat(Cval, Array(Cat(0)))
          Aval = concat(Aval, Array(Cat(1) / coefAc))
          Tval = concat(Tval, Array(Cat(2)))
          time = concat(time, Array(t))

          /* Cat = compute_CAT_RK4(zeta, box_l.value.toDouble, box_g.value.toDouble, box_M.value.toDouble, box_h.value.toDouble,
            box_c.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
            eps, box_phi.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_mp.value.toDouble,
            box_mt.value.toDouble, Cval.last * 10, Aval.last / coefAc, Tval.last) */

        } else {

          Cval = concat(Cval, Array(Cat(0)))
          Aval = concat(Aval, Array(Cat(1)))
          Tval = concat(Tval, Array(Cat(2)))
          time = concat(time, Array(t))

        }

      }
      /** ******** Limites sur C, A, T : ***********/
      var Cmax = Array(box_MaxC.value.toDouble)
      Cmax = Cmax.padTo(t_max, box_MaxC.value.toDouble)


      var Cmin = Array(box_MinC.value.toDouble)
      Cmin = Cmin.padTo(t_max, box_MinC.value.toDouble)


      var Amax = Array(box_MaxA.value.toDouble)
      Amax = Amax.padTo(t_max, box_MaxA.value.toDouble)


      var Amin = Array(box_MinA.value.toDouble)
      Amin = Amin.padTo(t_max, box_MinA.value.toDouble)


      var Tmax = Array(box_MaxT.value.toDouble)

      Tmax = Tmax.padTo(t_max, box_MaxT.value.toDouble)


      var Tmin = Array(box_MinT.value.toDouble)

      Tmin = Tmin.padTo(t_max, box_MinT.value.toDouble)

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

      /** ******** Plot limites sur C, A, T : ***********/


      val dataCmax = data
        .x(time.toJSArray)
        .y(Cmax.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(249, 149, 128)))
        .name("Cmax")

      val dataCmin = data
        .x(time.toJSArray)
        .y(Cmin.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(249, 149, 128)))
        .name("Cmin")

      val dataAmax = data
        .x(time.toJSArray)
        .y(Amax.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(128, 170, 249)))
        .name("Amax")

      val dataAmin = data
        .x(time.toJSArray)
        .y(Amin.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(128, 170, 249)))
        .name("Amin")

      val dataTmax = data
        .x(time.toJSArray)
        .y(Tmax.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(200, 235, 89)))
        .name("Tmax")

      val dataTmin = data
        .x(time.toJSArray)
        .y(Tmin.toJSArray)
        .set(plotlymarker.size(0.5).set(plotlycolor.rgb(200, 235, 89)))
        .name("Tmin")



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
          box_MaxT.value.toDouble, box_MinT.value.toDouble, sliders(1).getValue().toString.toDouble, sliders(0).getValue().toString.toDouble, sliders(3).getValue().toString.toInt,
          box_c.value.toDouble, sliders(2).getValue().toString.toDouble, sliders(11).getValue().toString.toDouble, sliders(13).getValue().toString.toDouble, sliders(4).getValue().toString.toDouble,
          sliders(12).getValue().toString.toDouble, sliders(14).getValue().toString.toDouble, box_d.value.toDouble, sliders(5).getValue().toString.toDouble, box_h.value.toDouble,
          sliders(6).getValue().toString.toDouble, sliders(8).getValue().toString.toDouble, box_MaxEps.value.toDouble, box_MinEps.value.toDouble, box_MaxZeta.value.toDouble,
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


          val dataKernel = data
            .x((Array(1.5, 4.5)).toJSArray)
            .y((Array(0.75, 0.75)).toJSArray)
            .text("Kernel")

          val plotshapes = Plotly.newPlot(KernelDiv0, js.Array(dataKernel), layoutKernel)
        }

        reader.readAsText(file)


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


        /*   img(src := "img/CAT_Schemat.png"),
          div(
             buttonIcon("Equation", btn_primary ).expandOnclick(panel("C represents the fishermen's capital (boats, income...), A represents the number of Animals (turtles) in the parc, T represents the number of tourists in the parc")(width := 400)),
             onoff.expand(div(backgroundColor := "pink", onoff.now.toString), button("Set/Unset", onclick := {() => onoff() = !onoff.now}, btn_danger))),
           img(src := "img/CAT_Equation.png"), */
        h2("  Step 1 : Set up the parameters :"),
        h3(
          " Here you can change the parameters :"
        ),
        /*  p(
            button("Parameters", btn_primary, onclick := { () => paramVisible.update(!paramVisible.now) }),
            paramVisible.expand(div(box_g).render)),*/
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
        /*  p("h:"),
          div(box_h),
          p("c:"),
          div(box_c), */

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