# Minecraft Market Board MOD (FF14 Style)

## 概要
FF14のマーケットボードシステムをモチーフにしたMinecraft (NeoForge 1.21) 用のMODです。
プレイヤー間でアイテムの売買を行うためのシステムとGUIを提供します。

## プロジェクト状況
**現在のフェーズ**: Phase 10 (スクロールリスト実装: 完了)

### 実装済み機能

#### 1. 通貨システム
*   **MoneyAttachment**: プレイヤーごとの所持金を管理 (Data Attachments)。
*   **CurrencyEventHandler**: 経験値オーブ拾得時に通貨を自動加算。
*   **コマンド**: `/balance add/set` で所持金操作が可能（デバッグ用）。

#### 2. マーケットシステム (コア)
*   **MarketListing**: 出品情報のデータ構造。
*   **MarketSavedData**: サーバー側での出品データ永続化。
    *   **オフライン販売対応**: 出品者がオフラインの間に売れた場合、売上金は「保留中 (Pending Payment)」として保存されます。
    *   **自動受け取り**: 出品者がログインした際、保留中の売上金が自動的に支払われます。

#### 3. マーケットボード UI
*   **一覧表示**: `MarketListingList` (スクロール対応) により、多数の出品アイテムを快適に閲覧可能。
*   **出品モード (Sell Mode)**:
    *   「Sell」ボタンで出品画面へ切り替え。
    *   手に持っているアイテム、またはカーソルで掴んでいるアイテムを出品可能。
    *   「Enter Price」に入力した価格で出品登録。
*   **購入確認 (Confirm Purchase)**:
    *   商品クリック時に確認画面を表示し、誤購入を防止。
    *   「Confirm」で購入確定、「Cancel」でキャンセル。

### 技術仕様
*   **Minecraft**: 1.21.1
*   **Mod Loader**: NeoForge 21.1.217+
*   **Java**: 21
*   **通信**: NeoForge Payload API (`AddListingPayload`, `BuyItemPayload`, `SyncListingsPayload`)

## 今後の予定
*   **Phase 11**: データベース連携 (SQLite)
*   **Phase 12**: 検索・フィルタリング機能
