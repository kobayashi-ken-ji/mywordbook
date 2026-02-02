package jp.co.wordbook.models;
import java.io.*;

/**
 * usersテーブルの値転送用オブジェクト
 */
public class UserDTO implements Serializable
{
    // ユーザーの基本情報
    private String id;
    private String password;

    // コンストラクタ
    public UserDTO() {}
    public UserDTO(String id, String password) {
        this.id = id;
        this.password = password;
    }

    // ゲッター
    public void setId(String id)             { this.id = id; }
    public void setPassword(String password) { this.password = password; }
    
    // セッター
    public String getId()       { return id; }
    public String getPassword() { return password; }
}