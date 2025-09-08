package jp.co.wordbook.models;

import java.util.*;
import java.io.*;
import javax.servlet.*;

/**
 * subjectテーブルのレコードを扱うクラス
 */
public class DifficultyBean implements Serializable
{
    private int id;
    private String name;

    // コンストラクタ
    public DifficultyBean() {}
    public DifficultyBean(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    //---------------------------------
    // ゲッター / セッター
    //---------------------------------
    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // データベース接続を実装
    private static final DatabaseAccess<DifficultyBean> database =
        results -> {
            return new DifficultyBean(
                results.getInt("id"),
                results.getString("name")
            );
        };
    

    // レコードからインスタンスを生成
    public static DifficultyBean getRecord(int difficulty_id)
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM difficulties WHERE id = ?";
        List<DifficultyBean> list = database.createList(sql, difficulty_id);

        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    public static List<DifficultyBean> getRecords()
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM difficulties";
        List<DifficultyBean> list = database.createList(sql);
        return list;
    }
}