package domain

import play.api.libs.json.Json

case class Movie(originalName: String)

object Movie {
  implicit val movieReads = Json.reads[Movie]
}