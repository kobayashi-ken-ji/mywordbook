
const JSON_URL = "./script/vocabularies.json"

//=============================================================================
// 問題データ クラス
//=============================================================================

class Question
{
    /**
     * JSON → 英語, 日本語, 例文
     * @param {string}   key    english
     * @param {string[]} values [japanese, example1, example2, ...]
     */
    constructor(key, values)
    {
        this.english  = key;
        this.japanese = values.shift();
        this.example  = values.join("\n");
    }
}

//=============================================================================
// 読込時の処理をまとめたクラス
//=============================================================================

class OnLoad
{
    // HTMLの要素を取得
    elements = {
        typeDropdown     : document.getElementById("type-dropdown"),
        languageDropdown : document.getElementById("language-dropdown"),
        noInputDropdown  : document.getElementById("no-input-dropdown"),
        countText        : document.getElementById("count-text"),
        questionText     : document.getElementById("question-text"),
        answerInput      : document.getElementById("answer-input"),
        correctText      : document.getElementById("corrent-text"),
        answerButton     : document.getElementById("answer-button"),
        nextButton       : document.getElementById("next-button"),
        exampleText      : document.getElementById("example-text"),
    };

    // Question型
    questionStorage = {
        word        : [],   // 単語
        idiom       : [],   // 熟語
        syntax      : [],   // 構文
        conjugation : [],   // 活用
    };

    // 出題管理用の変数
    questions       = [];   // 残りの問題リスト (出題ごとに減っていく)
    questionsLength = 0;    // 問題総数
    questionCount   = 0;    // 出題数 (何問目か)
    correctCount    = 0;    // 正解数
    correctData     = "";   // 現在の問題の答え
    isAnswered = false;     // すでに回答済みの問題かどうか


    // JSONを読み込み後、イベントリスナーを設定
    constructor(url)
    {
        // リクエストを送信, JSONを抽出, イベントリスナーを設定, エラー処理
        fetch(url)
            .then( response => response.json() )
            .then( json => this.setEventListener(json) )
            .catch( error => console.log("JSONの読込に失敗", error) );
    }


    // JSON読込後の処理
    setEventListener(jsonAll)
    {
        // JSON → questionStorage
        for (let type in this.questionStorage) {

            const storage = this.questionStorage[type];
            const json = jsonAll[type];

            // Question型化
            for (let key in json)
                storage.push(new Question(key, json[key]));
        }

        // イベントリスナーを設定
        const e = this.elements;
        e.typeDropdown   .addEventListener("change", () => this.applyQuestionType());
        e.noInputDropdown.addEventListener("change", () => this.applyAnswerInputVisible());
        e.answerButton   .addEventListener("click" , () => this.applyCorrectText());
        e.nextButton     .addEventListener("click" , () => this.applyNextQuestion());

        // 入力する/入力しない を反映
        this.applyAnswerInputVisible()

        // 出題モードを反映
        this.applyQuestionType();
    }


    // 出題タイプを反映
    applyQuestionType()
    {
        const e = this.elements;
        const type = e.typeDropdown.value;

        // 分詞モードの場合は、日本語←→英語 を無効にする
        if (type == "conjugation") {
            e.languageDropdown.value = "japanese";
            e.languageDropdown.disabled = true;
        }

        // それ以外は 無効を解除
        else e.languageDropdown.disabled = false;

        // 問題リスト, 問題総数, 回答数, 正解数 を更新
        this.questions       = [...this.questionStorage[type]];
        this.questionsLength = this.questions.length;
        this.questionCount   = 0;
        this.correctCount    = 0;

        // 次の問題をHTMLに反映
        this.applyNextQuestion();
    }


    // 正解テキストを反映
    applyCorrectText()
    {
        const e = this.elements;
        const inputValue = e.answerInput.value;
        const isCorrect  = (inputValue == this.correctData);

        // 初回答 → 回答済み
        if (!this.isAnswered) {
            this.isAnswered = true;

            // 正解カウント
            if (isCorrect) this.correctCount++;

            // 例文を表示
            e.exampleText.classList.remove('opacity_0');
        }

        // 正解判定マーク
        const mark =
            (inputValue == "") ? ""   :  // 回答無し → マーク無し
            (isCorrect       ) ? "○ " : "× ";

        // 正解を表示
        e.correctText.innerText = mark + this.correctData;
        e.correctText.classList.remove('opacity_0');
    }


    // 次の問題をHTMLに反映
    applyNextQuestion()
    {
        // 全問終了 → 出題を初期化
        if (!this.questions.length) {
            alert(
                "全問終了しました。\n" + 
                "正解数" + this.correctCount + "/" + this.questionsLength
            );
            this.applyQuestionType();
            return;
        }

        // 問題をランダムに選出, 配列から削除
        const index     = Math.floor(Math.random() * this.questions.length);
        const question  = this.questions.splice(index, 1)[0];

        // 未回答状態へ
        this.isAnswered = false;

        // 出題モード,入力モード を取得
        const e = this.elements;
        const isEnglishMode = (e.languageDropdown.value == "english");
        const isInputMode   = (e.noInputDropdown.value  == "input");

        // 出題数をカウント, 表示
        e.countText.innerText =
            (++this.questionCount) + "問目 / " +
            this.questionsLength + "問中";

        // 問題文を表示
        e.questionText.innerText = (isEnglishMode)
            ? question.japanese
            : question.english;

        // 正解データを保存
        this.correctData = (isEnglishMode)
            ? question.english
            : question.japanese;

        // 回答表示をクリア
        e.answerInput.value = "";

        // 正解を不透明度0に
        //      文字が無くなると、高さを失ってしまうため
        e.correctText.classList.add('opacity_0');
        e.exampleText.classList.add('opacity_0');

        // 例文を反映
        e.exampleText.innerText = question.example;

        // フォーカスを回答欄へ
        if (isInputMode)
            e.answerInput.focus();
    }


    // 入力する/入力しない を反映
    applyAnswerInputVisible()
    {
        const e = this.elements;
        const isInputMode = (e.noInputDropdown.value == "input");

        // 表示/非表示 を切替え
        (isInputMode)
            ? e.answerInput.classList.remove("display-none")
            : e.answerInput.classList.add   ("display-none");
    }
}

//=============================================================================
// 実行
//=============================================================================

new OnLoad(JSON_URL);
