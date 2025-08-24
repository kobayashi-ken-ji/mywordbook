//=============================================================================
// クリップボード(JSONテキスト) → オブジェクト
//      ※ Mery 2.6.7 で実行
//=============================================================================

function parseJsonText()
{
	var clipData = ClipboardData.GetData();

	if (clipData == "") {
		alert("クリップボードにテキストがありません。");
		return;
	}

	try {
		return JSON.parse(clipData);
	}
	catch (error) {
		alert("JSONとして解析できませんでした。");
		return null;
	}
}

//=============================================================================
// オブジェクト → SQL挿入文
//=============================================================================

// 改行文字
const LF = "\n";
const LF_ESC = "\\n"    // SQL内での改行用

// INSERT文
const INSERT_HEADER = 
    "INSERT" + LF +
    "    INTO quizzes(subject_id, difficulty_id, question, answer, explanation)" + LF +
    "    VALUES" + LF;


// 科目ID, 難易度ID [変更可]
const SUBJECT_ID    = 1;
const DIFFICULTY_ID = 2;    // 1 簡単, 2 普通, 3 難しい


// オブジェクト → SQL挿入文
function getSqlStatements(json)
{
    var lines = [];

    // 各問題をSQL文へ変換
    for (var key in json) {

        //  問題文 : [正解文, 説明文1行目, 2行目, ...];
        var texts       = json[key];
        var question    = key;
        var answer      = texts[0];
        var explanation = texts.slice(1).join(LF_ESC);

        lines.push(
            "        " +
            "(" +
            SUBJECT_ID          + ", " +
            DIFFICULTY_ID       + ", " +
            "\"" + question     + "\", " +
            "\"" + answer       + "\", " +
            "\"" + explanation  + "\"" +
            ")"
        );
    }

    // 配列を結合
    var sql = 
        INSERT_HEADER +
        lines.join("," + LF) +
        ";";

    // クリップボードへ送る
    ClipboardData.SetData(sql);
    document.selection.Paste();
}

//=============================================================================
// 実行
//=============================================================================

getSqlStatements(
    parseJsonText()
);


/*
    動作検証用JSON
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