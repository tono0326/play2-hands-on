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

// DBを使う
import play.api.db.slick._
import slick.driver.JdbcProfile
import models.Tables._
import slick.driver.H2Driver.api._

class LoginController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                                val messagesApi: MessagesApi) extends Controller 
    with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

  import LoginController._ // コンパニオンオブジェクトに定義したFormを参照
  
  // implicit rsはアクションの処理の中でHTTPリクエストやDBのセッションを暗黙的に使用するために必要になる記述
  def index = Action.async { implicit rs =>
    // val form = loginForm
    // Ok(views.html.board.login(loginForm))
    // form.flatMap { form => Ok(views.html.board.login(form))}
    // val form = loginForm.fill(LoginForm("test@test.com", "hogehoge"))
    
    // Futureのタイミングが違うパターン
    // val form = Future { loginForm }
    // val result = Ok(views.html.board.login(loginForm))
    // Future(result)
    
    val message = None
    Future(loginForm).map(form => Ok(views.html.board.login(form, message)))
  }
  
  // ログイン処理
  def login = Action.async { implicit rs =>
    // リクエストの内容をバインド
    loginForm.bindFromRequest.fold(
        // エラーの場合は入力画面に戻す
        error => {
          val message = Some("ログイン失敗（バリデーションエラー）")
          Future(BadRequest(views.html.board.login(error, message)))
        },
        // 成功時
        form => {
          db.run(Users.filter(t => t.email === form.email && t.password === form.password).result).map { users =>
            if(users.isEmpty) {
              val message = Some("ログイン失敗：存在しないユーザーです " + form.email)
              BadRequest(views.html.board.login(loginForm, message))
            } else {
              val message = Some("ログイン成功：" + form.email)
              Ok(views.html.board.login(loginForm, message))
            }
          }
        }
    )
  }
}

object LoginController {
  case class LoginForm(email: String, password: String)
  
  var loginForm = Form(
      mapping(
          "email" -> email,
          // パスワードは半角英数字10文字以内
          "password" -> nonEmptyText.verifying(
              Constraints.pattern("[a-zA-Z0-9]+".r),
              Constraints.maxLength(10)
          )
      )(LoginForm.apply)(LoginForm.unapply)
  )
}