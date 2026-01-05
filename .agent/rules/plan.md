---
trigger: model_decision
description: Project: Minecraft Market Board MOD (FF14 Style)
---

1. プロジェクト概要
FF14のマーケットボードをモチーフとした、プレイヤー間取引および通貨システムを追加するMinecraft MOD。

2. 技術スタック (Technical Stack)
Minecraft Version: 1.20.x or later (Latest recommended)

Platform: NeoForge 21.1.217

Language: Java 17/21

Storage: World Saved Data (NBT) or SQLite (for market listings)

UI: Vanilla Minecraft UI Style (Screen/RenderSystem)

3. 主要機能仕様
A. 通貨システム (Currency System)
基本仕様: プレイヤーごとに独自の通貨変数を保持。

獲得ロジック: 経験値(XP)の獲得量と同等の通貨を付与。

ターゲットイベント: PlayerXpEvent.PickupXp

コマンドによるXP付与も検知対象とする。

保存: プレイヤーのData Attachmentとして永続化。

B. マーケットボード機能 (Market Board)
UI構成:

アイテムカテゴリ別検索（武器、防具、素材等）。

自由なキーワード検索。

出品一覧の表示（アイテム名、価格、出品者）。

取引ロジック:

出品: アイテムと価格を設定し、サーバー側ストレージに預ける。

購入: 通貨を支払い、アイテムを受け取る。

売上回収: 出品者が売上（通貨）を回収できる専用インターフェース。

データ管理: サーバー側で一元管理し、クライアントへパケット送受信 (CustomPayloadPacket) で同期。

4. 実装タスクリスト (Antigravity 用)
以下の順序でエージェントに実行させることを想定しています。

[ ] Phase 1: プロジェクトの初期化

NeoForgeのテンプレート作成

開発用依存関係のセットアップ

[ ] Phase 2: 通貨システムの基盤作成

Currency データの保存用Data Attachmentの実装

XP取得イベントのリスナー作成と通貨加算ロジックの実装

現在の所持金を確認するコマンド (/balance) の作成

[ ] Phase 3: マーケットデータの管理ロジック

MarketListing クラスの定義（アイテム、価格、出品者）

サーバー側での出品データ保存処理の実装

[ ] Phase 4: UI/UXの実装

Screen クラスを継承したマーケットボードUIの作成

アイテムグリッドと検索窓のレイアウト実装

[ ] Phase 5: 通信パケットの実装

出品・購入・検索リクエスト用のパケット定義とハンドラ作成

[ ] Phase 6: テストとデバッグ

複数プレイヤー間での取引整合性テスト

経験値トラップ等での通貨増殖・負荷テスト

5. 外部連携
現時点では外部API連携はなし。

すべてMOD内のローカルロジックで完結させる。


