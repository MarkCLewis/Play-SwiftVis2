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
import scalajs.js

object ScalaJSExample {
  implicit val ec = scala.concurrent.ExecutionContext.global
  private var fullData = js.Array[IndexedSeq[Array[Double]]]()
  val canvas = document.getElementById("ring-plot").asInstanceOf[html.Canvas]

  def main(args: Array[String]): Unit = {
    //dom.window.setTimeout(() => getData(0, 10000), 1000)
    dom.window.setTimeout(() => plotFromTop(), 10000)
  }

  def getData(start: Int, count: Int): Unit = {
    Fetch.fetch(s"/ringParticles?start=$start&count=$count").toFuture.flatMap(res => res.text().toFuture).map { json =>
      Json.fromJson[IndexedSeq[Array[Double]]](Json.parse(json)) match {
        case JsSuccess(data, path) =>
        println(s"Got data $start, ${data.length}")
          fullData.push(data)
          val x = fullData.toSeq.flatMap(_.map(_(0)))
          val y = fullData.toSeq.flatMap(_.map(_(1)))
          val r = fullData.toSeq.flatMap(_.map(_(2) * 2))
          val plot = Plot.scatterPlot(x, y, "Sim", "Radial", "Azimuthal", r,
            xSizing = PlotSymbol.Sizing.Scaled, ySizing = PlotSymbol.Sizing.Scaled)
            .updatedAxis[NumericAxis]("x", axis => axis.copy(min = Some(-0.19), max = Some(-0.14), tickLabelInfo = axis.tickLabelInfo.map(_.copy(numberFormat = "%1.2f"))))
            .updatedAxis[NumericAxis]("y", axis => axis.copy(min = Some(100.77), max = Some(100.72), tickLabelInfo = axis.tickLabelInfo.map(_.copy(numberFormat = "%1.2f"))))
          JSRenderer(plot, canvas)
          if (data.length >= count) dom.window.setTimeout(() => getData(start + count, count), 1000)
        case e @ JsError(_) => println(e)
      }
    }
  }

  def plotFromTop(): Unit = {
    println(dom.window.asInstanceOf[js.Dynamic].topData.length)
    val td = dom.window.asInstanceOf[js.Dynamic].topData.asInstanceOf[js.Array[js.Array[Double]]]
    val x = td.toSeq.map(_(0))
    val y = td.toSeq.map(_(1))
    val r = td.toSeq.map(_(2) * 2)
    val plot = Plot.scatterPlot(x, y, "Sim", "Radial", "Azimuthal", r,
      xSizing = PlotSymbol.Sizing.Scaled, ySizing = PlotSymbol.Sizing.Scaled)
      .updatedAxis[NumericAxis]("x", axis => axis.copy(min = Some(-0.19), max = Some(-0.14), tickLabelInfo = axis.tickLabelInfo.map(_.copy(numberFormat = "%1.2f"))))
      .updatedAxis[NumericAxis]("y", axis => axis.copy(min = Some(100.77), max = Some(100.72), tickLabelInfo = axis.tickLabelInfo.map(_.copy(numberFormat = "%1.2f"))))
    JSRenderer(plot, canvas)
  }
}
