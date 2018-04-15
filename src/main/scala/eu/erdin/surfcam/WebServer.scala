package eu.erdin.surfcam

import java.io.File
import java.nio.file.{Files, Paths}

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.directives.DebuggingDirectives

import scala.io.StdIn

/**
  * @author robert.erdin@gmail.com 
  *         created on 15/04/18.
  */
object WebServer extends App with Config{

  def imageDestination(fileInfo: FileInfo): File = {
    val path = Paths.get(s"$imageStore${fileInfo.fileName}")
    Files.createFile(path).toFile
  }

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  val route =
    path("image") {
      storeUploadedFile("image", imageDestination) {
        case (_, _) => {
          parameter("foo") { (foo) =>
            complete(StatusCodes.OK)
          }
        }
      }
    }

  val clientRouteLogged = DebuggingDirectives.logRequestResult("Client", Logging.InfoLevel)(route)
  val bindingFuture = Http().bindAndHandle(clientRouteLogged, "0.0.0.0", 8080)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
}
