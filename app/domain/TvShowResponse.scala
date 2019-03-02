package domain

import play.api.libs.json.{JsPath, Json, Reads}
import play.api.libs.functional.syntax._

case class TvShowResponse(movies: Seq[Movie],
                          people: Seq[Person],
                          tvShows: Seq[TvShow],
                          tvEpisodes: Seq[TvEpisode],
                          tvSeasons: Seq[TvSeason]
                         )

object TvShowResponse {
  implicit val tvShowResponseReads: Reads[TvShowResponse] = (
    (JsPath \ "movie_results").read[Seq[Movie]] and
    (JsPath \ "person_results").read[Seq[Person]] and
    (JsPath \ "tv_results").read[Seq[TvShow]] and
    (JsPath \ "tv_episode_results").read[Seq[TvEpisode]] and
    (JsPath \ "tv_season_results").read[Seq[TvSeason]]
  )(TvShowResponse.apply _)
}