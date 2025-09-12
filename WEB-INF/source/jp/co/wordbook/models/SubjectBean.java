package jp.co.wordbook.models;

import java.io.*;

/**
 * subjectテーブルのレコードを扱うクラス
 */
public class SubjectBean implements Serializable
{
    private int id;
    private String name;
    private String user_id;

    // コンストラクタ
    public SubjectBean() {}
    public SubjectBean(int id, String name, String user_id) {
        this.id      = id;
        this.name    = name;
        this.user_id = user_id;
    }

    // セッター
    public void setId(int id)              { this.id = id; }
    public void setName(String name)       { this.name = name; }
    public void setUser_id(String user_id) { this.user_id = user_id; }

    // ゲッター
    public int getId()         { return id; }
    public String getName()    { return name; }
    public String getUser_id() { return user_id; }

    // idを文字列で取得
    public String getIdString() {
        return (id == 0) 
            ? "- 新規作成 -"
            : Integer.toString(id);
    }
}