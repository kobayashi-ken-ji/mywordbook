import {Clipboard} from "./clipboard.mjs";
import readline from 'node:readline/promises';
import {stdin, stdout} from 'node:process';

//=============================================================================
// mywordbook用
// JSONテキスト → SQL挿入文 へ変換
//=============================================================================

// 改行文字
const LF = "\n";
const LF_ESC = "\\n"    // SQL内での改行用

// INSERT文
const INSERT_HEADER = 
    "INSERT" + LF +
    "    INTO quizzes(subject_id, difficulty_id, question, answer, explanation, is_asked)" + LF +
    "    VALUES" + LF;


// 科目ID, 難易度ID, 出題済みフラグ [変更可]
// const SUBJECT_ID = 1;
const DIFFICULTY_ID = 4;    // 1完璧, 2ほぼ覚, 3うろ覚, 4苦手
const IS_ASKED = "FALSE";

/** JSONテキスト → SQL挿入文 へ変換 */
class JsonToSQL
{
    /**
     * JSONテキスト → SQL挿入文 へ変換
     * @param {string} jsonText 単語集のJSON (文字列)
     * @returns 同期が必要  const sql = await jsonToSql.convert();
     */
    async convert(jsonText) {

        if (jsonText == "") {
            console.log("クリップボードにテキストがありません。");
            return null;
        }

        // JSONテキスト → オブジェクト化
        let jsonObject;
        try {
            jsonObject = JSON.parse(jsonText);
        } catch (error) {
            console.log("JSONとして解析できませんでした。", error);
            return null;
        }

        // 入力・出力ストリームを指定
        const reader = readline.createInterface({
            input   : stdin,
            output  : stdout,
        });

        // 科目IDをIOから取得
        const subujectId = await reader.question("科目IDを指定してください。>");
        reader.close();
        if (!subujectId) return null;


        // 各問題をSQL文へ変換
        const lines = Object.entries(jsonObject).map(([key, values])=>{

            //  問題文 : [正解文, 説明文1行目, 2行目, ...];
            const question    = key;
            const answer      = values[0];
            const explanation = values.slice(1).join(LF_ESC);

            // SQL化
            // 例  (0, 1, "問題文", "正解文", "説明文", FALSE)
            const sql = 
                `(${subujectId}, ${DIFFICULTY_ID}, ` +
                `\"${question}\", \"${answer}\", \"${explanation}\", ${IS_ASKED})`;

            // インデントを追加
            return "        " + sql;
        });

        // 配列を結合
        const sql = 
            INSERT_HEADER +
            lines.join("," + LF) +
            ";";

        return sql;
    }
}

//=============================================================================
// 実行
//=============================================================================

(async ()=>{

    // クリップボードから取得
    const clipboard = new Clipboard();
    const jsonText = clipboard.get();

    // JSONテキスト → SQL文
    const jsonToSql = new JsonToSQL();
    const sql = await jsonToSql.convert(jsonText);
    if (!sql) return;

    // クリップボードへ送る
    clipboard.set(sql);

    // デバッグ用表示
    console.log(sql);
})();

/*
    動作検証用 JSONテキスト
    {
        "名詞 to 原形" : [
            "～するための, ～すべき [不定詞]",
            " 形容詞的用法 (名詞を詳しく説明)",
            "Do you have anything to eat?",
            "He has something to say."
        ],
        "to 原形 [副詞的用法]" : [
            "～するために [不定詞]",
            "動詞を詳しく説明",
            "My brother went to Sydoney to study art.",
            "To see bill, we will go to India next week."
        ]
    }
*/
