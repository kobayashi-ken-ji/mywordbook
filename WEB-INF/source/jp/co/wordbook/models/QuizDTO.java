package jp.co.wordbook.models;

import java.io.*;

/**
 * quizテーブルの値転送用オブジェクト
 */
public class QuizDTO implements Serializable {

    private int    id;               // クイズID
    private int    subject_id;       // 科目ID
    private int    difficulty_id;    // 難易度
    private String explanation;      // 説明文
    private String question;         // 問題文
    private String answer;           // 正解文


    // コンストラクタ
    public QuizDTO() {}
    public QuizDTO(
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
}
