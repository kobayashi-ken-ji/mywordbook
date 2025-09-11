package jp.co.wordbook.models;

import java.util.*;
import java.sql.*;

/**
 * quizzesテーブルへアクセスするクラス
 */
public class QuizDAO extends DataAccessObject<QuizBean> {
    
    // Entityを生成
    @Override
    protected QuizBean createEntity(ResultSet results) throws SQLException {

        return new QuizBean(
            results.getInt("id"),
            results.getInt("subject_id"),
            results.getInt("difficulty_id"),
            results.getString("explanation"),
            results.getString("question"),
            results.getString("answer")
        );
    }

    //-------------------------------------------------------------------------
    // レコードを取得
    //-------------------------------------------------------------------------

    // レコードからEntityを生成
    public QuizBean getRecord(int quiz_id) {
    
        String sql = "SELECT * FROM quizzes WHERE id = ?;";
        List<QuizBean> list = executeQuery(sql, quiz_id);
        
        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // 全レコードからEntityリストを生成
    public List<QuizBean> getAllRecords(int subject_id)
    {
        final String sql = "SELECT * FROM quizzes WHERE subject_id = ?;";
        List<QuizBean> list = executeQuery(sql, subject_id);
        return list;
    }


    // 全レコードからEntityリストを生成 (難易度を指定版)
    public List<QuizBean> getAllRecords(int subject_id, String[] difficulty_ids) {

        // SQLの検索条件を作成
        //      クイズ難易度は複数選択できる
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

        List<QuizBean> list = executeQuery(sql, subject_id);
        return list;
    }

    //-------------------------------------------------------------------------
    // レコードの 新規作成、上書き、削除
    //-------------------------------------------------------------------------

    /**
     * レコードの上書き、新規作成
     * @param quiz_id           問題id, 新規作成は0を指定
     * @param subject_id        科目id
     * @param difficulty_id     難易度id
     * @param explanation       説明文
     * @param question          問題文
     * @param answer            正解文
     * @return 問題id (0は失敗, 新規作成時は自動生成されたid)
     */
    public int updateRecord(
        int quiz_id, int subject_id, int difficulty_id,
        String explanation, String question, String answer) {

        // 改行コードを LF に限定
        explanation = explanation.replace("\r\n", "\n");

        // 新規作成
        if (quiz_id == 0) {
            String sql = 
                "INSERT INTO quizzes(subject_id, difficulty_id, explanation, question, answer) " +
                "VALUES (?, ?, ?, ?, ?)";

            List<Integer> list = executeInsert(
                1, sql, 
                subject_id, difficulty_id, explanation, question, answer
            );

            // 自動生成されたIDを返す
            return (!list.isEmpty())
                ? list.get(0)
                : 0;
        }

        // 上書き
        else {
            String sql = 
                "UPDATE quizzes " +
                "SET subject_id=?, difficulty_id=?, explanation=?, question=?, answer=? " +
                "WHERE id = ?;";

            int rowCount = executeUpdate(
                sql, subject_id, difficulty_id, explanation, question, answer, quiz_id);

            return (0 < rowCount) ? quiz_id : 0;
        }
    }

    // レコードの削除
    public boolean destroyRecord(int id) {

        String sql = "DELETE FROM quizzes WHERE id = ?";
        int rowCount = executeUpdate(sql, id);
        return (0 < rowCount);
    }


    // レコードの削除 (指定した科目のものを全て)
    public int destroyRecords(int subject_id) {
    
        String sql = "DELETE FROM quizzes WHERE subject_id = ?";
        int rowCount = executeUpdate(sql, subject_id);
        return rowCount;
    }

    
    // 難易度のみ変更
    public boolean updateDifficulty(int quiz_id, int difficulty_id) {
    
        String sql = "UPDATE quizzes SET difficulty_id = ? WHERE id = ?;";
        int rowCount = executeUpdate(sql, difficulty_id, quiz_id);
        return (0 < rowCount);
    }
}
