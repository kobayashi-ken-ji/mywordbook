package jp.co.wordbook.models;

import java.io.*;

/**
 * subjectテーブルのレコードを扱うクラス
 */
public class SubjectBean implements Serializable
{
    private int id;
    private String name;

    // コンストラクタ
    public SubjectBean() {}
    public SubjectBean(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    // ゲッター / セッター
    public void setId(int id)        { this.id = id; }
    public void setName(String name) { this.name = name; }
    public int getId()               { return id; }
    public String getName()          { return name; }

    // idを文字列で取得
    public String getIdString() {
        return (id == 0) 
            ? "- 新規作成 -"
            : Integer.toString(id);
    }
}