package jp.co.wordbook.models;

import java.sql.*;
import java.util.List;

/**
 * subjectsテーブルへアクセスするクラス
 */
public class SubjectDAO extends DataAccessObject<SubjectDTO> {

    // Entityを生成
    @Override
    protected SubjectDTO createEntity(ResultSet results) throws SQLException {

        return new SubjectDTO(
            results.getInt("id"),
            results.getString("name"),
            results.getString("user_id")
        );
    }
    

    /**
     * 科目とユーザーが紐づいているかを確認
     * @param subject_id            科目ID
     * @param userId               セッションから取得したユーザーID
     * @return                      必ずtrue (falseの場合は例外が発生)
     * @throws ParameterException   科目とユーザーが紐づいていない場合
     */
    public boolean userHasSubject(int subject_id, String userId) 
        throws ParameterException
    {
        return (getRecord(subject_id, userId) != null);
    }


    /**
     * レコードからインスタンスを生成
     * @param subject_id            科目ID
     * @param userId               ユーザーID
     * @return                      レコードの情報 (nullなし)
     * @throws ParameterException   レコードが取得できない場合
     */
    public SubjectDTO getRecord(int subject_id, String userId) 
        throws ParameterException
    {
        final String sql = "SELECT * FROM subjects WHERE id=? AND user_id=?;";
        List<SubjectDTO> list = executeQuery(sql, subject_id, userId);

        if (list.isEmpty())
            throw new ParameterException("Subjectレコードが取得できません。");

        return list.get(0);
    }


    /**
     * テーブルからインスタンスリストを生成
     * @param userId   ユーザーID
     * @return          科目リスト (nullなし)
     */
    public List<SubjectDTO> getAllRecords(String userId) {

        final String sql = "SELECT * FROM subjects WHERE user_id = ?";
        List<SubjectDTO> list = executeQuery(sql, userId);
        return list;
    }


    /**
     * レコードの上書き、新規作成
     * @param id        科目ID (新規作成の場合は0)
     * @param name      科目名
     * @param userId   ユーザーID
     * @return          書き込みの成否
     */
    public boolean updateRecord(int id, String name, String userId) {

        int rowCount = 0;

        // 新規作成
        if (id == 0) {
            String sql = "INSERT INTO subjects(name, user_id) VALUES (?, ?)";
            rowCount = executeUpdate(sql, name, userId);
        }

        // 上書き
        else {
            String sql = "UPDATE subjects SET name = ? WHERE id=? AND user_id=?;";
            rowCount = executeUpdate(sql, name, id, userId);
        }

        return (rowCount != 0);
    }


    /**
     * レコードを削除
     * @param id        科目ID (新規作成の場合は0)
     * @param user_id   ユーザーID
     * @return          削除の成否
     */
    public boolean deleteRecord(int id, String userId) {

        String sql = "DELETE FROM subjects WHERE id=? AND user_id=?;";
        int rowCount = executeUpdate(sql, id, userId);
        return (rowCount != 0);
    }
}
