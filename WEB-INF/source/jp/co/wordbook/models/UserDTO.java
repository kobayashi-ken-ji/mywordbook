package jp.co.wordbook.models;

import java.io.*;

/**
 * usersテーブルの値転送用オブジェクト
 */
public class UserDTO implements Serializable
{
    private String id;
    private String password;

    // コンストラクタ
    public UserDTO() {}
    public UserDTO(String id, String password) {
        this.id = id;
        this.password = password;
    }

    // ゲッター / セッター
    public void setId(String id)             { this.id = id; }
    public void setPassword(String password) { this.password = password; }
    public String getId()                    { return id; }
    public String getPassword()              { return password; }
}