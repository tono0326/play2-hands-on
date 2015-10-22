This is your new Play application
=================================

This file will be packaged with your application, when using `activator dist`.

# 進め方
これを写経する

[play2-hands-on/README.md at master · bizreach/play2-hands-on](https://github.com/bizreach/play2-hands-on/blob/master/play2.4-slick3.0/markdown/README.md)

## つまったところ

`activator eclipse`をするとエラーになったので以下を参考に対応

[PlayFramework - activator eclipse で [error] Not a valid command: eclipse (similar: help, alias) の時の対処法 - Qiita](http://qiita.com/shogo807/items/e50b4538bb1964d4ee92)

## 動かし方

Play

```sh
$ cd ~/scala-play/play2-hands-on/play2-hands-on/
$ activator run
```

DB

```sh
$ cd ~/scala-play/play2-hands-on/slick-codegen/h2/
$ sh start.sh
```

h2-browser

[Developing-with-the-H2-Database](https://www.playframework.com/documentation/ja/2.3.x/Developing-with-the-H2-Database)

```sh
$ activator
h2-browser
```

# 課題
- 項目を増やす
    - メールアドレス
    - パスワード
- 複数の更新・削除ができる
- 削除のIDをDBチェック

# その他
ディレクトリ階層を増やした

```sh
$ find . -type d -d 1 ! -path "./.*" ! -path "./play2-hands-on" | sed 's!^.*/!!' | xargs -I % git mv ./% ./play2-hands-on/%
$ find . -type f -d 1 ! -path "./.*" | sed 's!^.*/!!' | xargs -I % git mv ./% ./play2-hands-on/%
```

# 参考資料
- [Scala - Playframeworkのフォームで良く使うバリデーション - Qiita](http://qiita.com/modal_soul/items/00da5105b29880fee590)
- [Scala ExecutionContextって何 / Futureはスレッド立ち上げじゃないよ - ましめも](http://mashi.hatenablog.com/entry/2014/11/24/010417)

# ログインフォームを作る
## 手順
- [x] DBにメールアドレスとパスワードを追加
- ログイン画面を作る
    - [ ] 初期画面
        - [ ] HTMLのパスワードのhelperをなんとかする `constraint.pattern`
    - [ ] エラー画面
    - [ ] 完了画面
## がんばったこと
- 余計なimport文を削除する
- パスワード入力





