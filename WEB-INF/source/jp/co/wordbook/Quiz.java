package jp.co.wordbook;

import java.util.*;
import java.io.IOException;
import javax.servlet.ServletException;

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


    // レコードからインスタンスを生成
    public static Quiz getFromDatabase(int quiz_id)
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM quizzes WHERE id = ?";
        List<Quiz> list = database.createList(sql, quiz_id);
        
        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    public static List<Quiz> getListFromDatabase(int subject_id)
        throws ServletException, IOException
    {
        final String sql = "SELECT * FROM quizzes WHERE subject_id = ?";
        List<Quiz> list = database.createList(sql, subject_id);
        return list;
    }
}
