//=============================================================================
// [!] javascriptを変更しても更新されない場合は、ブラウザのキャッシュを削除
//=============================================================================

// トップページ 出題スタートボタン
function topStartButton() {
    
    const checkboxes = document.getElementsByName("difficultyids");

    // チェック済みがあるかを確認
    let isChecked = false;
    
    for (let checkbox of checkboxes) {
        if (checkbox.checked == true) {
            isChecked = true;
            break;
        }
    }

    // チェックされていない → ページ遷移を中止
    if (!isChecked) {
        alert("出題範囲を1つ以上選択してください。");
        event.preventDefault();
    }
}

// // トップページ 続きからボタン
// function topContinueButton() {

//     // 一度に出題する問題数
//     const questionLotSize = document.getElementsByName("question_lot_size");

    
// }


// トップページ チェックボックスを1つ以上選択させる
//  [不採用] CSSでチェックボックスが display:none 指定され、警告を表示できないため
// function addTopCheckBoxListener()
// {
//     const checkboxes = document.getElementsByName("difficultyids");
//     const message = "出題範囲を1つ以上選択してください。"

//     checkboxes.forEach(checkbox => {

//         // 表示メッセージを設定
//         checkbox.setCustomValidity(message);

//         // イベントリスナーを設定
//         checkbox.addEventListener("change", () => {

//             // チェック済みがあるかを確認
//             let isChecked = false;
//             for (let c of checkboxes) {
//                 if (c.checked == true) {
//                     isChecked = true;
//                     break;
//                 }
//             }

//             console.log(isChecked);

//             // チェック済み → requiredを外す、メッセージもなしにする
//             for (let c of checkboxes) {
//                 c.required = !isChecked;
//                 checkbox.setCustomValidity(isChecked ? "" : message);
//                 console.log(checkbox.setCustomValidity);
//             }
//         });
//     });
// }
