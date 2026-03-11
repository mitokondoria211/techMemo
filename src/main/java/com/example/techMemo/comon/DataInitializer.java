package com.example.techMemo.comon;

import com.example.techMemo.article.ArticleRepository;
import com.example.techMemo.article.entity.Article;
import com.example.techMemo.category.Category;
import com.example.techMemo.category.CategoryRepository;
import com.example.techMemo.tag.Tag;
import com.example.techMemo.tag.TagRepository;
import com.example.techMemo.url.Url;
import com.example.techMemo.url.UrlRepository;
import com.example.techMemo.user.Role;
import com.example.techMemo.user.User;
import com.example.techMemo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dev")
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UrlRepository urlRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() > 0) {
            log.info("ダミーデータは既に登録済みです。スキップします。");
            return;
        }

        log.info("ダミーデータの登録を開始します。");

        insertUsers();
        insertCategories();
        insertTags();
        insertArticlesAndUrls();

        log.info("ダミーデータの登録が完了しました");
    }

    private void insertUsers() {
        userRepository.saveAll(List.of(
            User.builder().name("ミートソース").email("test1@test.com").password(passwordEncoder.encode("password")).role(Role.ADMIN).build(),
            User.builder().name("テスト").email("test2@test.com").password(passwordEncoder.encode("password")).role(Role.USER).build(),
            User.builder().name("田中太郎").email("test3@test.com").password(passwordEncoder.encode("password")).role(Role.USER).build()
        ));
        log.info("ユーザー登録完了");
    }

    private void insertCategories() {
        categoryRepository.saveAll(List.of(
            Category.builder().name("バックエンド").build(),
            Category.builder().name("フロントエンド").build(),
            Category.builder().name("インフラ").build(),
            Category.builder().name("データベース").build(),
            Category.builder().name("セキュリティ").build(),
            Category.builder().name("アーキテクチャ").build()
        ));
        log.info("カテゴリ登録完了");
    }

    private void insertTags() {
        tagRepository.saveAll(List.of(
            Tag.builder().name("Spring Boot").build(),
            Tag.builder().name("Java").build(),
            Tag.builder().name("React").build(),
            Tag.builder().name("TypeScript").build(),
            Tag.builder().name("Docker").build(),
            Tag.builder().name("AWS").build(),
            Tag.builder().name("JWT").build(),
            Tag.builder().name("PostgreSQL").build(),
            Tag.builder().name("Kubernetes").build(),
            Tag.builder().name("Redis").build(),
            Tag.builder().name("GraphQL").build(),
            Tag.builder().name("REST API").build()
        ));
        log.info("タグ登録完了");
    }

    private void insertArticlesAndUrls() {
        User user1 = userRepository.findByEmail("test1@test.com").orElseThrow();
        User user2 = userRepository.findByEmail("test2@test.com").orElseThrow();
        User user3 = userRepository.findByEmail("test3@test.com").orElseThrow();

        Category backend  = categoryRepository.findByName("バックエンド").orElseThrow();
        Category frontend = categoryRepository.findByName("フロントエンド").orElseThrow();
        Category infra    = categoryRepository.findByName("インフラ").orElseThrow();
        Category database = categoryRepository.findByName("データベース").orElseThrow();
        Category security = categoryRepository.findByName("セキュリティ").orElseThrow();
        Category arch     = categoryRepository.findByName("アーキテクチャ").orElseThrow();

        Tag springBoot  = tagRepository.findByName("Spring Boot").orElseThrow();
        Tag java        = tagRepository.findByName("Java").orElseThrow();
        Tag react       = tagRepository.findByName("React").orElseThrow();
        Tag typescript  = tagRepository.findByName("TypeScript").orElseThrow();
        Tag docker      = tagRepository.findByName("Docker").orElseThrow();
        Tag aws         = tagRepository.findByName("AWS").orElseThrow();
        Tag jwt         = tagRepository.findByName("JWT").orElseThrow();
        Tag postgresql  = tagRepository.findByName("PostgreSQL").orElseThrow();
        Tag kubernetes  = tagRepository.findByName("Kubernetes").orElseThrow();
        Tag redis       = tagRepository.findByName("Redis").orElseThrow();
        Tag graphql     = tagRepository.findByName("GraphQL").orElseThrow();
        Tag restApi     = tagRepository.findByName("REST API").orElseThrow();

        // ===== バックエンド系記事 (30件) =====
        articleRepository.saveAll(List.of(
            Article.builder().title("Spring BootでJWT認証を実装する").content("JWT認証の実装手順のメモです。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, java, jwt)).build(),
            Article.builder().title("Spring SecurityでOAuth2ログインを設定する").content("Google/GitHubログインの設定方法まとめ。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring BootのRESTful API設計ベストプラクティス").content("REST APIの設計方針とURLの命名規則について。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, restApi)).build(),
            Article.builder().title("Spring DataでページングとソートAPIを実装する").content("Pageableを使ったページネーション実装メモ。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring BootでExceptionHandlerを使う").content("グローバル例外ハンドリングの実装方法。").publicFlag(true).user(user2).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Javaのストリームで複雑なデータ変換をする").content("Stream APIの活用パターン集。").publicFlag(true).user(user1).category(backend).tags(List.of(java)).build(),
            Article.builder().title("Spring BootのBean Validationを使ったバリデーション").content("@Validatedと@Validの違いとカスタムアノテーションの作り方。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Javaのレコードクラスを活用する").content("Java 16+のrecordをDTOとして使う方法。").publicFlag(false).user(user2).category(backend).tags(List.of(java)).build(),
            Article.builder().title("Spring BootでRedisをキャッシュとして使う").content("@Cacheableアノテーションとredis連携のメモ。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, redis)).build(),
            Article.builder().title("Spring BootでGraphQL APIを実装する").content("spring-graphqlを使ったGraphQL実装手順。").publicFlag(true).user(user3).category(backend).tags(List.of(springBoot, graphql)).build(),
            Article.builder().title("Spring BootのFilterとInterceptorの違い").content("リクエスト処理の流れとFilterとInterceptorの使い分け。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("JavaのOptionalを正しく使う").content("Optionalのアンチパターンと正しい使い方。").publicFlag(true).user(user2).category(backend).tags(List.of(java)).build(),
            Article.builder().title("Spring BootでAOPを使ってロギングを実装する").content("@Aroundアドバイスでメソッドの実行時間を計測する方法。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring BootのScheduled実装").content("@Scheduledを使った定期バッチ処理の実装。").publicFlag(false).user(user3).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring BootでファイルアップロードAPIを作る").content("MultipartFileを使ったファイル受け取りとS3連携。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, aws)).build(),
            Article.builder().title("Javaでデザインパターン（Strategyパターン）を実装する").content("StrategyパターンをSpring Bootで活用する例。").publicFlag(true).user(user2).category(backend).tags(List.of(java)).build(),
            Article.builder().title("Spring BootのTestで@MockBeanを使う").content("単体テストと結合テストの書き方メモ。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring BootでWebSocket通信を実装する").content("STOMPを使ったリアルタイム通信の実装手順。").publicFlag(true).user(user3).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("JavaのEnumを活用したステータス管理").content("Enumにメソッドを持たせる設計パターン。").publicFlag(true).user(user1).category(backend).tags(List.of(java)).build(),
            Article.builder().title("Spring BootでメールをSMTP送信する").content("JavaMailSenderを使ったメール送信実装。").publicFlag(false).user(user2).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring BootのDI（依存性注入）を深く理解する").content("コンストラクタインジェクションとフィールドインジェクションの違い。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Javaの非同期処理 @Asyncを使う").content("@AsyncとCompletableFutureの組み合わせ方。").publicFlag(true).user(user3).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring Bootでマルチテナントを実装する").content("テナントIDによるデータ分離の設計と実装。").publicFlag(false).user(user1).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring BootでCSVをエクスポートするAPIを作る").content("OpenCSVを使ったCSV生成とダウンロード実装。").publicFlag(true).user(user2).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring BootのProfilesでの環境切り替え").content("dev/stg/prod環境をProfilesで切り替える方法。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot)).build(),
            Article.builder().title("Javaのジェネリクスを使ったユーティリティクラス設計").content("型安全なユーティリティを作るジェネリクス活用法。").publicFlag(true).user(user3).category(backend).tags(List.of(java)).build(),
            Article.builder().title("Spring BootでSwagger/OpenAPIドキュメントを生成する").content("springdoc-openapiを使ったAPIドキュメント自動生成。").publicFlag(true).user(user1).category(backend).tags(List.of(springBoot, restApi)).build(),
            Article.builder().title("Spring BootでSlack通知を送る").content("Webhookを使ったSlack通知実装。").publicFlag(true).user(user2).category(backend).tags(List.of(springBoot, java)).build(),
            Article.builder().title("Spring Boot + Redisでセッション管理する").content("Spring Session + Redisを使ったセッション共有設定。").publicFlag(false).user(user1).category(backend).tags(List.of(springBoot, redis)).build(),
            Article.builder().title("Spring BootでHTTPクライアント（WebClient）を使う").content("RestTemplateからWebClientへの移行メモ。").publicFlag(true).user(user3).category(backend).tags(List.of(springBoot, java)).build()
        ));

        // ===== フロントエンド系記事 (25件) =====
        articleRepository.saveAll(List.of(
            Article.builder().title("Reactでカスタムフックを作る").content("カスタムフックの使い方まとめ。").publicFlag(true).user(user1).category(frontend).tags(List.of(react)).build(),
            Article.builder().title("ReactのuseContextでグローバル状態管理する").content("useContext + useReducerでReduxなしの状態管理。").publicFlag(true).user(user1).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("TypeScriptの型定義ベストプラクティス").content("interfaceとtypeの使い分けとユーティリティ型の活用。").publicFlag(true).user(user2).category(frontend).tags(List.of(typescript)).build(),
            Article.builder().title("React Router v6の使い方まとめ").content("v6での変更点とネストされたルーティング実装。").publicFlag(true).user(user1).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("ReactでAPIフェッチをカスタムフック化する").content("useFetchフックを自作してAPI呼び出しを共通化する。").publicFlag(false).user(user3).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("ReactとSpring BootでCORS設定をする").content("開発環境と本番環境でのCORS設定の違い。").publicFlag(true).user(user1).category(frontend).tags(List.of(react, springBoot)).build(),
            Article.builder().title("TypeScriptのジェネリクスを使ったコンポーネント設計").content("汎用的なコンポーネントをジェネリクスで実装する方法。").publicFlag(true).user(user2).category(frontend).tags(List.of(typescript, react)).build(),
            Article.builder().title("ReactでJWT認証フローを実装する").content("ログイン→トークン保存→認証済みルートの実装。").publicFlag(true).user(user1).category(frontend).tags(List.of(react, jwt, typescript)).build(),
            Article.builder().title("Reactのレンダリング最適化（memo, useCallback, useMemo）").content("不要な再レンダリングを防ぐ最適化テクニック。").publicFlag(true).user(user3).category(frontend).tags(List.of(react)).build(),
            Article.builder().title("TailwindCSSとReactを組み合わせる").content("TailwindCSS v3のセットアップとコンポーネントへの適用。").publicFlag(true).user(user1).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("ReactでFormの状態管理をreact-hook-formで行う").content("バリデーション込みのフォーム実装パターン。").publicFlag(false).user(user2).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("TypeScriptでZodを使ったスキーマバリデーション").content("Zodを使ったランタイム型チェックとReact連携。").publicFlag(true).user(user1).category(frontend).tags(List.of(typescript)).build(),
            Article.builder().title("ReactでInfinite Scrollを実装する").content("Intersection Observer APIを使った無限スクロール。").publicFlag(true).user(user3).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("ViteでReact + TypeScript環境をセットアップする").content("CRAからViteへの移行と設定ファイルの解説。").publicFlag(true).user(user1).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("ReactでDrag & Dropを実装する").content("dnd-kitを使ったドラッグ&ドロップUIの実装。").publicFlag(false).user(user2).category(frontend).tags(List.of(react)).build(),
            Article.builder().title("Next.jsのApp Routerを使い始める").content("Pages RouterからApp Routerへの移行ガイド。").publicFlag(true).user(user1).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("ReactのエラーバウンダリでUIクラッシュをハンドリングする").content("ErrorBoundaryの実装とSentryとの連携。").publicFlag(true).user(user3).category(frontend).tags(List.of(react)).build(),
            Article.builder().title("TypeScriptのmapped typesを理解する").content("Partial, Required, ReadonlyなどのMapped Typesの仕組み。").publicFlag(true).user(user1).category(frontend).tags(List.of(typescript)).build(),
            Article.builder().title("ReactでMarkdownエディタを実装する").content("react-markdownとCodeMirrorを組み合わせた実装。").publicFlag(true).user(user2).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("Jestを使ったReactコンポーネントのテスト").content("Testing LibraryとJestでUIテストを書く方法。").publicFlag(false).user(user1).category(frontend).tags(List.of(react, typescript)).build(),
            Article.builder().title("ReactでPDFを表示するコンポーネントを作る").content("react-pdfを使ったPDFビューアの実装。").publicFlag(true).user(user3).category(frontend).tags(List.of(react)).build(),
            Article.builder().title("TypeScriptで型安全なAPIクライアントを生成する").content("OpenAPI GeneratorでAPIクライアントを自動生成する方法。").publicFlag(true).user(user1).category(frontend).tags(List.of(typescript, restApi)).build(),
            Article.builder().title("ReactでCSVダウンロード機能を実装する").content("Blobを使ったCSVファイルのダウンロード処理。").publicFlag(true).user(user2).category(frontend).tags(List.of(react)).build(),
            Article.builder().title("ReactのSuspenseとlazy loadingを使う").content("コード分割とSuspenseによるローディングUI実装。").publicFlag(true).user(user1).category(frontend).tags(List.of(react)).build(),
            Article.builder().title("ReactでダークモードをCSSカスタムプロパティで実装する").content("prefers-color-schemeとReact状態管理の組み合わせ。").publicFlag(false).user(user3).category(frontend).tags(List.of(react, typescript)).build()
        ));

        // ===== インフラ系記事 (20件) =====
        articleRepository.saveAll(List.of(
            Article.builder().title("DockerでPostgreSQL環境を構築する").content("Docker Composeの設定まとめ。").publicFlag(false).user(user2).category(infra).tags(List.of(docker, postgresql)).build(),
            Article.builder().title("Docker ComposeでSpring Boot + PostgreSQL環境を作る").content("開発環境をDocker Composeで一発起動する設定。").publicFlag(true).user(user1).category(infra).tags(List.of(docker, springBoot, postgresql)).build(),
            Article.builder().title("Kubernetesの基本概念を整理する").content("Pod/Service/Deploymentの関係をまとめた。").publicFlag(true).user(user2).category(infra).tags(List.of(kubernetes)).build(),
            Article.builder().title("AWS EC2にSpring Bootアプリをデプロイする").content("EC2セットアップからJarデプロイまでの手順。").publicFlag(true).user(user1).category(infra).tags(List.of(aws, springBoot)).build(),
            Article.builder().title("AWS RDSでPostgreSQLを使う").content("RDS作成からSpring Bootとの接続設定まで。").publicFlag(true).user(user3).category(infra).tags(List.of(aws, postgresql)).build(),
            Article.builder().title("DockerfileでSpring Bootアプリをコンテナ化する").content("マルチステージビルドを使った効率的なDockerfile。").publicFlag(true).user(user1).category(infra).tags(List.of(docker, springBoot)).build(),
            Article.builder().title("GitHub ActionsでCI/CDパイプラインを構築する").content("Spring BootアプリのビルドとECRプッシュを自動化する。").publicFlag(true).user(user2).category(infra).tags(List.of(aws, docker)).build(),
            Article.builder().title("AWS S3を使ったファイルストレージ実装").content("Spring BootからS3へのファイルアップロードとURL生成。").publicFlag(false).user(user1).category(infra).tags(List.of(aws, springBoot)).build(),
            Article.builder().title("Nginxをリバースプロキシとして設定する").content("Spring BootアプリをNginxの背後で動かす設定。").publicFlag(true).user(user3).category(infra).tags(List.of(docker)).build(),
            Article.builder().title("Kubernetesでローリングアップデートをする").content("Deploymentのローリングアップデート設定とロールバック手順。").publicFlag(true).user(user1).category(infra).tags(List.of(kubernetes)).build(),
            Article.builder().title("AWS CloudWatchでアプリのログを監視する").content("Spring BootのログをCloudWatch Logsへ転送する設定。").publicFlag(true).user(user2).category(infra).tags(List.of(aws)).build(),
            Article.builder().title("DockerネットワークとDNS解決の仕組み").content("コンテナ間通信とカスタムネットワークの設定。").publicFlag(false).user(user1).category(infra).tags(List.of(docker)).build(),
            Article.builder().title("Terraformを使ったAWSインフラのコード化").content("VPC/EC2/RDSをTerraformで定義する基本。").publicFlag(true).user(user3).category(infra).tags(List.of(aws)).build(),
            Article.builder().title("AWS ALBとターゲットグループの設定").content("ロードバランサーでSpring Bootを冗長化する手順。").publicFlag(true).user(user1).category(infra).tags(List.of(aws)).build(),
            Article.builder().title("DockerイメージをECRにプッシュする").content("ECRリポジトリ作成とdocker pushまでのコマンドまとめ。").publicFlag(true).user(user2).category(infra).tags(List.of(docker, aws)).build(),
            Article.builder().title("KubernetesのConfigMapとSecretを使う").content("環境変数の管理とSecretの安全な扱い方。").publicFlag(true).user(user1).category(infra).tags(List.of(kubernetes)).build(),
            Article.builder().title("Redisをキャッシュサーバーとして運用する").content("Redis設定チューニングとメモリ管理のポイント。").publicFlag(false).user(user3).category(infra).tags(List.of(redis)).build(),
            Article.builder().title("AWS ECSでコンテナをデプロイする").content("ECS Fargateを使ったサーバーレスコンテナ運用。").publicFlag(true).user(user1).category(infra).tags(List.of(aws, docker)).build(),
            Article.builder().title("Dockerのボリュームでデータを永続化する").content("named volumeとbind mountの使い分け。").publicFlag(true).user(user2).category(infra).tags(List.of(docker)).build(),
            Article.builder().title("Kubernetesのリソース制限（requests/limits）を設定する").content("CPUとメモリのリソース管理のベストプラクティス。").publicFlag(true).user(user1).category(infra).tags(List.of(kubernetes)).build()
        ));

        // ===== データベース系記事 (15件) =====
        articleRepository.saveAll(List.of(
            Article.builder().title("PostgreSQLのインデックス設計と最適化").content("B-treeインデックスとクエリプランの読み方。").publicFlag(true).user(user1).category(database).tags(List.of(postgresql)).build(),
            Article.builder().title("Spring Data JPAでN+1問題を解決する").content("fetchJoinとEntityGraphを使ったN+1対策。").publicFlag(true).user(user2).category(database).tags(List.of(springBoot, postgresql)).build(),
            Article.builder().title("PostgreSQLのトランザクション分離レベルを理解する").content("READ COMMITTED/REPEATABLE READの違いと使い所。").publicFlag(true).user(user1).category(database).tags(List.of(postgresql)).build(),
            Article.builder().title("FlywayでDBマイグレーションを管理する").content("Spring Bootと組み合わせたFlywayのセットアップ手順。").publicFlag(true).user(user3).category(database).tags(List.of(springBoot, postgresql)).build(),
            Article.builder().title("PostgreSQLのJSONB型を使う").content("JSONB型へのデータ格納とクエリ方法。").publicFlag(false).user(user1).category(database).tags(List.of(postgresql)).build(),
            Article.builder().title("JPAのエンティティ設計で避けるべきアンチパターン").content("双方向リレーションのLazy/Eager問題と循環参照対策。").publicFlag(true).user(user2).category(database).tags(List.of(springBoot, java)).build(),
            Article.builder().title("PostgreSQLのEXPLAIN ANALYZEでクエリを分析する").content("実行計画の読み方とスロークエリの改善方法。").publicFlag(true).user(user1).category(database).tags(List.of(postgresql)).build(),
            Article.builder().title("Spring BootでQueryDSLを使った型安全クエリ").content("QueryDSLのセットアップと動的クエリの実装。").publicFlag(true).user(user3).category(database).tags(List.of(springBoot, java, postgresql)).build(),
            Article.builder().title("PostgreSQLのバックアップとリストア手順").content("pg_dumpとpg_restoreを使ったバックアップ戦略。").publicFlag(false).user(user2).category(database).tags(List.of(postgresql)).build(),
            Article.builder().title("JPA楽観ロックで同時更新を防ぐ").content("@Versionアノテーションを使った楽観ロック実装。").publicFlag(true).user(user1).category(database).tags(List.of(springBoot, java)).build(),
            Article.builder().title("PostgreSQLの全文検索機能を使う").content("tsvectorとtsqueryを使った日本語全文検索の設定。").publicFlag(true).user(user3).category(database).tags(List.of(postgresql)).build(),
            Article.builder().title("Spring BootのJPAで論理削除を実装する").content("@SQLDeleteと@Whereを使ったソフトデリートの実装。").publicFlag(true).user(user1).category(database).tags(List.of(springBoot, java)).build(),
            Article.builder().title("PostgreSQLのパーティショニングを設定する").content("時系列データの範囲パーティショニング設定手順。").publicFlag(false).user(user2).category(database).tags(List.of(postgresql)).build(),
            Article.builder().title("データベース設計のER図をMermaidで書く").content("Mermaidを使ったER図記法とGitHubでの活用。").publicFlag(true).user(user1).category(database).tags(List.of(postgresql)).build(),
            Article.builder().title("Spring Bootでテスト用にH2インメモリDBを使う").content("H2のセットアップと本番DBとの差異への対処法。").publicFlag(true).user(user3).category(database).tags(List.of(springBoot, java)).build()
        ));

        // ===== セキュリティ・アーキテクチャ系記事 (10件) =====
        articleRepository.saveAll(List.of(
            Article.builder().title("Spring SecurityでRBACを実装する").content("ロールベースのアクセス制御の設計と実装。").publicFlag(true).user(user1).category(security).tags(List.of(springBoot, jwt)).build(),
            Article.builder().title("XSS/CSRFをSpring Bootで防ぐ").content("セキュリティヘッダーの設定とCSRFトークンの実装。").publicFlag(true).user(user2).category(security).tags(List.of(springBoot, java)).build(),
            Article.builder().title("SQLインジェクション対策のベストプラクティス").content("プリペアドステートメントとJPAの安全なクエリ実装。").publicFlag(true).user(user1).category(security).tags(List.of(springBoot, postgresql)).build(),
            Article.builder().title("JWTのアクセストークンとリフレッシュトークンを実装する").content("トークンローテーション戦略と実装パターン。").publicFlag(true).user(user3).category(security).tags(List.of(springBoot, jwt)).build(),
            Article.builder().title("Spring Bootでレートリミットを実装する").content("Bucket4jを使ったAPIレートリミットの実装。").publicFlag(false).user(user1).category(security).tags(List.of(springBoot, redis)).build(),
            Article.builder().title("マイクロサービスアーキテクチャの基本設計").content("サービス分割の考え方とAPI Gatewayパターン。").publicFlag(true).user(user1).category(arch).tags(List.of(springBoot, docker)).build(),
            Article.builder().title("DDDをSpring Bootで実践する").content("ドメイン駆動設計の集約・リポジトリ・ドメインサービスの実装例。").publicFlag(true).user(user2).category(arch).tags(List.of(springBoot, java)).build(),
            Article.builder().title("CQRSパターンをSpring Bootで実装する").content("コマンドとクエリを分離したAPIアーキテクチャの実装。").publicFlag(true).user(user1).category(arch).tags(List.of(springBoot, java)).build(),
            Article.builder().title("イベント駆動アーキテクチャ入門（Spring Events）").content("ApplicationEventとApplicationEventPublisherを使ったイベント実装。").publicFlag(false).user(user3).category(arch).tags(List.of(springBoot, java)).build(),
            Article.builder().title("クリーンアーキテクチャをJavaで実践する").content("ユースケース層・インターフェース層の分離と依存方向の制御。").publicFlag(true).user(user1).category(arch).tags(List.of(java, springBoot)).build()
        ));

        // ===== URL (代表的なもの) =====
        Article article1 = articleRepository.findAll().getFirst();
        Article article2 = articleRepository.findAll().get(2);
        Article article3 = articleRepository.findAll().get(3);
        urlRepository.saveAll(List.of(
            Url.builder().user(user1).article(article1).url("https://spring.io/docs").title("Spring公式ドキュメント").sortOrder(0).build(),
            Url.builder().user(user1).article(article1).url("https://qiita.com/example/jwt").title("Qiita JWT解説記事").sortOrder(1).build(),
            Url.builder().user(user1).article(article2).url("https://docs.docker.com").title("Docker公式ドキュメント").sortOrder(0).build(),
            Url.builder().user(user2).article(article3).url("https://kubernetes.io/docs").title("Kubernetes公式ドキュメント").sortOrder(0).build(),
            Url.builder().user(user1).article(article3).url("https://docs.aws.amazon.com").title("AWS公式ドキュメント").sortOrder(1).build()
        ));

        log.info("記事・URL登録完了");
    }
}
