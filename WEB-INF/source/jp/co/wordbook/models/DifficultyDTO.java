package jp.co.wordbook.models;

import java.io.*;

/**
 * difficultiesテーブルの値転送用オブジェクト
 */
public class DifficultyDTO implements Serializable
{
    private int id;
    private String name;

    // コンストラクタ
    public DifficultyDTO() {}
    public DifficultyDTO(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    // ゲッター / セッター
    public void setId(int id)        { this.id = id; }
    public void setName(String name) { this.name = name; }
    public int getId()               { return id; }
    public String getName()          { return name; }
}