package domain

import play.api.libs.json.{JsPath, Json, Reads}
import play.api.libs.functional.syntax._

case class TvShow(originalName: String,
                  id: Int,
                  name: String,
                  voteCount: Int,
                  voteAverage: Double,
                  firstAirDate: String,
                  posterPath: String,
                  genreIds: List[Int],
                  originalLanguage: String,
                  backdropPath: String,
                  overview: String,
                  originalCountry: List[String],
                  popularity: Double)

object TvShow {
  implicit val tvShowReads: Reads[TvShow] = (
    (JsPath \ "original_name").read[String] and
      (JsPath \ "id").read[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "vote_count").read[Int] and
      (JsPath \ "vote_average").read[Double] and
      (JsPath \ "first_air_date").read[String] and
      (JsPath \ "poster_path").read[String] and
      (JsPath \ "genre_ids").read[List[Int]] and
      (JsPath \ "original_language").read[String] and
      (JsPath \ "backdrop_path").read[String] and
      (JsPath \ "overview").read[String] and
      (JsPath \ "origin_country").read[List[String]] and
      (JsPath \ "popularity").read[Double]
    )(TvShow.apply _)
}