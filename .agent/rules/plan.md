---
trigger: model_decision
description: Project: Minecraft Market Board MOD (FF14 Style)
---

# 1. プロジェクト概要
FF14のマーケットボードをモチーフとした、プレイヤー間取引および通貨システムを追加するMinecraft MOD。

# 2. 技術スタック (Technical Stack)
- **Minecraft Version**: 1.21.1
- **Platform**: NeoForge 21.1.217+
- **Language**: Java 21
- **Storage**:
    - 通貨: Data Attachments (NeoForge API)
    - マーケットデータ: World Saved Data (NBT)
- **Communication**: NeoForge Payload API (1.21 standard)

# 3. 実装済み仕様 (Implemented Specifications)

## A. 通貨システム (Currency System)
- **基本仕様**: プレイヤーごとに独自の通貨「G」を保持。
- **データ構造**: \MoneyAttachment\ (Integer) をプレイヤーエンティティにアタッチ。
- **獲得ロジック**:
    - \PlayerXpEvent.PickupXp\ をフックし、獲得XPと同量を自動加算。
    - コマンド \/balance [add/set]\ によるデバッグ操作が可能。

## B. マーケットシステム (Market System)
- **データ構造 (\MarketListing\)**:
    - ID (UUID), 商品 (ItemStack), 価格 (int), 出品者 (UUID/Name), 登録日時。
- **サーバー側管理 (\MarketSavedData\)**:
    - OverworldのWorld Dataとして保存。
    - \pendingPayments\: オフライン販売対応用の未受取金マップ (出品者がログインすると自動振替)。
- **ネットワーク通信**:
    - \AddListingPayload\: 出品 (C->S)
    - \BuyItemPayload\: 購入 (C->S)
    - \SyncListingsPayload\: 出品リスト同期 (S->C)

## C. UI/UX (User Interface)
- **マーケットボード (\MarketBoardScreen\)**:
    - **一覧表示**: \MarketListingList\ (Scrollable) による出品アイテムのリスト表示。
    - **Sell Mode**:
        - メインハンドまたはカーソルのアイテムを認識。
        - 独自のオーバーレイUIで価格を入力して出品。
    - **購入確認**:
        - アイテムクリック時に独自オーバーレイを表示。
        - 誤操作防止の確認プロセス。
    - **閉じるボタン**: 右上の標準的なXボタン。

# 4. 実装フェーズ (Status)

- [x] **Phase 1: 初期化** (Setup, Gradle)
- [x] **Phase 2: 通貨システム** (Attachment, Event, Command)
- [x] **Phase 3: データ管理** (Listing Record, SavedData)
- [x] **Phase 4: UI基盤** (Menu, Screen, Rendering)
- [x] **Phase 5: ネットワーク** (Payloads, Handlers)
- [x] **Phase 7: 出品UI** (Sell Button, Input Overlay)
- [x] **Phase 8: オフライン対応** (Pending Payments)
- [x] **Phase 9: UI改善** (Confirm Overlay, Close Button, Layout Fixes)
- [x] **Phase 10: スクロールリスト** (MarketListingList)

# 5. 今後の拡張 (Future)
- [ ] データベース連携 (SQLite)
- [ ] カテゴリ検索
- [ ] キーワード検索
- [ ] 売上履歴の表示
