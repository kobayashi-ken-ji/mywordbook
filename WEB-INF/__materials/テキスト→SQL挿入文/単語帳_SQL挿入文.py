"""
mywordbook用ユーティリティー。
クリップボードから取得、SQL文(単語挿入文)に変換し、再格納します。

クリップボードのテキストの形式
    1行目   問題文
    2行目   正解文
    3行目～ 説明文 (最大5行)
    空行    区切り文字
    以降繰り返し

[実行例]

assess
[動] 評価する, 査定する
risk assessment
- 事前にリスクの大きさを評価

↓

INSERT
    INTO quizzes(subject_id, difficulty_id, question, answer, explanation, is_asked)
    VALUES
        (1, 4, "assess", "[動] 評価する, 査定する", "risk assessment\n- 事前にリスクの大きさを評価", FALSE);
"""

from typing import Final, Iterable
import clipboard

#==============================================================================
# 定数
#==============================================================================

# 改行文字
LF      : Final = "\n"
LF_ESC  : Final = "\\n"          # SQL内での改行用
INDENT2 : Final = "        "     # インデント2つ分

# 難易度ID, 出題済みフラグ
DIFFICULTY_ID: Final = 4    # 1完璧, 2ほぼ覚, 3うろ覚, 4苦手
IS_ASKED: Final = "FALSE"

# INSERT文 (先頭部分のみ)
INSERT_HEADER: Final = (
    "INSERT" + LF +
    "    INTO quizzes(subject_id, difficulty_id, question, answer, explanation, is_asked)" + LF +
    "    VALUES" + LF
)

#==============================================================================
# 関数
#==============================================================================

# クリップボードの行リスト → SQL文
def lines_to_sql(lines: list[str]) -> Iterable[str]:

    # 科目IDを標準入力から取得
    print("科目ID 英単語:1")
    subject_id: Final = input("科目IDを指定してください。>")
    if not subject_id: return []

    # linesに空行を追加
    # 末尾に空行が無いと、最後の問題がsql_listに追加されない
    lines.append("")
    
    # SQL文リスト
    sql_list: Final[list[str]] = []

    # 問題を格納する
    question    : str = ""
    answer      : str = ""
    explanation : str = ""

    for line in lines:

        # 文字がある → 変数に格納
        if line:
            if not question: question = line
            elif not answer: answer = line
            elif not explanation: explanation = line
            else: explanation += LF_ESC + line  # 2行目以降は改行する
        
        # 空行(区切り文字) → 変数をSQL化
        else:
            # 問題文がない → 空行が続いたためスキップ
            if not question: continue

            # 正解文がない → 入力漏れ
            if not answer:
                raise ValueError("正解文が記入されていません。: " + question)

            # SQL文を生成、リストに追加
            # 例  (0, 1, "問題文", "正解文", "説明文", FALSE)
            sql_list.append( 
                f"{INDENT2}({subject_id}, {DIFFICULTY_ID}, " + 
                f"\"{question}\", \"{answer}\", \"{explanation}\", {IS_ASKED})"
            )

            # 問題変数を初期化
            question    = ""
            answer      = ""
            explanation = ""

    # 配列を結合
    SEPARATOR: Final = "," + LF
    sql = INSERT_HEADER + SEPARATOR.join(sql_list) + ";"

    # この関数内で結合したため、要素数1で返す
    return [sql]



# クリップボード処理に関数を渡す
clipboard.update( lines_to_sql )