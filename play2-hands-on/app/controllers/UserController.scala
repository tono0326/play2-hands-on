package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.db.slick._
import slick.driver.JdbcProfile
import models.Tables._
import javax.inject.Inject
import scala.concurrent.Future
import slick.driver.H2Driver.api._

/**
 * @author cw_ono
 */
class UserController @Inject()(val dbConfigProvider: DatabaseConfigProvider,
                               val messagesApi: MessagesApi) extends Controller 
    with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {
  
  /**
   * 一覧表示
   */
  // implicit rsはアクションの処理の中でHTTPリクエストやDBのセッションを暗黙的に使用するために必要になる記述
  def list = Action.async { implicit rs =>
    // IDの昇順に全てのユーザー情報を取得(SELECT * FROM USERS ORDER BY ID)
    db.run(Users.sortBy(t => t.id).result).map { users =>
      // 一覧画面を表示
      Ok(views.html.user.list(users))
    }
  }
  
  /**
   * 編集画面表示
   */
  import UserController._ // コンパニオンオブジェクトに定義したFormを参照用
  def edit(id: Option[Long]) = Action.async { implicit rs =>
    // リクエストパラメータにIDが存在する場合
    val form = if(id.isDefined) {
      db.run(Users.filter(t => t.id === id.get.bind).result.head).map { user =>
        // 値をフォームに詰める
        userForm.fill(UserForm(Some(user.id), user.name, user.companyId, user.email, user.password))
      }
    } else {
      // リクエストパラメータにIDが存在しない場合
      Future { userForm }
    }
    
    form.flatMap { form =>
      // 会社一覧を取得
      // SELECT * FROM COMPANIES ORDER BY ID
      db.run(Companies.sortBy(_.id).result).map { companies =>
        Ok(views.html.user.edit(form, companies))
      }
    }
  }
  
  /**
   * 登録実行
   */
  def create = Action.async { implicit rs =>
    // リクエストの内容をバインド
    userForm.bindFromRequest.fold(
      // エラーの場合
      error => {
        db.run(Companies.sortBy(t => t.id).result).map { companies => 
          BadRequest(views.html.user.edit(error, companies))}
      },
      // OKの場合
      form => {
        // ユーザーを登録
        // INSERT INTO USERS (ID, NAME, COMPANY_ID, EMAIL, PASSWORD) VALUES (?, ?, ?, ?, ?)
        val user = UsersRow(0, form.name, form.companyId, form.email, form.password)
        db.run(Users += user).map { _ =>
          // 一覧画面へリダイレクト（タイプセーフに指定）
          Redirect(routes.UserController.list)
        }
      }
    )
  }
  
  /**
   * 更新実行
   */
  def update = Action.async { implicit rs =>
    // リクエストの内容をバインド
    userForm.bindFromRequest.fold(
      // エラーの場合は登録画面に戻す
      error => {
        db.run(Companies.sortBy(t => t.id).result).map { companies =>
          BadRequest(views.html.user.edit(error, companies))
        }
      },
      // OKの場合は登録を行い一覧画面にリダイレクトする
      form => {
        // ユーザー情報を更新
        // UPDATE USERS SET NAME = ?, COMPANY_ID = ?, EMAIL = ?, PASSWORD = ? WHERE ID = ?
        val user = UsersRow(form.id.get, form.name, form.companyId, form.email, form.password)
        db.run(Users.filter(t => t.id === user.id.bind).update(user)).map { _ =>
          // 一覧画面にリダイレクト（タイプセーフに指定）
          Redirect(routes.UserController.list)
        }
      }
    )
  }
  
  /**
   * 削除実行
   */
  def remove(id: Long) = Action.async {implicit rs =>
    // ユーザーを削除
    // DELETE FROM USERS WHERE ID = ?
    db.run(Users.filter(t => t.id === id.bind).delete).map { _ =>
      // 一覧画面へリダイレクト
      Redirect(routes.UserController.list)
    }
  }
}

object UserController {
  // フォームの値を格納するケースクラス 
  case class UserForm(id: Option[Long], name: String, companyId: Option[Int], email: String, password: String)
  
  val halfWidthAlphaNum = Constraints.pattern("[a-zA-Z0-9]+".r)
  
  // formから送信されたデータ ⇔ ケースクラスの変換を行う
  var userForm = Form(
      mapping(
        "id"        -> optional(longNumber),
        "name"      -> nonEmptyText(maxLength = 20),
        "companyId" -> optional(number),
        "email"     -> email,
        "password"  -> nonEmptyText.verifying(halfWidthAlphaNum)
      )(UserForm.apply)(UserForm.unapply)
  )
}
