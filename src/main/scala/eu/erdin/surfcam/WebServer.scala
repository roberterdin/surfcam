package eu.erdin.surfcam

import java.io.File
import java.nio.file.{Files, Paths}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.ActorMaterializer

import scala.io.StdIn

/**
  * @author robert.erdin@gmail.com 
  *         created on 15/04/18.
  */
object WebServer {



  def imageDestination(fileInfo: FileInfo): File = {
    val path = Paths.get(s"${fileInfo.fileName}.jpg")
    Files.createFile(path).toFile
  }


  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route =
      path("image") {
        storeUploadedFile("image", imageDestination) {
          case (metadata, file) =>
            // do something with the file and file metadata ...
            println(metadata)
            complete(StatusCodes.OK)
        }
      }

    val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }



}
