package jp.co.wordbook.models;
import java.io.*;
import java.util.List;

/**
 * 現在出題中の情報テーブルの値転送用オブジェクト
 */
public class QuizSettingDTO implements Serializable
{
    private String  userId;                 // ユーザーID
    private int     subjectId;              // 科目ID
    private int     lotSize;                // 一度に出題する問題数
    private boolean isSwapMode;             // 問題文と正解文を入れ替えるフラグ
    private int     answeredCount;          // 既出題数
    private List<Integer> difficultyIds;    // 設定中の難易度IDのリスト
    private boolean isSpeechEnabled;        // 読み上げ機能の 有効/無効
    private boolean isInputEnabled;         // ユーザー回答欄の 有効/無効

    // コンストラクタ
    public QuizSettingDTO() {}
    public QuizSettingDTO(
        String userId, int subjectId,
        int lotSize, boolean isSwapMode, int answeredCount, List<Integer> difficultyIds,
        boolean isSpeechEnabled, boolean isInputEnabled
    ) {
        this.userId          = userId;
        this.subjectId       = subjectId;
        this.lotSize         = lotSize;
        this.isSwapMode      = isSwapMode;
        this.answeredCount   = answeredCount;
        this.difficultyIds   = difficultyIds;
        this.isSpeechEnabled = isSpeechEnabled;
        this.isInputEnabled  = isInputEnabled;
    }

    // ゲッター
    public void setUserId(String userId)            { this.userId = userId; }
    public void setSubjectId(int subjectId)         { this.subjectId = subjectId; }
    public void setLotSize(int lotSize)             { this.lotSize = lotSize; }
    public void setIsSwapMode(boolean isSwapMode)   { this.isSwapMode = isSwapMode; }
    public void setAnsweredCount(int answeredCount) { this.answeredCount = answeredCount; }
    public void setDifficultyIds(List<Integer> difficultyIds) {
        this.difficultyIds = difficultyIds;
    }
    public void setIsSpeechEnabled(boolean isSpeechEnabled) { this.isSpeechEnabled = isSpeechEnabled; }
    public void setIsInputEnabled(boolean isInputEnabled)  { this.isInputEnabled = isInputEnabled; }

    // セッター
    public String getUserId()               { return userId; }
    public int getSubjectId()               { return subjectId; }
    public int getLotSize()                 { return lotSize; }
    public boolean getIsSwapMode()          { return isSwapMode; }
    public int getAnsweredCount()           { return answeredCount; }
    public List<Integer> getDifficultyIds() { return difficultyIds; }
    public boolean getIsSpeechEnabled()     { return isSpeechEnabled; }
    public boolean getIsInputEnabled()      { return isInputEnabled; }
}