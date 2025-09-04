package jp.co.wordbook;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

// DBの問題セットを格納する
public class Quiz {

    public int    id;               // クイズID
    public int    subject_id;       // 科目ID
    public int    difficulty_id;    // 難易度
    public String explanation;      // 説明文
    public String question;         // 問題文
    public String answer;           // 正解文

    // SQLのJOINで取得する部分 (詳細は下記)
    public String subject_name;     // 科目名
    public String difficulty_name;  // 難易度名


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

    // コンストラクタ (SQLリザルトを使用)
    public Quiz(ResultSet results) throws SQLException {
        id              = results.getInt("id");
        subject_id      = results.getInt("subject_id");
        difficulty_id   = results.getInt("difficulty_id");
        explanation     = results.getString("explanation");
        question        = results.getString("question");
        answer          = results.getString("answer");
    }

    // Subjectリストを生成
    public static List<Quiz> createList(ResultSet results)
        throws SQLException
    {
        List<Quiz> list = new ArrayList<>();

        while (results.next())
            list.add(new Quiz(results));

        return list;
    }

    //-------------------------------------------------------------------------

    // 全項目を取得するSQL文  (科目名, 難易度名 を追加で取得)
    public static final String ALL_COULUMN_SQL = 
        "SELECT quizzes.id, subject_id, difficulty_id, explanation, question, answer, subjects.name AS subject_name, difficultys.name AS difficulty_name " +
        "FROM quizzes " +
            "LEFT JOIN subjects ON subjects.id = quizzes.subject_id " +
            "LEFT JOIN difficultys ON difficultys.id = quizzes.difficulty_id " +
        "WHERE quizzes.id = ?;";

   
    // データベースからクイズを取得、リクエストへセット
    public static void setQuizToRequest
        (int quizID, String attributeName, HttpServletRequest request)
        throws ServletException, IOException
    {
        // データベースへ接続 / 処理実行
        ServletSupport.access(
            ALL_COULUMN_SQL,
            statement -> {
                try {
                    // クイズIDで指定、SQL文を実行
                    statement.setInt(1, quizID);
                    ResultSet results = statement.executeQuery();

                    // クイズ(科目名, 難易度名を含む) をリクエストへセット
                    if (results.next()) {
                        Quiz quiz = new Quiz(results);
                        quiz.subject_name    = results.getString("subject_name");
                        quiz.difficulty_name = results.getString("difficulty_name");
                        request.setAttribute(attributeName, quiz);
                    }
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        );
    }
}
