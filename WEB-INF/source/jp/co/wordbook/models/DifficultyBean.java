package jp.co.wordbook.models;

import java.io.*;

/**
 * difficultiesテーブルへアクセスするクラス
 */
public class DifficultyBean implements Serializable
{
    private int id;
    private String name;

    // コンストラクタ
    public DifficultyBean() {}
    public DifficultyBean(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    // ゲッター / セッター
    public void setId(int id)        { this.id = id; }
    public void setName(String name) { this.name = name; }
    public int getId()               { return id; }
    public String getName()          { return name; }
}