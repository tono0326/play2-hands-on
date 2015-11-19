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

class BoardController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                                val messagesApi: MessagesApi) extends Controller 
    with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

  import BoardController._ // コンパニオンオブジェクトに定義したFormを参照
  
  // implicit rsはアクションの処理の中でHTTPリクエストやDBのセッションを暗黙的に使用するために必要になる記述
  def index = Action.async { implicit rs =>
    // ログイン済みかチェック
    // https://www.playframework.com/documentation/2.4.x/ScalaSessionFlash#Reading-a-Session-value
    rs.session.get("connected").map { email =>
      // ログイン済みの場合は掲示板に転送
      Future(Redirect(routes.BoardController.thread))
    }.getOrElse {
      // ログイン画面に転送
      val message = None
      Future(loginForm).map(form => Ok(views.html.board.login(form, message)))
    }
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
              // セッションにログイン情報を記録してリダイレクト
              Redirect(routes.BoardController.index).withSession(
                  rs.session + ("connected" -> users.head.email)
              )
            }
          }
        }
    )
  }
  
  // ログアウト処理
  def logout = Action.async { implicit rs =>
    Future(Redirect(routes.BoardController.index).withSession(
        rs.session - "connected"
    ))
  }
  
  // スレッド処理
  def thread = Action.async { implicit rs => 
    // ログイン済みかチェック
    rs.session.get("connected").map { email =>
      // ログイン済みの場合
      Future(Ok("ログイン中 : " + email))
    }.getOrElse {
      // ログイン画面に転送
      Future(Redirect(routes.BoardController.index))
    }
  }
}

object BoardController {
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