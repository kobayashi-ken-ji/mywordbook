package jp.co.wordbook;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DBの科目を格納する
public class Subject
{
    public int id;
    public String name;

    // コンストラクタ
    public Subject(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    // コンストラクタ (SQLリザルトを使用)
    public Subject(ResultSet results) throws SQLException {
        this.id   = results.getInt("id");
        this.name = results.getString("name");
    }


    // Subjectリストを生成
    public static List<Subject> createList(ResultSet results)
        throws SQLException
    {
        List<Subject> list = new ArrayList<>();

        while (results.next())
            list.add(new Subject(results));

        return list;
    }
}