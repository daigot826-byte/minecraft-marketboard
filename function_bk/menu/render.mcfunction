# --- 1. チェストの配置と初期化 ---
# 座標 0 -60 0 をGUI用チェストとして使用（場所は任意で調整可能）
setblock 0 -60 0 minecraft:chest replace
data modify block 0 -60 0 CustomName set value '{"text":"マーケットボード"}'

# --- 2. ストレージからアイテムをコピー ---
# ストレージの items[0] をチェストの 0番スロットに同期
# 1.20.5以降の item block コマンドを使用（NBTも丸ごとコピーされます）
item block 0 -60 0 container.0 copy storage marketboard:main items[0]

# --- 3. (応用) Pythonでの量産を想定 ---
# 27スロット分を同期するには、ここを0〜26まで増やす必要があります。