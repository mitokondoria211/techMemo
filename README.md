# TechMemo Backend

TechMemoのバックエンドAPIです。
フロントエンドリポジトリ: [techmemo-frontend](フロントのGitHubのURL)

## 使用技術

- Java 17
- Spring Boot 3.x
- Spring Security（JWT認証 / RSA方式）
- Spring Data JPA
- MapStruct
- PostgreSQL

## 主な機能

- ユーザー登録・ログイン（JWT認証）
- 記事の投稿・編集・削除・公開/非公開切り替え
- タグ・カテゴリによる記事の絞り込み検索
- 参考URLの記事への紐付け管理
- いいね機能
- ブックマーク管理

## 設計上の工夫

- JWT認証にRSA方式を採用
  - フロントとバックを分離した構成のためトークン認証を採用
  - HMAC方式と比較して秘密鍵の管理範囲を最小化できるためRSA方式を選択
- アクセストークンとリフレッシュトークンを分離
  - アクセストークンの有効期限を短く設定しリスクを低減
  - リフレッシュトークンはHttpOnly CookieにセットしXSSから保護
- GlobalExceptionHandlerで例外を一元管理
  - RFC 9457準拠のProblemDetail形式でエラーレスポンスを統一
- 記事更新時のURL全件入れ替え設計
  - orphanRemoval=trueを活用しシンプルな実装を実現
- タグのfindOrCreate実装
  - 同時登録時の競合をDataIntegrityViolationExceptionでハンドリング

## 開発背景

最初はThymeleafを使ったサーバーサイドレンダリングで開発しましたが、
フロントとバックエンドを分離した構成の方が責務が明確になると判断し、
React + REST APIの構成に移行しました。
分離するにあたりセッション管理ではなくJWT認証を採用しました。

## セットアップ

### 必要な環境
- Java 21以上
- PostgreSQL 14以上

### 1. RSA鍵ペアの生成

JWT認証にRSA方式を使用しているため、鍵ペアを生成して配置する必要があります。

```bash
mkdir -p src/main/resources/keys

# 秘密鍵の生成
openssl genrsa -out src/main/resources/keys/private_key.pem 2048

# 公開鍵の抽出
openssl rsa -in src/main/resources/keys/private_key.pem \
  -pubout -out src/main/resources/keys/public_key.pem
```

### 2. 環境変数の設定

`.env` ファイルをプロジェクトルートに作成するか、シェルにエクスポートしてください。

```bash
DB_URL=jdbc:postgresql://localhost:5432/techmemo
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=604800000
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

| 変数名 | 説明 | 例 |
|---|---|---|
| `DB_URL` | PostgreSQL接続URL | `jdbc:postgresql://localhost:5432/techmemo` |
| `DB_USERNAME` | DBユーザー名 | `postgres` |
| `DB_PASSWORD` | DBパスワード | `password` |
| `JWT_EXPIRATION` | アクセストークン有効期限（ミリ秒） | `3600000`（1時間） |
| `JWT_REFRESH_EXPIRATION` | リフレッシュトークン有効期限（ミリ秒） | `604800000`（7日間） |
| `CORS_ALLOWED_ORIGINS` | CORSで許可するオリジン | `http://localhost:3000` |

### 3. データベースの準備

```sql
CREATE DATABASE techmemo;
```

### 4. 起動

devプロファイルを指定して起動します。devプロファイルでは起動時にテーブルの再作成とダミーデータの投入が行われます。

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

環境変数をコマンドラインで渡す場合:

```bash
DB_URL=jdbc:postgresql://localhost:5432/techmemo \
DB_USERNAME=postgres \
DB_PASSWORD=password \
JWT_EXPIRATION=3600000 \
JWT_REFRESH_EXPIRATION=604800000 \
CORS_ALLOWED_ORIGINS=http://localhost:3000 \
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## API仕様

Swagger UIで確認できます。
ローカル起動後: http://localhost:8080/swagger-ui.html
