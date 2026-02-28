"""クリップボード操作に関するユーティリティモジュール"""
import pyperclip
import re
from typing import Final, Callable, Iterable


def update(convert: Callable[ [list[str]], Iterable[str] ]) -> str:

    """クリップボードの内容を取得・加工して再格納します。

    この関数では次の処理を担当します。
    ・クリップボードへのアクセス
    ・改行文字での分割、結合

    Args:
        convert: クリップボードの各行を受け取り、変換後の各行を返す関数。

    Returns:
        クリップボードへ最終的に格納された文字列。

    Examples:
        クリップボードの内容を逆順にする例:
        >>> clipboard.update(reversed)
    """

    # クリップボードから取得
    text: Final = pyperclip.paste()
    if not text:
        print("クリップボードに値がありません。")
        return ""

    # 改行文字を取得 (最初に出現する改行)
    EOL_REG: Final = r"\r\n|\n|\r"
    match: Final = re.search(EOL_REG, text)
    eol: Final = match[0] if match else "\n"

    # 配列化 → 変換処理 → 文字列に戻す
    lines: Final[list[str]] = re.split(EOL_REG, text)
    converted: Final = convert(lines)
    new_text: Final = eol.join(converted)

    # クリップボードへ送信
    pyperclip.copy(new_text)

    # デバッグ用表示
    print(new_text)
    return new_text