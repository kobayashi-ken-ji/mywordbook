package jp.co.wordbook.models;

import java.io.*;

/**
 * quizテーブルの値転送用オブジェクト
 */
public class QuizDTO implements Serializable {

    private int     id;             // クイズID
    private int     subjectId;      // 科目ID
    private int     difficultyId;   // 難易度
    private String  explanation;    // 説明文
    private String  question;       // 問題文
    private String  answer;         // 正解文
    private boolean isAsked;        // 出題済みフラグ


    // コンストラクタ
    public QuizDTO() {}
    public QuizDTO(
        int id, int subjectId, int difficultyId,
        String explanation, String question, String answer,
        boolean isAsked
    ) {
        this.id           = id;
        this.subjectId    = subjectId;
        this.difficultyId = difficultyId;
        this.explanation  = explanation;
        this.question     = question;
        this.answer       = answer;
        this.isAsked      = isAsked;
    }

    // セッター
    public void setId(int id)                      { this.id = id; }
    public void setSubjectId(int subjectId)        { this.subjectId = subjectId; }
    public void setDifficultyId(int difficultyId)  { this.difficultyId = difficultyId; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public void setQuestion(String question)       { this.question = question; }
    public void setAnswer(String answer)           { this.answer = answer; }
    public void setIsAsked(boolean isAsked)        { this.isAsked = isAsked; }

    // ゲッター
    public int getId()             { return id; }
    public int getSubjectId()      { return subjectId; }
    public int getDifficultyId()   { return difficultyId; }
    public String getExplanation() { return explanation; }
    public String getQuestion()    { return question; }
    public String getAnswer()      { return answer; }
    public boolean getIsAsked()    { return isAsked; }
}
