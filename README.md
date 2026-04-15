# TechMemo Backend

TechMemoのバックエンドAPIです。
フロントエンドリポジトリ: [techmemo-frontend](フロントのGitHubのURL)

## 使用技術

- Java 21
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

## ER図

```mermaid
erDiagram
  users {
    uuid id PK
    string name
    string email
    enum role
    timestamp created_at
    timestamp updated_at
  }
  categories {
    bigint id PK
    string name
    timestamp created_at
    timestamp updated_at
  }
  tags {
    bigint id PK
    string name
    timestamp created_at
    timestamp updated_at
  }
  articles {
    bigint id PK
    string title
    text content
    boolean public_flag
    uuid user_id FK
    bigint category_id FK
    timestamp created_at
    timestamp updated_at
  }
  article_tags {
    bigint article_id FK
    bigint tag_id FK
  }
  urls {
    bigint id PK
    bigint article_id FK
    string url
    string title
    timestamp created_at
    timestamp updated_at
  }
  likes {
    bigint id PK
    uuid user_id FK
    bigint article_id FK
    timestamp created_at
  }
  bookmarks {
    bigint id PK
    uuid user_id FK
    string url
    string title
    string memo
    timestamp created_at
    timestamp updated_at
  }

  users ||--o{ articles : "writes"
  users ||--o{ likes : "likes"
  users ||--o{ bookmarks : "saves"
  categories ||--o{ articles : "categorizes"
  articles ||--o{ article_tags : "has"
  tags ||--o{ article_tags : "tagged by"
  articles ||--o{ urls : "has"
  articles ||--o{ likes : "receives"
```
## セットアップ

### 必要な環境
- Java 21以上
- PostgreSQL 14以上

### 環境変数
DB_URL=jdbc:postgresql://localhost:5432/techmemo
DB_USERNAME=your_username
DB_PASSWORD=your_password
JWT_EXPIRATION=3600000
JWT_REFRESH_EXPIRATION=604800000

### 起動
```bash
./mvnw spring-boot:run
```

起動時に開発用ダミーデータが自動で投入されます。

## API仕様

Swagger UIで確認できます。
ローカル起動後: http://localhost:8080/swagger-ui.html
