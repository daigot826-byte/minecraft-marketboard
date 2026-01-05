# 最新の情報を反映させる
function marketboard:menu/render

# プレイヤーをGUIチェストの前にテレポート
# チェストを直接開かせることはできないため、目の前に飛ばしてクリックを促します
tp @s 0.5 -59 -1.5 0 0
tellraw @s {"text":"マーケットボードを開きました。目の前のチェストを確認してください。","color":"yellow"}