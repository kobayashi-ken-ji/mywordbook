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
    public boolean updateRecord(int id, String name) {

        int rowCount = 0;

        // 新規作成
        if (id == 0) {
            String sql = "INSERT INTO subjects(name) VALUES (?)";
            rowCount = executeUpdate(sql, name);
        }

        // 上書き
        else {
            String sql = "UPDATE subjects SET name = ? WHERE id = ?;";
            rowCount = executeUpdate(sql, name, id);
        }

        return (rowCount != 0);
    }


    // レコードの削除
    public boolean deleteRecord(int id) {

        String sql = "DELETE FROM subjects WHERE id = ?";
        int rowCount = executeUpdate(sql, id);
        return (rowCount != 0);
    }
}
