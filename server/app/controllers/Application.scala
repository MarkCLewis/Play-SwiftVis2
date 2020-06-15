package controllers

import javax.inject._

import edu.trinity.videoquizreact.shared.SharedMessages
import play.api.mvc._
import play.api.libs.json.Json

@Singleton
class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  println("Load particles.")
  val particles = jvmplots.CartAndRad.read(new java.io.File("data/CartAndRad.88840.bin"))
  println("Count = " + particles.length)

  def index = Action {
    Ok(views.html.index(SharedMessages.itWorks))
  }

  def ringParticles(start: Int, count: Int) = Action {
    Ok(Json.toJson(particles.slice(start, start+count).map(p => Array(p(0), p(1), p(6)))))
  }

  def reactPlot = Action {
    Ok(views.html.reactPlot())
  }

}
