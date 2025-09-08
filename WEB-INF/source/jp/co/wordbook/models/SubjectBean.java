package jp.co.wordbook.models;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.servlet.*;

/**
 * subjectテーブルのレコードを扱うクラス
 */
public class SubjectBean implements Serializable
{
    private int id;
    private String name;

    // コンストラクタ
    public SubjectBean() {}
    public SubjectBean(int id, String name) {
        this.id   = id;
        this.name = name;
    }

    // データベース接続を実装
    private static final DatabaseAccess<SubjectBean> database =
        results -> {
            return new SubjectBean(
                results.getInt("id"),
                results.getString("name")
            );
        };
    
    //-------------------------------------------------------------------------
    // ゲッター / セッター
    //-------------------------------------------------------------------------
    
    public void setId(int id)        { this.id = id; }
    public void setName(String name) { this.name = name; }
    public int getId()               { return id; }
    public String getName()          { return name; }

    // idを文字列化して返す
    public String getIdString() {
        return (id == 0) 
            ? "- 新規作成 -"
            : Integer.toString(id);
    }

    //-------------------------------------------------------------------------
    // データベースへのアクセス
    //-------------------------------------------------------------------------

    // レコードからインスタンスを生成
    public static SubjectBean getRecord(int subject_id)
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM subjects WHERE id = ?";
        List<SubjectBean> list = database.createList(sql, subject_id);

        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    public static List<SubjectBean> getRecords()
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM subjects";
        List<SubjectBean> list = database.createList(sql);
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