package jp.co.wordbook;

import java.util.*;
import java.io.IOException;
import javax.servlet.ServletException;

/**
 * subjectテーブルのレコードを扱うクラス
 */
public class Subject
{
    public int id;
    public String name;

    // コンストラクタ
    public Subject(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    // データベース接続を実装
    private static final DatabaseAccess<Subject> database =
        results -> {
            return new Subject(
                results.getInt("id"),
                results.getString("name")
            );
        };
    

    // レコードからインスタンスを生成
    static Subject getFromDatabase(int subject_id)
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM subjects WHERE id = ?";
        List<Subject> list = database.createList(sql, subject_id);

        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    static List<Subject> getListFromDatabase()
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM subjects";
        List<Subject> list = database.createList(sql);
        return list;
    }
}