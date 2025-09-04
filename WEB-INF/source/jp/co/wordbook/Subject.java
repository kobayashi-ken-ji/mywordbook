package jp.co.wordbook;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.servlet.*;

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
    public static Subject getRecord(int subject_id)
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM subjects WHERE id = ?";
        List<Subject> list = database.createList(sql, subject_id);

        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    public static List<Subject> getRecords()
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM subjects";
        List<Subject> list = database.createList(sql);
        return list;
    }


    // レコードの上書き(id>=0)、新規作成(id==0)
    public void updateRecord() throws ServletException, IOException {

        // 新規作成
        if (id == 0) {
            String sql = "INSERT INTO subjects(name) VALUES (?)";

            database.access(sql, statement -> {
                try {
                    statement.setString(1, name);
                    statement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        // 上書き
        else {
            String sql = "UPDATE subjects SET name = ? WHERE id = ?;";

            database.access(sql, statement -> {
                try {
                    statement.setString(1, name);
                    statement.setInt(2, id);
                    statement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // レコードの削除
    public void destroyRecord() throws ServletException, IOException {

        String sql = "DELETE FROM subjects WHERE id = ?";

        database.access(sql, statement -> {
            try {
                statement.setInt(1, id);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}