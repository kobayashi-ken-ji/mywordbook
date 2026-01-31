package jp.co.wordbook.models;
import java.io.*;
// import java.util.List;

/**
 * usersテーブルの値転送用オブジェクト
 */
public class UserDTO implements Serializable
{
    // ユーザーの基本情報
    private String id;
    private String password;

    // // 現在出題中の情報
    // private int activeSubjectId;         // 科目ID
    // private int activeAnsweredCount;     // 既出題数
    // private int activeQuestionLotSize;   // 一度に出題する問題数
    // private List<Integer> activeDifficultyIds;   // 設定中の難易度IDのリスト

    // コンストラクタ
    public UserDTO() {}
    public UserDTO(
        String id, String password
        // int activeSubjectId, int activeAnsweredCount, int activeQuestionLotSize,
        // List<Integer> activeDifficultyIds
    ) {
        this.id = id;
        this.password = password;
        // this.activeSubjectId       = activeSubjectId;
        // this.activeAnsweredCount   = activeAnsweredCount;
        // this.activeQuestionLotSize = activeQuestionLotSize;
        // this.activeDifficultyIds   = activeDifficultyIds;
    }

    // ゲッター
    public void setId(String id)             { this.id = id; }
    public void setPassword(String password) { this.password = password; }

    // public void setActiveSubjectId(int activeSubjectId) {
    //     this.activeSubjectId = activeSubjectId;
    // }

    // public void setActiveAnsweredCount(int activeAnsweredCount) {
    //     this.activeAnsweredCount = activeAnsweredCount;
    // }

    // public void setActiveQuestionLotSize(int activeQuestionLotSize) {
    //     this.activeQuestionLotSize = activeQuestionLotSize;
    // }

    // public void setActiveDifficultys(List<Integer> activeDifficultyIds) {
    //     this.activeDifficultyIds = activeDifficultyIds;
    // }
    

    // セッター
    public String getId()                 { return id; }
    public String getPassword()           { return password; }
    // public int getActiveSubjectId()       { return activeSubjectId; }
    // public int getActiveAnsweredCount()   { return activeAnsweredCount; }
    // public int getActiveQuestionLotSize() { return activeQuestionLotSize; }
    // public List<Integer> getActiveDifficultyIds() { return activeDifficultyIds; }
}