package fr.iscpif.client

import java.nio.ByteBuffer

import CAT_RK4._
import boopickle.Default._
import org.scalajs.dom

import scala.Array.concat
import scala.concurrent.Future
import scala.scalajs.js.annotation.JSExportTopLevel
import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js.typedarray.{ArrayBuffer, TypedArrayBuffer}
import com.definitelyscala.plotlyjs.PlotData
import org.scalajs.dom.html

import scala.scalajs
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

import scala.scalajs.js.JSConverters._
import com.definitelyscala.plotlyjs._
import com.definitelyscala.plotlyjs.all._
import com.definitelyscala.plotlyjs.PlotlyImplicits._
import com.definitelyscala.plotlyjs.plotlyConts._
import org.querki.jsext.JSOptionBuilder
import org.scalajs.dom
import org.scalajs.dom.raw._
import autowire._
import shared.Api
import shared.Data._
import scaladget.tools.JsRxTags._

import Array._
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global
import boopickle.Default._

import scala.util.{Failure, Success}
import rx._

object Client {


  @JSExportTopLevel("run")
  def run() {
    implicit val ctx: Ctx.Owner = Ctx.Owner.safe()
    lazy val rng = scala.util.Random

    def randomDoubles(nb: Int = 100, ratio: Int = 1000) = Seq.fill(nb)(rng.nextDouble * ratio).toJSArray

    def randomInts(nb: Int = 100, ratio: Int = 1000) = Seq.fill(nb)(rng.nextInt(ratio)).toJSArray


    /* Definition des parametres */

    val box_zeta = input(
      `type` := "text",
      value := "0.01"
    ).render

    box_zeta.onkeyup = (e: dom.Event) => println(box_zeta.value)


    val output_zeta = box_zeta.value

    val box_l = input(
      `type` := "text",
      value := "0.1"
    ).render

    /*  val output_l = span.render

      box_zeta.onkeyup = (e: dom.Event) => {
        output_l.textContent =
          box_l.value
      }
*/
    val box_g = input(
      `type` := "text",
      value := "2.0"
    ).render

    val box_M = input(
      `type` := "text",
      value := "5000"
    ).render

    val box_h = input(
      `type` := "text",
      value := "0.00001"
    ).render

    val box_c = input(
      `type` := "text",
      value := "0.01"
    ).render

    val box_p = input(
      `type` := "text",
      value := "0.1"
    ).render

    val box_a = input(
      `type` := "text",
      value := "100.0"
    ).render

    val box_e = input(
      `type` := "text",
      value := "0.01"
    ).render

    val box_eta = input(
      `type` := "text",
      value := "0.00005"
    ).render

    val box_phi = input(
      `type` := "text",
      value := "1.0"
    ).render

    val box_d = input(
      `type` := "text",
      value := "1.0"
    ).render

    val box_delta = input(
      `type` := "text",
      value := "0.01"
    ).render

    val box_mp = input(
      `type` := "text",
      value := "15.0"
    ).render

    val box_mt = input(
      `type` := "text",
      value := "0.2"
    ).render

    //  controles :

    val check_Eps = input(
      `type` := "checkbox",
      value := "checkbox"
    ).render

    val check_zeta = input(
      `type` := "checkbox",
      value := "cmd"
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

    /* Calcul de CAT RK4 */

    // controles et valeur init:

    val eps = 100.0
    val zeta = 0.1
    val Cinit = 10000.0
    val Ainit = 5000.0
    val Tinit = 5000.0
    val t_max = 500

    var Cval = Array(Cinit / 10)
    var Aval = Array(Ainit)
    var Tval = Array(Tinit)
    var time = Array(0)
    // calcul:
    for (t <- 1 to t_max) {

      val Cat = compute_CAT_RK4(zeta, box_l.value.toDouble, box_g.value.toDouble, box_M.value.toDouble, box_h.value.toDouble,
        box_c.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
        eps, box_phi.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_mp.value.toDouble,
        box_mt.value.toDouble, Cval.last * 10, Aval.last, Tval.last)

      Cval = concat(Cval, Array(Cat(0) / 10))
      Aval = concat(Aval, Array(Cat(1)))
      Tval = concat(Tval, Array(Cat(2)))
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


    // bouton pour rafraichir le graphe :

    val addButton = button("Plot").render


    addButton.onclick = (e: dom.Event) => {
      var Cval = Array(Cinit)
      var Aval = Array(Ainit)
      var Tval = Array(Tinit)
      var time = Array(0)
      // calcul:
      for (t <- 1 to t_max) {

        val Cat = compute_CAT_RK4(zeta, box_l.value.toDouble, box_g.value.toDouble, box_M.value.toDouble, box_h.value.toDouble,
          box_c.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
          eps, box_phi.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_mp.value.toDouble,
          box_mt.value.toDouble, Cval.last * 10, Aval.last, Tval.last)

        Cval = concat(Cval, Array(Cat(0) / 10))
        Aval = concat(Aval, Array(Cat(1)))
        Tval = concat(Tval, Array(Cat(2)))
        time = concat(time, Array(t))

      }

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
    }


    val kernelStatus: Var[KernelStatus] = Var(KernelStatus.NOT_COMPUTED_YED)

    val addButtonVideOrNot = button("Show Kernel").render
    lazy val addButtonCalc = button("Compute Kernel",
      onclick := { () =>
        kernelStatus() = KernelStatus.COMPUTING_KERNEL
        Post[Api].CalcKernel(box_MaxC.value.toDouble, box_MinC.value.toDouble, box_MaxA.value.toDouble, box_MinA.value.toDouble,
          box_MaxT.value.toDouble, box_MinT.value.toDouble, box_l.value.toDouble, box_g.value.toDouble, box_M.value.toInt,
          box_c.value.toDouble, box_p.value.toDouble, box_a.value.toDouble, box_e.value.toDouble, box_eta.value.toDouble,
          box_phi.value.toDouble, box_phi.value.toDouble, box_d.value.toDouble, box_delta.value.toDouble, box_h.value.toDouble,
          box_mp.value.toDouble, box_mt.value.toDouble).call().foreach { kr: KernelResult =>
          kernelStatus() = KernelStatus.computedKernel(kr)
        }
      }
    )


    //    addButtonVideOrNot.onclick = (e: dom.MouseEvent) => {
    //
    //       Post[Api].VideOrnot("results/resparc2DBWithControlD10_TRYWeb.txt").call()
    //
    //         .onComplete {
    //         case Success(b) =>
    //          if(b){
    //            affichage = "Your Kernel is empty, please try again by changing your controls and/or your constraints."
    //          }else{
    //            affichage = "Congratulation, your Kernel is not empty !"
    //          }
    //         case Failure(t) =>
    //           affichage = "Could not process file"
    //      }


    // }


    /* HTML tags : */

    dom.document.body.appendChild(
      div(width := "100%", height := "100%")(
        h1("Step 2:"),
        h2("Equation : "),
        img(src := "img/CAT_Schemat.png"),
        p("C represents the fishermen's capital (boats, income...)"),
        p("A represents the number of Animals (turtles) in the parc "),
        p("T represents the number of tourists in the parc"),
        img(src := "img/CAT_Equation.png"),
        h2("Set up the parameters:"),
        p(
          "Here you can change the parameters:"
        ),

        p("l:"),
        div(box_l),
        p("g:"),
        div(box_g),
        p("M:"),
        div(box_M),
        p("h:"),
        div(box_h),
        p("c:"),
        div(box_c),
        p("p:"),
        div(box_p),
        p("a:"),
        div(box_a),
        p("e:"),
        div(box_e),
        p("eta:"),
        div(box_eta),
        p("phi:"),
        div(box_phi),
        p("d:"),
        div(box_d),
        p("delta:"),
        div(box_delta),
        p("mp:"),
        div(box_mp),
        p("mt :"),
        div(box_mt),
        p(" "),
        div(addButton),
        h2("Capital, number of Animals and number of tourists in function of time for the above parameters (no controls):"),
        div(plotDiv.render),
        h2("Choose the controls: "),
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

        h2("Choose the limits on C, A and T: "),
        p("Maximum on fisherman's capital :"),
        div(box_MaxC),
        p("Minimum on fisherman's capital :"),
        div(box_MinC),
        p("Maximum on the number of animals :"),
        div(box_MaxA),
        p("Minimum on the number of animals :"),
        div(box_MinA),
        p("Maximum on the number of tourists :"),
        div(box_MaxT),
        p("Minimum on the number of tourists :"),
        div(box_MinT),
        div(addButtonCalc),
        div(addButtonVideOrNot),
        p(
           Rx{
          kernelStatus() match {
            case kr@(KernelStatus.NOT_COMPUTED_YED | KernelStatus.COMPUTING_KERNEL) =>
              kr.message
            case kr: KernelStatus =>
             // val kr = kernelStatus.now
              s"""
                    ${kr.message} :: ${
                if (kr.kernelResult.map {
                  _.isResultEmpty
                }.getOrElse(false))
                  "Your Kernel is empty, please try again by changing your controls and/or your constraints."
                else
                  "Congratulation, your Kernel is not empty !"
              }
                    """
          }
        }
        )

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
