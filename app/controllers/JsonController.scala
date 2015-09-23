package controllers

import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.db.slick._
import slick.driver.JdbcProfile
import models.Tables._
import javax.inject.Inject
import scala.concurrent.Future
import slick.driver.H2Driver.api._

import play.api.libs.json._ // Play2のJSONサポート機能を使用するために必要なimport文です
import play.api.libs.functional.syntax._

// テンプレートを使用していないので国際化機能のために必要だったMessagesApiのDIやI18nSupportトレイトのミックスインは行っていません。
class JsonController @Inject()(val dbConfigProvider: DatabaseConfigProvider) extends Controller 
    with HasDatabaseConfigProvider[JdbcProfile]{
    
  /**
   * 一覧表示
   */
  def list = TODO
  
  /**
   * ユーザー登録
   */
  def create = TODO
  
  /**
   * ユーザー更新
   */
  def update = TODO
  
  /**
   * ユーザー削除
   */
  def remove(id: Long) = TODO
}