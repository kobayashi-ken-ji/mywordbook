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
            results.getString("name")
        );
    }
    

    // レコードからインスタンスを生成
    public SubjectBean getRecord(int subject_id) {
    
        final String sql = "SELECT * FROM subjects WHERE id = ?";
        List<SubjectBean> list = executeQuery(sql, subject_id);

        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    public List<SubjectBean> getAllRecords() {

        final String sql = "SELECT * FROM subjects";
        List<SubjectBean> list = executeQuery(sql);
        return list;
    }


    // レコードの上書き(id>0)、新規作成(id==0)
    public int updateRecord(int id, String name) {

        // 新規作成
        if (id == 0) {
            String sql = "INSERT INTO subjects(name) VALUES (?)";
            return executeUpdate(sql, name);
        }

        // 上書き
        else {
            String sql = "UPDATE subjects SET name = ? WHERE id = ?;";
            return executeUpdate(sql, name, id);
        }
    }


    // レコードの削除
    public int destroyRecord(int id) {

        String sql = "DELETE FROM subjects WHERE id = ?";
        return executeUpdate(sql, id);
    }
}
