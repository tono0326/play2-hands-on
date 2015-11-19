This is your new Play application
=================================

This file will be packaged with your application, when using `activator dist`.

# 進め方
## これを写経する

[play2-hands-on/README.md at master · bizreach/play2-hands-on](https://github.com/bizreach/play2-hands-on/blob/master/play2.4-slick3.0/markdown/README.md)

### つまったところ

`activator eclipse`をするとエラーになったので以下を参考に対応

[PlayFramework - activator eclipse で [error] Not a valid command: eclipse (similar: help, alias) の時の対処法 - Qiita](http://qiita.com/shogo807/items/e50b4538bb1964d4ee92)

### 動かし方

#### Play

```sh
$ cd ~/scala-play/play2-hands-on/play2-hands-on/
$ activator run
```

#### DB

```sh
$ cd ~/scala-play/play2-hands-on/slick-codegen/h2/
$ sh start.sh
```

h2-browser

[Developing-with-the-H2-Database](https://www.playframework.com/documentation/ja/2.3.x/Developing-with-the-H2-Database)

```sh
$ activator
$ h2-browser
```

※ h2を起動している状態で行うこと

## 課題をする
[課題](https://chatwork.atlassian.net/wiki/pages/viewpage.action?pageId=6422657)

1. ユーザー登録/取得
2. ユーザーがサービスにログイン
3. スレッド操作

# 参考資料
- [Scala - Playframeworkのフォームで良く使うバリデーション - Qiita](http://qiita.com/modal_soul/items/00da5105b29880fee590)
- [Scala ExecutionContextって何 / Futureはスレッド立ち上げじゃないよ - ましめも](http://mashi.hatenablog.com/entry/2014/11/24/010417)

## 後で読む

JavaGuide4
https://www.playframework.com/documentation/ja/2.2.x/JavaGuide4