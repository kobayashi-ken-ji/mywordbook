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
