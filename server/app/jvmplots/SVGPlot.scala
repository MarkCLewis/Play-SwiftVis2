package jvmplots

import scalafx.application.JFXApp
import swiftvis2.plotting.Plot
import swiftvis2.plotting.PlotSymbol
import swiftvis2.plotting.renderer.JVMSVGInterface
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import swiftvis2.plotting.NumericAxis

object SVGPlot extends App {
  println("Load Data")
  val data = jvmplots.CartAndRad.read(new java.io.File("data/CartAndRad.88840.bin")).take(6000000)
  println("Data Loaded " + data.size)

  val x = data.map(_(0))
  val y = data.map(_(1))
  val r = data.map(_(6) * 2)

  println("Start")
  val start = System.nanoTime()
  val plot = Plot.scatterPlot(x, y, "Sim", "Radial", "Azimuthal", r,
    xSizing = PlotSymbol.Sizing.Scaled, ySizing = PlotSymbol.Sizing.Scaled)
    .updatedAxis[NumericAxis]("x", axis => axis.copy(min = Some(-0.19), max = Some(-0.14), tickLabelInfo = axis.tickLabelInfo.map(_.copy(numberFormat = "%1.2f"))))
    .updatedAxis[NumericAxis]("y", axis => axis.copy(min = Some(100.77), max = Some(100.72), tickLabelInfo = axis.tickLabelInfo.map(_.copy(numberFormat = "%1.2f"))))
  JVMSVGInterface.apply(plot, "/home/mlewis/Downloads/svgPlot.svg", 1200, 1000)
  println((System.nanoTime() - start) * 1e-9)
}
