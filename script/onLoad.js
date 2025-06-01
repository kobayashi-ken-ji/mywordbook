
const JSON_URL = "./script/vocabularies.json"

//=============================================================================
// 問題データ クラス
//=============================================================================

class Question
{
    constructor(english, japanese)
    {
        this.english  = english;
        this.japanese = japanese;
    }
}

//=============================================================================
// 読込時の処理をまとめたクラス
//=============================================================================

class OnLoad
{
    elements        = {};   // HTML要素
    questions       = [];   // Question型配列
    totalQuestions  = 0;    // 問題総数 (開始前)
    questionCount   = 0;    // 出題数 (何問目か)
    correctCount    = 0;    // 正解数
    incorrectCount  = 0;    // 不正解数
    correctText     = "";   // 現在の問題の答え


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
    setEventListener(json)
    {
        // JSON → Question配列
        for (let key in json) 
            this.questions.push( new Question(key, json[key]) );

        // 問題総数を記録
        this.totalQuestions = this.questions.length;

        // HTMLの要素を取得
        this.elements = {
            modeDropdown      : document.getElementById("mode"),
            questionCountText : document.getElementById("question-count"),
            questionText      : document.getElementById("question"),
            answerInput       : document.getElementById("answer-input"),
            correctAnswerText : document.getElementById("correct-answer"),
            answerButton      : document.getElementById("answer-button"),
            nextButton        : document.getElementById("next-button"),
        };

        // イベントリスナーを設定
        const e = this.elements;
        e.answerButton.addEventListener("click", ()=>this.applyCorrectAnswer() );
        e.nextButton  .addEventListener("click", ()=>this.applyNextQuestion()  );

        // 次の問題をHTMLに反映
        this.applyNextQuestion();
    }


    // 正解テキストを反映
    applyCorrectAnswer()
    {
        const e = this.elements;
        const value = e.answerInput.value;

        // 正解/不正解をカウント
        const isCorrect = (value == this.correctText);
        if (isCorrect)  this.correctCount++;
        else            this.incorrectCount++;

        // 正解判定マーク
        const mark =
            (value == "")   ? ""  :  // 回答無し → マーク無し
            (isCorrect)     ? "○ ":
                              "× ";

        // 正解を表示
        e.correctAnswerText.innerText = mark + this.correctText;
        e.correctAnswerText.classList.remove('opacity_0');
    }


    // 次の問題をHTMLに反映
    applyNextQuestion()
    {
        // 出題終了
        if (!this.questions.length) {
            alert("全問終了しました。");
            location.reload();
            return;
        }

        // 問題をランダムに選出, 配列から削除
        const index     = Math.floor(Math.random() * this.questions.length);
        const question  = this.questions.splice(index, 1)[0];

        // 出題モードを取得
        const e = this.elements;
        const isEnglishMode = (e.modeDropdown.value == "english");

        // 出題数をカウント, 表示
        this.questionCount++;
        e.questionCountText.innerText = 
            this.questionCount + "問目 / " +
            this.totalQuestions + "問中";

        // 問題文を表示
        e.questionText.innerText = (isEnglishMode)
            ? question.japanese
            : question.english;

        // 正解データを保存
        this.correctText = (isEnglishMode)
            ? question.english
            : question.japanese;
       
        // 回答表示をクリア
        e.answerInput.value = "";

        // 正解を不透明度0に
        //      文字が無くなると、高さを失ってしまうため
        e.correctAnswerText.classList.add('opacity_0');

        // フォーカスを回答欄へ
        e.answerInput.focus();
    }
}

//=============================================================================
// 実行
//=============================================================================

new OnLoad(JSON_URL);
