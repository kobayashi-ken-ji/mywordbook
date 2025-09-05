package jp.co.wordbook;

import java.util.*;
import java.io.*;
import java.sql.*;
import javax.servlet.*;

/**
 * quizテーブルのレコードを扱うクラス
 */
public class Quiz {

    public int    id;               // クイズID
    public int    subject_id;       // 科目ID
    public int    difficulty_id;    // 難易度
    public String explanation;      // 説明文
    public String question;         // 問題文
    public String answer;           // 正解文


    // コンストラクタ
    public Quiz(
        int id, int subject_id, int difficulty_id,
        String explanation, String question, String answer
    ) {
        this.id             = id;
        this.subject_id     = subject_id;
        this.difficulty_id  = difficulty_id;
        this.explanation    = explanation;
        this.question       = question;
        this.answer         = answer;
    }


    // データベース接続を実装
    private static final DatabaseAccess<Quiz> database =
        results -> {
            return new Quiz(
                results.getInt("id"),
                results.getInt("subject_id"),
                results.getInt("difficulty_id"),
                results.getString("explanation"),
                results.getString("question"),
                results.getString("answer")
            );
        };
    
    //-------------------------------------------------------------------------
    // インスタンスの生成
    //-------------------------------------------------------------------------

    // レコードからインスタンスを生成
    public static Quiz getRecord(int quiz_id)
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM quizzes WHERE id = ?;";
        List<Quiz> list = database.createList(sql, quiz_id);
        
        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    public static List<Quiz> getRecords(int subject_id)
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM quizzes WHERE subject_id = ?;";
        List<Quiz> list = database.createList(sql, subject_id);
        return list;
    }


    // テーブルからインスタンスリストを生成 (難易度を指定版)
    public static List<Quiz> getRecords(int subject_id, String[] difficulty_ids)
        throws ServletException, IOException
    {
        // SQL条件(クイズ難易度) を作成
        //      複数の場合がある
        //      例:  配列 {"2","3"}  →  文字列 "difficulty_id=2 OR difficulty_id=3"
        String difficultiesSql = 
            Arrays.stream(difficulty_ids)
                .map(id -> "difficulty_id=" + Integer.parseInt(id))
                .reduce((String joined, String element) -> joined + " OR " + element)
                .orElse(null);
        
        // 難易度指定がない場合は、ANDも不要
        difficultiesSql = (difficultiesSql == null)
            ? ""
            : "AND (" + difficultiesSql + ")";

        final String sql = 
            "SELECT * FROM quizzes " +
            "WHERE subject_id = ? " + difficultiesSql + ";";

        System.out.println(sql);
        List<Quiz> list = database.createList(sql, subject_id);
        return list;
    }

    //-------------------------------------------------------------------------
    // レコードの 新規作成、上書き、削除
    //-------------------------------------------------------------------------

    // レコードの上書き(id>=0)、新規作成(id==0)
    public void updateRecord() throws ServletException, IOException {

        // 新規作成
        if (id == 0) {
            String sql = 
                "INSERT INTO quizzes(subject_id, difficulty_id, explanation, question, answer) " +
                "VALUES (?, ?, ?, ?, ?)";

            database.access(sql, statement -> {
                try {
                    statement.setInt(1, subject_id);
                    statement.setInt(2, difficulty_id);
                    statement.setString(3, explanation);
                    statement.setString(4, question);
                    statement.setString(5, answer);
                    statement.executeUpdate();

                    // 生成したIDを取得
                    ResultSet result = statement.getGeneratedKeys();
                    if(result.next())
                        this.id = result.getInt(1);

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        // 上書き
        else {
            String sql = 
                "UPDATE quizzes " +
                "SET subject_id=?, difficulty_id=?, explanation=?, question=?, answer=? " +
                "WHERE id = ?;";

            database.access(sql, statement -> {
                try {
                    statement.setInt(1, subject_id);
                    statement.setInt(2, difficulty_id);
                    statement.setString(3, explanation);
                    statement.setString(4, question);
                    statement.setString(5, answer);
                    statement.setInt(6, id);
                    statement.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    // レコードの削除
    public void destroyRecord() throws ServletException, IOException {

        String sql = "DELETE FROM quizzes WHERE id = ?";

        database.access(sql, statement -> {
            try {
                statement.setInt(1, id);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    // レコードの削除 (指定した科目のものを全て)
    public static void destroyRecords(int subject_id)
        throws ServletException, IOException
    {
        String sql = "DELETE FROM quizzes WHERE subject_id = ?";

        database.access(sql, statement -> {
            try {
                statement.setInt(1, subject_id);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
