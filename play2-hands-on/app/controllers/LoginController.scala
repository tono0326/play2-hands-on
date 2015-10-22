package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation._

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n.{MessagesApi, I18nSupport}
import javax.inject.Inject

import scala.concurrent.Future

class LoginController @Inject()(val messagesApi: MessagesApi) extends Controller 
    with I18nSupport {

  import LoginController._ // コンパニオンオブジェクトに定義したFormを参照
  
  // implicit rsはアクションの処理の中でHTTPリクエストやDBのセッションを暗黙的に使用するために必要になる記述
  def index = Action.async { implicit rs =>
    // val form = loginForm
    // Ok(views.html.board.login(loginForm))
    // form.flatMap { form => Ok(views.html.board.login(form))}
    // val form = loginForm.fill(LoginForm("test@test.com", "hogehoge"))
    // val form = Future { loginForm }
    // val result = Ok(views.html.board.login(loginForm))
    // Future(result)
    Future(loginForm).map(form => Ok(views.html.board.login(form)))
  }
  
  // ログイン処理
  def login = TODO

}

object LoginController {
  case class LoginForm(email: String, password: String)
  
  val halfWidthAlphaNum = Constraints.pattern("[a-zA-Z0-9]+".r)
  
  var loginForm = Form(
      mapping(
          "email" -> email,
          "password" -> nonEmptyText.verifying(halfWidthAlphaNum)
      )(LoginForm.apply)(LoginForm.unapply)
  )
}