package jp.co.wordbook.models;

import java.sql.*;
import java.util.List;

/**
 * subjectsテーブルへアクセスするクラス
 */
public class SubjectDAO extends DataAccessObject<SubjectBean> {

    // Entityを生成
    @Override
    protected SubjectBean createEntity(ResultSet results) throws SQLException {

        return new SubjectBean(
            results.getInt("id"),
            results.getString("name"),
            results.getString("user_id")
        );
    }
    

    /**
     * 科目とユーザーが紐づいているかを確認
     * @param subject_id    科目ID
     * @param user_id       セッションから取り出したユーザーID
     * @return              ユーザーはこの科目を持っているか否か
     */
    public boolean userHasSubject(int subject_id, String user_id) {
        
        return (getRecord(subject_id, user_id) != null);
    }


    /**
     * レコードからインスタンスを生成
     * @param subject_id    科目ID
     * @param user_id       ユーザーID
     * @return              レコードのデータ (存在しない場合はnull)
     */
    public SubjectBean getRecord(int subject_id, String user_id) {
    
        final String sql = "SELECT * FROM subjects WHERE id=? AND user_id=?;";
        List<SubjectBean> list = executeQuery(sql, subject_id, user_id);

        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    public List<SubjectBean> getAllRecords(String user_id) {

        final String sql = "SELECT * FROM subjects WHERE user_id = ?";
        List<SubjectBean> list = executeQuery(sql, user_id);
        return list;
    }


    /**
     * レコードの上書き、新規作成
     * @param id        科目ID (新規作成の場合は0)
     * @param name      科目名
     * @param user_id   ユーザーID
     * @return          レコードへの書き込みの成否
     */
    public boolean updateRecord(int id, String name, String user_id) {

        int rowCount = 0;

        // 新規作成
        if (id == 0) {
            String sql = "INSERT INTO subjects(name, user_id) VALUES (?, ?)";
            rowCount = executeUpdate(sql, name, user_id);
        }

        // 上書き
        else {
            String sql = "UPDATE subjects SET name = ? WHERE id=? AND user_id=?;";
            rowCount = executeUpdate(sql, name, id, user_id);
        }

        return (rowCount != 0);
    }


    // レコードの削除
    public boolean deleteRecord(int id, String user_id) {

        String sql = "DELETE FROM subjects WHERE id=? AND user_id=?;";
        int rowCount = executeUpdate(sql, id, user_id);
        return (rowCount != 0);
    }
}
