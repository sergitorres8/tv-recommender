package services

import domain.errors.TvShowError
import domain.{TvShow, TvShowResponse}
import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FeatureSpec, Matchers}
import play.api.libs.ws.ahc.AhcWSResponse
import play.api.libs.ws.ahc.cache.{CacheableHttpResponseBodyPart, CacheableHttpResponseStatus}
import play.api.libs.ws.{WSClient, WSRequest}
import play.mvc.Http.Status._
import play.shaded.ahc.org.asynchttpclient.Response
import play.shaded.ahc.org.asynchttpclient.uri.Uri
import scalaz.{-\/, \/-}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

class TvShowServiceTest extends FeatureSpec with BeforeAndAfter with MockitoSugar with Matchers {
  val tvShowResponse =
    """{
        "movie_results": [],
        "person_results": [],
        "tv_results": [
        {
          "original_name": "Game of Thrones",
          "id": 1399,
          "name": "Game of Thrones",
          "vote_count": 5302,
          "vote_average": 8.2,
          "first_air_date": "2011-04-17",
          "poster_path": "/gwPSoYUHAKmdyVywgLpKKA4BjRr.jpg",
          "genre_ids": [
          18,
          10759,
          10765
          ],
          "original_language": "en",
          "backdrop_path": "/gX8SYlnL9ZznfZwEH4KJUePBFUM.jpg",
          "overview": "Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.",
          "origin_country": [
          "US"
          ],
          "popularity": 176.441
        }
        ],
        "tv_episode_results": [],
        "tv_season_results": []
      }""".stripMargin

  private var mockClient: WSClient = _
  private var tvShowService: TvShowService = _
  private var mockRequest: WSRequest = _
  private var IMDbID: String = _
  private implicit val executionContext: ExecutionContext = ExecutionContext.global
  private var url: String = _

  before {
    IMDbID = "some-imdb-id"
    url = "https://api.themoviedb.org/3/find/" + IMDbID
    mockRequest = mock[WSRequest]
    mockClient = mock[WSClient]
    when(mockClient.url(url)).thenReturn(mockRequest)
    setupMockRequest
    setupMockResponse(OK, tvShowResponse)
    tvShowService = new TvShowService(mockClient)
  }

  feature("MovieDb request generation") {
    scenario("should create request with movieDB url") {
      val expectedUrl = "https://api.themoviedb.org/3/find/some-imdb-id"

      tvShowService.getTvShowFor(url)

      verify(mockClient).url(expectedUrl)
    }

    scenario("should add the Api key to the request header") {
      tvShowService.getTvShowFor(url)

      verify(mockRequest).withHttpHeaders(("api_key", "some-key"))
    }

    scenario("should request for a specific imdb id and language param") {
      tvShowService.getTvShowFor(url)

      verify(mockRequest).withQueryStringParameters(("language", "en-US"), ("external_source", "imdb_id"))
    }
  }

  feature("MovieDb response") {
    scenario("should request the MovieDb api") {
      tvShowService.getTvShowFor(url)

      verify(mockRequest).get()
    }

    scenario("should parse /discover 200 response for Imdb id") {
      setupMockResponse(OK, tvShowResponse)

      val response = tvShowService.getTvShowFor(url)

      getResult(response) shouldBe \/-(getTvShowResponse())
    }

    scenario("should not get lead data when fail to get bluewhale response") {
      setupMockResponse(BAD_REQUEST, tvShowResponse)

      val response = tvShowService.getTvShowFor(url)

      getResult(response) shouldBe -\/(TvShowError())
    }
  }

  private def setupMockResponse(statusCode: Int, response: String) = {
    val wSResponse: AhcWSResponse = new AhcWSResponse(
      new Response.ResponseBuilder()
        .accumulate(new CacheableHttpResponseStatus(Uri.create("https://uri"), statusCode, "status text", "protocols!"))
        .accumulate(new CacheableHttpResponseBodyPart(response.getBytes(), true))
        .build())

    when(mockRequest.get).thenReturn(Future(wSResponse))
  }

  private def setupMockRequest = {
    when(mockRequest.withHttpHeaders(("api_key", "some-key"))).thenReturn(mockRequest)
    when(mockRequest.withQueryStringParameters(("language", "en-US"), ("external_source", "imdb_id"))).thenReturn(mockRequest)
  }

  private def getResult[T](response: Future[T]): T = {
    Await.result(response, 2.seconds)
  }

  private def getTvShowResponse(): TvShowResponse = {
    TvShowResponse(List(), List(), List(TvShow(
      "Game of Thrones",
      1399,
      "Game of Thrones",
      5302,
      8.2,
      "2011-04-17",
      "/gwPSoYUHAKmdyVywgLpKKA4BjRr.jpg",
      List(18, 10759, 10765),
      "en",
      "/gX8SYlnL9ZznfZwEH4KJUePBFUM.jpg",
      "Seven noble families fight for control of the mythical land of Westeros. Friction between the houses leads to full-scale war. All while a very ancient evil awakens in the farthest north. Amidst the war, a neglected military order of misfits, the Night's Watch, is all that stands between the realms of men and icy horrors beyond.",
      List("US"),
      176.441)),
      List(),
      List()
    )
  }
}
