//=============================================================================
// 音声読み上げ
//=============================================================================

class Speech extends SpeechSynthesisUtterance
{
    isValid;            // 読み上げAPIが有効か否か
    enable = false;     // speak()の有効/無効

    constructor() {
        super();
        this.lang = "en-US";
        this.rate = 0.9;    // 速度
        this.pitch = 1;     // 高さ
        this.volume = 1;    // 音量

        this.isValid = ('speechSynthesis' in window);
    }

    /**
     * テキスト読み上げ
     * @param {string} text 省略時は前回の内容を読み上げる
     */
    speak(text = null) {
        if (!this.isValid) return;
        if (!this.enable) return;
        if (text) this.text = text;
        window.speechSynthesis.speak(this);
    }
}

//=============================================================================
// 問題データ クラス
//=============================================================================

class Quiz
{
    /**
     * 出題用のデータをまとめるクラス
     * @param {number} id               問題のID
     * @param {number} difficulty_id    難易度
     * @param {string} question         問題文
     * @param {string} answer           正解文
     * @param {string} explanation      説明文
     */
    constructor(id, difficulty_id, question, answer, explanation) {
        this.id            = id;
        this.difficulty_id = difficulty_id;
        this.question      = question;
        this.answer        = answer;
        this.explanation   = explanation;
    }
}

//=============================================================================
// 出題処理を行うクラス
//=============================================================================

class ToQuiz
{
    // HTMLの要素を取得
    elements = {
        inputEnable   : document.getElementById("input-enable"),
        speakEnable   : document.getElementById("speak-enable"),
        count         : document.getElementById("count"),
        question      : document.getElementById("question"),
        input         : document.getElementById("input"),
        answer        : document.getElementById("answer"),
        explanation   : document.getElementById("explanation"),
        difficultyids : document.getElementsByName("difficultyids"),
        answerButton  : document.getElementById("answer-button"),
        nextButton    : document.getElementById("next-button"),
        quitButton    : document.getElementById("quit-button"),
    };

    // 出題用の変数
    subjectName   = "";     // 科目名
    quizzes       = [];     // 残りの問題リスト (出題ごとに減っていく)
    quizzesLength = 0;      // 問題総数
    quizCount     = 0;      // 出題数 (何問目か)
    correctCount  = 0;      // 正解数
    quiz          = null;   // 現在出題中の問題
    isAnswered    = false;  // すでに回答済みの問題かどうか

    // 読み上げ機能
    speech = new Speech();

    /**
     * 出題を開始する
     * @param {array} quizJson 
     */
    constructor(quizJson, subjectName) {

        this.subjectName = subjectName;

        // JSONから、Quizリストを生成
        this.quizzes = quizJson.map(args => new Quiz(...args));
        this.quizzesLength = this.quizzes.length;

        // 総問題数が0
        if (this.quizzes.length == 0) {
            console.log("問題数が0です");
            return;
        }

        // イベントリスナーを設定
        {
            const e = this.elements;
            e.inputEnable .addEventListener("change", () => this.applyInputEnable());
            e.speakEnable .addEventListener("change", () => this.speech.enable = e.speakEnable.checked);
            e.quitButton  .addEventListener("click" , () => this.goToResult());
            e.answerButton.addEventListener("click" , () => this.displayAnswer(true));

            // クリック時に答えを表示、離した時に次の問題へ
            // e.nextButton  .addEventListener("mousedown" , () => this.displayAnswer(false));
            // e.nextButton  .addEventListener("mouseup"   , () => this.displayNextQuiz());

            e.nextButton  .addEventListener("click" , () => this.displayNextQuiz());
        }

        // 難易度ボタン (複数のラジオボタン)
        for (let element of this.elements.difficultyids) {
            let value = element.value;
            element.addEventListener("click", () => this.sendDifficultyid(value));
        }

        // 入力する/入力しない を反映
        this.applyInputEnable()

        // 次の問題を表示
        this.displayNextQuiz();
    }


    // リザルト画面へ移行
    goToResult() {
        // URLを生成
        const rate = this.correctCount / this.quizCount * 100;
        const url =
            `result?subjectname=${this.subjectName}` +
            `&correctcount=${this.correctCount}` +
            `&quizcount=${this.quizCount}&rate=${rate}`;

        // ページ遷移
        window.location.href = url;
    }


    // 次の問題を表示
    displayNextQuiz() {

        // 全問終了 → 結果ページへ
        if (!this.quizzes.length) {
            this.goToResult();
            return;
        }

        // 問題をランダムに選出, 配列から削除
        const index = Math.floor(Math.random() * this.quizzes.length);
        const quiz  = this.quizzes.splice(index, 1)[0];
        this.quiz = quiz;

        const e = this.elements;

        // 正解文を保存、未回答状態へ
        this.isAnswered = false;

        // 難易度を反映
        // ラジオボタンの配列[ 難易度id-1 ]
        e.difficultyids[quiz.difficulty_id-1].checked = true;

        // 出題カウントを更新
        e.count.innerText =
            (++this.quizCount) + "問目 / " +
            this.quizzesLength + "問中";

        // 正解文、説明文を非表示
        e.answer     .classList.add('opacity_0');
        e.explanation.classList.add('opacity_0');

        // 問題文、説明文を更新
        e.question.innerText = quiz.question;
        e.explanation.innerText = quiz.explanation;

        // 回答欄をクリア
        e.input.value = "";

        // フォーカスを回答欄へ
        if (e.inputEnable.value == "true")
            e.input.focus();

        // 読み上げ
        this.speech.text = quiz.question; 
        this.speech.speak();
    }


    /**
     * 正解文を表示
     * @param {boolean} isSpeak 読み上げするか否か
     */
    displayAnswer(isSpeak = true) {

        // 読み上げ
        if (isSpeak)
            this.speech.speak();

        const e = this.elements;
        const inputValue = e.input.value;
        const isCorrect  = (inputValue == this.quiz.answer);

        // 初回答時の処理
        if (!this.isAnswered) {
            this.isAnswered = true;

            // 正解をカウント、説明文を可視化
            if (isCorrect) this.correctCount++;
            e.explanation.classList.remove('opacity_0');
        }

        // 正解判定マーク
        const mark =
            (inputValue == "") ? ""   :  // 回答無し → マーク無し
            (isCorrect       ) ? "○ " : "× ";

        // 正解を表示
        e.answer.innerText = mark + this.quiz.answer;
        e.answer.classList.remove('opacity_0');
        
    }


    // 回答欄を表示/非表示 を反映
    applyInputEnable() {

        // <input>内の class="display-none" を付与/削除
        const e = this.elements;
        (e.inputEnable.checked)
            ? e.input.classList.remove("display-none")
            : e.input.classList.add   ("display-none");
    }


    // 難易度の変更を送信 (onclick用)
    sendDifficultyid(difficulty_id) {
        
        // URLを作成
        const query = `?quizid=${this.quiz.id}&difficultyid=${difficulty_id}`;
        const url = "difficultyupdate" + query;

        // 送信
        fetch(url)
            .catch(error => console.error(error));
    }
}

//=============================================================================
// 実行
//=============================================================================

new ToQuiz(quizJson, subjectName);
