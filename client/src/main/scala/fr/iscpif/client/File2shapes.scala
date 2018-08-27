package fr.iscpif.client

import com.definitelyscala.plotlyjs.Shape
import org.scalajs.dom
import org.scalajs.dom._

import scala.concurrent._
import scala.language.implicitConversions
import scala.scalajs.js




object File2shapes {

  def readTextFile(fileToRead: dom.File): Future[Either[DOMError, String]] = {

    // Used to create the Future containing either the file content or an error
    val promisedErrorOrContent = Promise[Either[DOMError, String]]

    val reader = new FileReader()
    reader.readAsText(fileToRead, "UTF-8")

    reader.onload = (_: UIEvent) => {
      val resultAsString = s"${reader.result}"
      promisedErrorOrContent.success(Right(resultAsString))
    }
    reader.onerror = (_: Event) => promisedErrorOrContent.success(Left(reader.error))

    promisedErrorOrContent.future
  }
/*
  def printFileContent(file: dom.File) =
    readTextFile(file).map {
      case Right(fileContent) => println(s"File content: $fileContent")
      case Left(error) => println(s"Could not read file ${file.name}. Error is: $error")
    }
*/

  def FileToshapes(fileContent: String, id: Int): js.Array[Shape] = {

    /*readTextFile(file).map {
      case Right(fileContent) => println(s"File content: $fileContent")
      case Left(error) => println(s"Could not read file ${file.name}. Error is: $error")
    }*/
    var x_0 = 0.0
    var x_1 = 0.0
    var y_0 = 0.0
    var y_1 = 0.0

    var color = "rgba(128, 0, 128, 0.5)" // case Animaux Touristes
    if (id == 2){
       color = "rgba(205, 92, 92, 0.5)" // case Capital Animaux
    }
    if (id == 3){
       color = "rgba(65, 209, 204, 0.5)" // case Capital Tourist
    }
    var line = "0"

    var rect = scalajs.js.Dynamic.literal(
      `type` = "rect",
      x0 = x_0,
      y0 = y_1,
      x1 = x_1,
      y1 = y_1,
      fillcolor = color
    ).asInstanceOf[Shape]


//    var shapes = ArrayBuffer(r
// ect)
    var shapes = js.Array(rect)
        val lines = fileContent.split("\n")
        for (lineString <- lines) {
          var line = lineString.split(" ")
          if (id == 1){ // case Animaux Touristes
            y_0 = line{7}.toDouble;
            x_0 = line{5}.toDouble;
            y_1 = line{8}.toDouble;
            x_1 = line{6}.toDouble;
          }
          if (id == 2){ // case Capital Animaux
            y_0 = line{3}.toDouble;
            x_0 = line{5}.toDouble;
            y_1 = line{4}.toDouble;
            x_1 = line{6}.toDouble;
          }
          if (id == 3){ // case Capital Tourist
            y_0 = line{3}.toDouble;
            x_0 = line{7}.toDouble;
            y_1 = line{4}.toDouble;
            x_1 = line{8}.toDouble;
          }
        rect = scalajs.js.Dynamic.literal(
          `type` = "rect",
          y0 = y_0,
          x0 = x_0,
          y1 = y_1,
          x1 = x_1,
          fillcolor = color
        ).asInstanceOf[Shape]
      shapes.push({rect})
    }

    shapes
  }

}

