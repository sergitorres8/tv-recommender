package domain

import play.api.libs.json.Json

case class TvSeason(originalName: String)

object TvSeason {
  implicit val tvSeasonReads = Json.reads[TvSeason]
}