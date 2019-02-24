package services

import org.mockito.Mockito.{verify, when}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfter, FeatureSpec}
import play.api.libs.ws.{WSClient, WSRequest}

import scala.concurrent.ExecutionContext

class TvShowServiceTest extends FeatureSpec with BeforeAndAfter with MockitoSugar {
  private var mockClient: WSClient = _
  private var tvShowService: TvShowService = _
  private var mockRequest: WSRequest = _
  private var IMDbID: String = _
  private var url: String = _
  private implicit val executionContext: ExecutionContext = ExecutionContext.global

  before {
    IMDbID = "some-imdb-id"
    url = "https://api.themoviedb.org/3/find/" + IMDbID
    mockRequest = mock[WSRequest]
    mockClient = mock[WSClient]
    when(mockClient.url(url)).thenReturn(mockRequest)
    setupMockRequest
    tvShowService = new TvShowService(mockClient)
  }


  feature("The movie db request generation") {
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


  private def setupMockRequest = {
    when(mockRequest.withHttpHeaders(("api_key", "some-key"))).thenReturn(mockRequest)
    when(mockRequest.withQueryStringParameters(("language", "en-US"), ("external_source", "imdb_id"))).thenReturn(mockRequest)
  }
}
