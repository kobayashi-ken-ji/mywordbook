package jp.co.wordbook.models;

import java.io.*;

/**
 * subjectテーブルの値転送用オブジェクト
 */
public class SubjectDTO implements Serializable
{
    private int id;
    private String name;
    private String user_id;

    // コンストラクタ
    public SubjectDTO() {}
    public SubjectDTO(int id, String name, String user_id) {
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
}