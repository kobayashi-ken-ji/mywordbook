package jp.co.wordbook.models;

import java.sql.*;
import java.util.List;

/**
 * difficultesテーブルへアクセスするクラス
 */
public class DifficultyDAO extends DataAccessObject<DifficultyDTO> {

    // Entityを生成
    @Override
    protected DifficultyDTO createEntity(ResultSet results) throws SQLException {

        return new DifficultyDTO(
            results.getInt("id"),
            results.getString("name")
        );
    }
    

    /**
     * レコードからインスタンスを生成
     * @param difficulty_id         難易度ID
     * @return                      レコード情報 (nullなし)
     * @throws ParameterException   レコードが存在しない場合に発生
     */
    public DifficultyDTO getRecord(int difficulty_id) throws ParameterException {
    
        final String sql = "SELECT * FROM difficulties WHERE id = ?";
        List<DifficultyDTO> list = executeQuery(sql, difficulty_id);

        // 例外を投げる
        if (list.isEmpty())
            throw new ParameterException("Difficultyレコードが存在しません。");

        return list.get(0);
    }


    /**
     * テーブルからインスタンスリストを生成
     * @return 難易度リスト (nullなし)
     */
    public List<DifficultyDTO> getAllRecords() {

        final String sql = "SELECT * FROM difficulties";
        List<DifficultyDTO> list = executeQuery(sql);
        return list;
    }
}
