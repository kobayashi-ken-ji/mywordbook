package jp.co.wordbook.models;

import java.sql.*;
import java.util.List;

/**
 * difficultesテーブルへアクセスするクラス
 */
public class DifficultyDAO extends DataAccessObject<DifficultyBean> {

    // Entityを生成
    @Override
    protected DifficultyBean createEntity(ResultSet results) throws SQLException {

        return new DifficultyBean(
            results.getInt("id"),
            results.getString("name")
        );
    }
    

    // レコードからインスタンスを生成
    public DifficultyBean getRecord(int subject_id) {
    
        final String sql = "SELECT * FROM difficulties WHERE id = ?";
        List<DifficultyBean> list = executeQuery(sql, subject_id);

        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // テーブルからインスタンスリストを生成
    public List<DifficultyBean> getAllRecords() {

        final String sql = "SELECT * FROM difficulties";
        List<DifficultyBean> list = executeQuery(sql);
        return list;
    }
}
