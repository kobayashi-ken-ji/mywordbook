//  child_process
//      Node.jsアプリ内で他のOSコマンドやプログラム（子プロセス）を生成・実行し、
//      その入出力を制御するための機能を提供
import {execSync} from "node:child_process";
import os from "node:os";


/** クリップボードへの読み書きを提供 */
export class Clipboard
{
    getCommand;
    setCommand;

    constructor() {
        const platform = os.platform();

        switch (platform) {

            // Windows
            case 'win32':
                // 文字化け対策にUTF8を指定
                this.getCommand = `powershell -Command "[Console]::OutputEncoding = [System.Text.Encoding]::UTF8; Get-Clipboard"`;
                this.setCommand = 'clip';
                break;

            // macOS
            case 'darwin':
                this.getCommand = 'pbpaste';
                this.setCommand = 'pbcopy';
                break;

            // Linux
            case 'linux':
                this.getCommand = 'xclip -selection clipboard -o';
                this.setCommand = 'xsel --clipboard --input';
                break;

            default:
                console.log("OSを識別できませんでした。 : ", platform);
        }

    }


    /** クリップボードから読み込み (同期処理) */
    get() {
        try {
            const text = execSync(this.getCommand, {encoding: "utf-8"});
            return text;
        } catch (err) {
            console.log("クリップボードの読み込みに失敗しました : ", err.message);
            return null;
        }
    }


    /** クリップボードへ書き込み (同期処理) */
    set(text) {
        try {
            execSync(this.setCommand, {input:text});
        } catch (err) {
            console.log("クリップボードの書き込みに失敗しました : ", err.message);
        }
    }
}

// デバッグ
// {
//     const clip = new Clipboard();
//     console.log(clip.get());
//     clip.set("書き込み");
//     console.log(clip.get());
// };
