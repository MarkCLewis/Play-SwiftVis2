package playsv2

import edu.trinity.videoquizreact.shared.SharedMessages
import org.scalajs.dom
import dom.document
import org.scalajs.dom.html
import swiftvis2.plotting._
import swiftvis2.plotting.renderer.JSRenderer
import swiftvis2.plotting.styles._
import org.scalajs.dom.experimental.Fetch
import play.api.libs.json.Json
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError

object ScalaJSExample {
  implicit val ec = scala.concurrent.ExecutionContext.global

  def main(args: Array[String]): Unit = {
    val canvas = document.getElementById("ring-plot").asInstanceOf[html.Canvas]
    val renderer = new JSRenderer(canvas)
    Fetch.fetch("/ringParticles?start=0&count=10000").toFuture.flatMap(res => res.text().toFuture).map { json =>
      Json.fromJson[IndexedSeq[Array[Double]]](Json.parse(json)) match {
        case JsSuccess(data, path) =>
          val x = data.map(_(0))
          val y = data.map(_(1))
          val rad = data.map(_(6) * 2)
          val plot = Plot.simple(ScatterStyle(x, y, symbolWidth = rad, symbolHeight = rad, xSizing = PlotSymbol.Sizing.Scaled, ySizing = PlotSymbol.Sizing.Scaled), "Title")
          plot.render(renderer, Bounds(0, 0, canvas.width, canvas.height))
        case e @ JsError(_) => println(e)
      }
    }
  }
}
