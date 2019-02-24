package controllers

import javax.inject._
import play.api.mvc._
import services.TvShowService

@Singleton
class HomeController @Inject()(components: ControllerComponents, tvShowService: TvShowService) extends BaseController {
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index())
  }

  override protected def controllerComponents: ControllerComponents = components
}
