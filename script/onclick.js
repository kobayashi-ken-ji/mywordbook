//=============================================================================
// [!] javascriptを変更しても更新されない場合は、ブラウザのキャッシュを削除
//=============================================================================

// トップページ 出題スタートボタン
function topStartButton() {
    
    if (
        // 難易度が増えた場合は、こちらにも追加
        !document.getElementById("difficultyid1").checked &&
        !document.getElementById("difficultyid2").checked &&
        !document.getElementById("difficultyid3").checked
    ) {
        alert("出題範囲が１つも選択されていません。");
        event.preventDefault();
    }
}


// 科目編集 保存ボタン
function subjectSaveButton() {
    
    if (!document.form.subjectname.value) {
        alert("科目名が入力されていません。");
        event.preventDefault();
    }
}


// 科目編集 削除ボタン
function subjectDeleteButton() {

    if (!confirm("科目を削除しますか？"))
        event.preventDefault();
}


// 問題編集 保存ボタン
function quizSaveButton() {
    
    let text = "";

    if (!document.form.subjectid.value)
        text += "科目が選択されていません。\n";

    if (!document.form.question.value) 
        text += "問題文が入力されていません。\n";
    
    if (!document.form.answer.value)
        text += "正解文が入力されていません。\n";

    if (text != "") {
        alert(text);
        event.preventDefault();
    }
}


// 問題編集 削除ボタン
function quizDeleteButton() {

    if (!confirm("この問題を削除しますか？"))
        event.preventDefault();
}

