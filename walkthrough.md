# 開発進捗: Minecraft Market Board MOD

ここまでの実装内容（Phase 1 ~ Phase 10）のまとめです。

## 実装済み機能一覧

### Phase 2: 通貨システム
- **機能**: 経験値を拾うと自動的に独自通貨「G」に変換されます。
- **データ**: `MoneyAttachment` (NeoForge Data Attachment) を使用してプレイヤーデータに保存。
- **コマンド**: `/balance` で残高確認・操作が可能。

### Phase 3: マーケットデータ管理
- **機能**: サーバー側で全出品データを一元管理。
- **データ**: `MarketSavedData` (World Saved Data) を使用し、サーバー再起動後も出品情報を保持。

### Phase 5: ネットワーク通信
- **機能**: クライアント(GUI)とサーバー間のデータ同期。
- **実装**: NeoForge Payload APIを使用。
    - `AddListingPayload`: 出品登録
    - `BuyItemPayload`: 購入処理
    - `SyncListingsPayload`: 出品リストの同期

### Phase 7: 出品機能 (Sell Mode)
- **機能**: GUI内の「Sell」ボタンからアイテムを出品可能。
- **UI**:
    - メインハンドまたはカーソルで持っているアイテムを認識。
    - 価格入力フィールド (`EditBox`) を実装。
    - 入力不備（価格未入力など）時のバリデーション実装。

### Phase 8: オフライン販売者対応
- **機能**: 出品者がオフラインの間に商品が売れた場合、売上金を「保留」として保存。
- **実装**:
    - `MarketSavedData` に `pendingPayments` マップを追加。
    - プレイヤーログイン時 (`PlayerLoggedInEvent`) に保留金を自動付与し、チャットで通知。

### Phase 9: UI改善 (Refinements)
- **機能**: ユーザー体験の向上。
- **UI**:
    - **閉じるボタン**: 画面右上に「X」ボタンを追加。
    - **購入確認画面**: 即時購入ではなく、確認オーバーレイを表示して誤操作を防止。
    - **レイアウト調整**: インベントリとの干渉を防ぐため、オーバーレイの描画レイヤー (Z-index) を調整。

### Phase 10: スクロールリスト (Scrollable List)
- **機能**: 多数の出品アイテムを表示するためのスクロール可能なリスト。
- **実装**:
    - `MarketListingList` (ObjectSelectionList継承) を新規作成。
    - `MarketBoardScreen` への組み込みと、以前の手動レンダリングの廃止。
    - アイテムクリック時の購入確認フローとの連携。

## 検証状況
- **ビルド**: `./gradlew build` 成功確認済み。
- **動作確認**:
    - 出品登録 (ハンドアイテム/カーソルアイテム) -> OK
    - 購入処理 (オンライン/オフライン) -> OK (ロジック実装済み)
    - UI表示 (確認画面、閉じるボタン) -> OK
    - リスト表示 (スクロール、クリック) -> OK
