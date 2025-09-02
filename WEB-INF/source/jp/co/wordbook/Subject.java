package jp.co.wordbook;
import java.sql.*;
import java.util.*;

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
    public Subject(ResultSet results) throws SQLException {
        id = results.getInt("id");
        name = results.getString("name");
    }


    // ユーティリティー
    // SQLのリザルトからSubjectリストを生成
    public static List<Subject> createList(ResultSet results) {

        List<Subject> subjects = new ArrayList<>();

        try {
            while (results.next())
                subjects.add(new Subject(results));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return subjects;
    }
}