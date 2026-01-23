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

    /**
     * レコードを取得 (ログインセキュア版)
     * @param quiz_id               問題ID
     * @param subject_id            科目ID (ユーザーIDと照合したもの)
     * @return                      レコード情報 (nullなし)
     * @throws ParameterException   レコードが取得できない場合に発生
     */
    public QuizBean getRecord(int quiz_id, int subject_id) throws ParameterException {

        String sql = "SELECT * FROM quizzes WHERE id=? AND subject_id=?;";
        List<QuizBean> list = executeQuery(sql, quiz_id);
        
        if (list.isEmpty())
            throw new ParameterException("Quizレコードが取得できません。");

        return list.get(0);
    }


    /**
     * レコードを取得
     * @param quiz_id               問題ID
     * @return                      レコード情報 (nullなし)
     * @throws ParameterException   レコードが取得できない場合に発生
     */
    public QuizBean getRecord(int quiz_id) throws ParameterException {

        String sql = "SELECT * FROM quizzes WHERE id = ?;";
        List<QuizBean> list = executeQuery(sql, quiz_id);
        
        if (list.isEmpty())
            throw new ParameterException("Quizレコードが取得できません。");

        return list.get(0);
    }


    /**
     * 全レコードからリストを生成
     * @param subject_id    科目ID (ユーザーIDと照合したもの)
     * @return              問題リスト (nullなし)
     */
    public List<QuizBean> getAllRecords(int subject_id) {

        final String sql = "SELECT * FROM quizzes WHERE subject_id = ?;";
        List<QuizBean> list = executeQuery(sql, subject_id);
        return list;
    }


    /**
     * 全レコードからリストを生成 (難易度を指定版)
     * @param subject_id        科目ID (ユーザーIDと照合したもの)
     * @param difficulty_ids    難易度IDの配列 (サーブレットのパラメータから取得)
     * @return                  難易度リスト (nullなし)
     */
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
     * @param quiz_id           問題ID, 新規作成は0を指定
     * @param subject_id        科目ID (ユーザーIDと照合したもの)
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

            // 自動生成されたIDを取得
            Long generatedId = (Long)executeInsert(
                1, sql, 
                subject_id, difficulty_id, explanation, question, answer
            );

            return (generatedId == null) ? 0 : generatedId.intValue();
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

    
    /**
     * レコードの削除
     * @param id    問題ID (ユーザーIDと科目が照合済みのもの)
     * @return      削除の成否
     */
    public boolean deleteRecord(int id) {

        String sql = "DELETE FROM quizzes WHERE id = ?";
        int rowCount = executeUpdate(sql, id);
        return (0 < rowCount);
    }


    /**
     * レコードの削除 (指定した科目のものを全て)
     * @param subject_id    科目ID (ユーザーIDと照合したもの)
     * @return              削除したレコード数 (0は削除失敗)
     */
    public int deleteRecords(int subject_id) {
    
        String sql = "DELETE FROM quizzes WHERE subject_id = ?";
        int rowCount = executeUpdate(sql, subject_id);
        return rowCount;
    }

    
    /**
     * 問題の難易度のみ変更
     * @param quiz_id       問題ID
     * @return              変更の成否
     */
    public boolean updateDifficulty(int quiz_id, int difficulty_id) {
    
        String sql = "UPDATE quizzes SET difficulty_id = ? WHERE id = ?;";
        int rowCount = executeUpdate(sql, difficulty_id, quiz_id);
        return (0 < rowCount);
    }
}
