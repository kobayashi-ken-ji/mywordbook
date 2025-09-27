package jp.co.wordbook.models;

import java.util.*;
import java.io.*;

/**
 * quizテーブルのレコードを扱うクラス
 */
public class QuizBean implements Serializable {

    private int    id;               // クイズID
    private int    subject_id;       // 科目ID
    private int    difficulty_id;    // 難易度
    private String explanation;      // 説明文
    private String question;         // 問題文
    private String answer;           // 正解文


    // コンストラクタ
    public QuizBean() {}
    public QuizBean(
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

    // セッター
    public void setId(int id)                       { this.id = id; }
    public void setSubject_id(int subject_id)       { this.subject_id = subject_id; }
    public void setDifficulty_id(int difficulty_id) { this.difficulty_id = difficulty_id; }
    public void setExplanation(String explanation)  { this.explanation = explanation; }
    public void setQuestion(String question)        { this.question = question; }
    public void setAnswer(String answer)            { this.answer = answer; }

    // ゲッター
    public int getId()              { return id; }
    public int getSubject_id()      { return subject_id; }
    public int getDifficulty_id()   { return difficulty_id; }
    public String getExplanation()  { return explanation; }
    public String getQuestion()     { return question; }
    public String getAnswer()       { return answer; }
    
    // idを文字列で取得
    public String getIdString() {
        return (id == 0) 
            ? "- 新規作成 -"
            : Integer.toString(id);
    }

    //-------------------------------------------------------------------------
    // 出題時の処理
    //-------------------------------------------------------------------------

    /**
     * 問題文と正解文を入れ替える (出題方式の変更用)
     */
    public void swapQuestionAndAnswer() {

        String tmp = question;
        question = answer;
        answer = tmp;
    }


    /**
     * インスタンスリストからJSONテキストを生成
     * @param quizzes   JSON化する問題のリスト
     * @return          JSONテキスト (nullなし)
     */
    public static String getJson(List<QuizBean> quizzes) {

        final String DELIM = ", ";
        final String QUOUT = "\"";
        StringJoiner joiner = new StringJoiner(",\n");

        // JSONへ変換
        for (QuizBean quiz : quizzes) {

            // "[クイズid, 難易度id, 問題文, 正解文, 説明文]"
            joiner.add(
                "    [" +
                    quiz.id + DELIM +
                    quiz.difficulty_id + DELIM +
                    QUOUT + quiz.question + QUOUT + DELIM +
                    QUOUT + quiz.answer + QUOUT + DELIM +
                    QUOUT + quiz.explanation.replace("\n", "\\n") + QUOUT +
                "]"
            );
        }

        return "[\n" + joiner.toString() + "\n]";
    }
}
