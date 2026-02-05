//=============================================================================
// 音声読み上げ (英語)
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
     * @param {number} difficultyId     難易度
     * @param {string} question         問題文
     * @param {string} answer           正解文
     * @param {string} explanation      説明文
     */
    constructor(id, difficultyId, question, answer, explanation) {
        this.id           = id;
        this.difficultyId = difficultyId;
        this.question     = question;
        this.answer       = answer;
        this.explanation  = explanation;
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
        speechEnabled : document.getElementById("speak-enable"),
        totalCount    : document.getElementById("total-count"),
        countInLot    : document.getElementById("count-in-lot"),
        question      : document.getElementById("question"),
        input         : document.getElementById("input"),
        answer        : document.getElementById("answer"),
        explanation   : document.getElementById("explanation"),
        difficultyids : document.getElementsByName("difficultyids"),
        answerButton  : document.getElementById("answer-button"),
        retestButton  : document.getElementById("retest-button"),
        nextButton    : document.getElementById("next-button"),
        quitButton    : document.getElementById("quit-button"),
    };

    // 出題用の変数
    subjectName    = "";     // 科目名
    quizzes        = [];     // 残りの問題リスト (出題ごとに減っていく)
    solvedQuizzes  = [];     // 回答し終えた問題のリスト
    totalCount     = 0;      // 何問目か (全体)
    countInLot     = 0;      // 何問目か (今回)
    totalSize      = 0;      // 全体の総数 
    lotSize        = 0;      // 今回の総数
    correctCount   = 0;      // 正解数 (今回)
    noRetestCount  = 0;      // 再出題せず、次へ進んだ数 (ユーザーの習得度が高い)
    quiz           = null;   // 現在出題中の問題
    isAnswered     = false;  // すでに回答済みの問題か否か
    isDone         = false;  // 出題が一巡したか否か

    // 読み上げ機能
    speech = new Speech();

    /**
     * 出題を開始する
     * @param {array} quizJson 
     */
    constructor(quizJson, subjectName, answeredCount, totalSize) {

        this.subjectName = subjectName;
        this.totalSize = totalSize;
        this.totalCount = answeredCount;

        // JSONから、Quizリストを生成
        this.quizzes = quizJson.map(args => new Quiz(...args));
        this.lotSize = this.quizzes.length;

        // 総問題数が0
        if (this.quizzes.length == 0) {
            console.log("問題数が0です");
            return;
        }

        // イベントリスナーを設定
        {
            const e = this.elements;
            this.speech.enable = e.speechEnabled.checked;
            e.inputEnable  .addEventListener("change", () => this.applyInputEnable());
            e.speechEnabled.addEventListener("change", () => this.speech.enable = e.speechEnabled.checked);
            e.quitButton   .addEventListener("click" , () => this.goToResult());
            e.answerButton .addEventListener("click" , () => this.displayAnswer(true));
            e.nextButton   .addEventListener("click" , () => this.displayNextQuiz(false));
            e.retestButton .addEventListener("click" , () => this.displayNextQuiz(true));
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

        const form = document.getElementById("form");

        // 出題済み問題のID配列 を生成
        const quizIds = this.solvedQuizzes.map(quiz => quiz.id);

        // 配列の要素を、隠し要素としてフォーム追加
        quizIds.forEach(quizId => {
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = "quizIds";
            input.value = quizId;
            form.appendChild(input);
        });

        // フォームに追加するラムダ
        const input = (name, value)=>{
            const input = document.createElement("input");
            input.type = "hidden";
            input.name = name;
            input.value = value;
            form.appendChild(input);
        };

        // その他、クエリで送信していたものもフォームに追加
        input("subjectName", this.subjectName);
        input("correctCount", this.correctCount);
        input("noRetestCount", this.noRetestCount);
        input("isSpeechEnabled", this.elements.speechEnabled.checked);
        input("isInputEnabled", this.elements.inputEnable.checked);

        // POST送信
        form.submit();
    }


    // 次の問題を表示
    // 再出題ボタンが押された場合は true
    displayNextQuiz(isRetest = false) {

        // 再出題 → 前回の問題を、配列の末尾に追加する
        if (isRetest)
            this.quizzes.push(this.quiz);

        // 再出題しない → 終了配列に格納
        else if (this.quiz != null) {
            this.solvedQuizzes.push(this.quiz);

            // 再出題しなかった問題をカウント
            if (!this.isDone)
                this.noRetestCount++;
        }

        // 全問終了 → 結果ページへ
        if (!this.quizzes.length) {
            this.goToResult();
            return;
        }

        // 問題をデキューする
        const quiz = this.quizzes.shift();
        this.quiz = quiz;

        const e = this.elements;

        // 正解文を保存、未回答状態へ
        this.isAnswered = false;

        // 難易度を反映
        //  ・ ラジオボタン配列 : 0開始
        //  ・ 難易度id- : 1開始
        e.difficultyids[quiz.difficultyId-1].checked = true;

        // 出題カウントを更新 (全体、今回)
        // 今回の出題数より多い部分は、再出題部分の為カウントしない
        if (this.countInLot < this.lotSize) {
            e.totalCount.innerText =
                (++this.totalCount) + "問目 / " + this.totalSize + "問中";

            e.countInLot.innerText =
                (++this.countInLot) + "問目 / " + this.lotSize + "問中";
        }

        // 出題数が終わったフラグ
        else this.isDone = true;

        // 正解文、説明文を非表示
        e.answer     .classList.add('opacity_0');
        e.explanation.classList.add('opacity_0');

        // 問題文、説明文を更新
        e.question.innerText = quiz.question;
        e.explanation.innerText = quiz.explanation;

        // 回答欄をクリア
        e.input.value = "";

        // フォーカスを回答欄へ
        if (e.inputEnable.checked)
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
    sendDifficultyid(difficultyId) {
        
        // クイズ情報にも反映
        // 再出題するときに表示するため
        this.quiz.difficultyId = difficultyId;

        // URLを作成
        const query = `?quizid=${this.quiz.id}&difficultyid=${difficultyId}`;
        const url = "difficultyupdate" + query;

        // 送信
        fetch(url)
            .catch(error => console.error(error));
    }
}

//=============================================================================
// 実行
//=============================================================================

new ToQuiz(quizJson, subjectName, answeredCount, totalSize);
