package jp.co.wordbook;
import java.sql.*;

// DBの科目情報を格納する
public class Subject
{
    public int id;
    public String name;

    // コンストラクタ
    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // コンストラクタ (SQLリザルト版)
    public Subject(ResultSet results) throws Exception {
        id = results.getInt("id");
        name = results.getString("name");
    }
}