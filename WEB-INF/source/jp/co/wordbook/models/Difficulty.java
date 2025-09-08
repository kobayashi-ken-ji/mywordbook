package jp.co.wordbook.models;

import java.util.*;
import java.io.*;
import javax.servlet.*;

/**
 * subjectテーブルのレコードを扱うクラス
 */
public class Difficulty
{
    public int id;
    public String name;

    // コンストラクタ
    public Difficulty(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    // データベース接続を実装
    private static final DatabaseAccess<Difficulty> database =
        results -> {
            return new Difficulty(
                results.getInt("id"),
                results.getString("name")
            );
        };
    

    // レコードからインスタンスを生成
    public static Difficulty getRecord(int difficulty_id)
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM difficulties WHERE id = ?";
        List<Difficulty> list = database.createList(sql, difficulty_id);

        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    public static List<Difficulty> getRecords()
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM difficulties";
        List<Difficulty> list = database.createList(sql);
        return list;
    }
}