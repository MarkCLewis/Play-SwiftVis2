package jvmplots

import java.io.PrintStream
import java.io.FileOutputStream
import play.api.libs.json.Json

object SaveJSON extends App {
  val particles = jvmplots.CartAndRad.read(new java.io.File("data/CartAndRad.88840.bin"))
  val data = particles.map(p => Array(p(0), p(1), p(6)))
  val ps = new PrintStream(new FileOutputStream("server/public/CartAndRad.88840.js"))
  ps.println("console.log('Defining topData.');")
  ps.print("window.topData = ")
  ps.print(Json.toJson(data.take(6000000)))
  ps.println(";")
  ps.close()
}