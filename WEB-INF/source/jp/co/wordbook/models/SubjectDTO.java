package jp.co.wordbook.models;

import java.io.*;

/**
 * subjectテーブルの値転送用オブジェクト
 */
public class SubjectDTO implements Serializable
{
    private int id;
    private String name;
    private String userId;

    // コンストラクタ
    public SubjectDTO() {}
    public SubjectDTO(int id, String name, String userId) {
        this.id     = id;
        this.name   = name;
        this.userId = userId;
    }

    // セッター
    public void setId(int id)             { this.id = id; }
    public void setName(String name)      { this.name = name; }
    public void setUserId(String userId) { this.userId = userId; }

    // ゲッター
    public int getId()         { return id; }
    public String getName()    { return name; }
    public String getUserId()  { return userId; }
}