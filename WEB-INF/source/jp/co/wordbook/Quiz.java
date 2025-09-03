package jp.co.wordbook;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// DBの問題を格納する
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

    // コンストラクタ (SQLリザルトを使用)
    public Quiz(ResultSet results) throws SQLException {
        id             = results.getInt("id");
        subject_id     = results.getInt("subject_id");
        difficulty_id  = results.getInt("difficulty_id");
        explanation    = results.getString("explanation");
        question       = results.getString("question");
        answer         = results.getString("answer");
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
}
