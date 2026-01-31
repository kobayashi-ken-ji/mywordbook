package jp.co.wordbook.models;

import java.io.*;

/**
 * difficultiesテーブルの値転送用オブジェクト
 */
public class DifficultyDTO implements Serializable
{
    // DBの値
    private int id;
    private String name;

    // サーブレットからJSPへ渡す値 (チェックボックスの初期値)
    private boolean checked = false;


    // コンストラクタ
    public DifficultyDTO() {}
    public DifficultyDTO(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    // セッター
    public void setId(int id)               { this.id = id; }
    public void setName(String name)        { this.name = name; }
    public void SetChecked(boolean checked) { this.checked = checked; }

    // ゲッター
    public int getId()         { return id; }
    public String getName()    { return name; }
    public boolean isChecked() { return checked; }
}