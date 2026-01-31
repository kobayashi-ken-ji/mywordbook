package jp.co.wordbook.models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
// import java.util.stream.Collectors;

/**
 * usersテーブルへアクセスするクラス
 */
public class QuizSettingDAO extends DataAccessObject<QuizSettingDTO> {

    // Entityを生成
    @Override
    protected QuizSettingDTO createEntity(ResultSet results) throws SQLException {

        return new QuizSettingDTO(
            results.getString("user_id"),
            results.getInt("subject_id"),
            results.getInt("lot_size"),
            results.getBoolean("is_swap_mode"),
            results.getInt("answered_count"),
            difficultyMaskToList(results.getInt("difficulty_mask"))
        );
    }


    // レコードからインスタンスを生成
    public QuizSettingDTO getRecord(String id) throws ParameterException {
    
        final String sql = "SELECT * FROM user_quiz_settings WHERE user_id = ?";
        List<QuizSettingDTO> list = executeQuery(sql, id);

        if (list.isEmpty())
            throw new ParameterException("user_quiz_settingレコードを取得できません。");

        return list.get(0);
    }


    /** レコードの上書き (answered_count 以外) */
    public boolean updateRecord(
        String userId, int subjectId, boolean isSwapMode, int lotSize,
        int answeredCount, List<Integer> difficultyIds
    ) {
        // Integer → ビットマスク
        final int mask = difficultyListToMask(difficultyIds);

        String sql =
            "UPDATE user_quiz_settings " + 
            "SET subject_id = ?, difficulty_mask = ?, is_swap_mode = ?, lot_size = ?, answered_count = ? " +
            "WHERE user_id = ?;";

        int rowCount = 
            executeUpdate(sql, subjectId, mask, isSwapMode, lotSize, answeredCount, userId);
        return (rowCount != 0);
    }


    /** レコードの上書き (lot_size のみ) */
    public boolean updateLotSize(String userId, int lotSize) {

        String sql =
            "UPDATE user_quiz_settings " + 
            "SET lot_size = ? " +
            "WHERE user_id = ?;";

        int rowCount = executeUpdate(sql, lotSize, userId);
        return (rowCount != 0);
    }


    /** レコードの上書き (出題数を加算) */
    public boolean addAnsweredCount(String userId, int answeredCount) {

        String sql =
            "UPDATE user_quiz_settings " + 
            "SET answered_count = answered_count + ? " +
            "WHERE user_id = ?;";

        int rowCount = executeUpdate(sql, answeredCount, userId);
        return (rowCount != 0);
    }


    /** レコードの上書き (既出題数を0にする) */
    public boolean resetAnsweredCount(String userId) {

        String sql =
            "UPDATE user_quiz_settings " + 
            "SET answered_count = 0 " +
            "WHERE user_id = ?;";

        int rowCount = executeUpdate(sql, userId);
        return (rowCount != 0);
    }

    //-------------------------------------------------------------------------
    // 難易度idの変換ユーティリティ   ビットマスク(DB側) ←→ List型(Java側)
    //-------------------------------------------------------------------------

    // 難易度idは 1~4 の4段階
    public static final int DIFFICULTY_ID_LENGTH = 4;

    /**
     * 難易度idを変換 (ビットマスク → List型)
     */
    public static List<Integer> difficultyMaskToList(int difficultyMask) {

        final int mask = difficultyMask;
        List<Integer> difficultys = new ArrayList<Integer>();

        // 有効になっているidを取り出す
        for (int i=0; i<DIFFICULTY_ID_LENGTH; i++) {
            final int bit = (1 << i);

            // ビットが立っていれば有効なid (idは1から始まるため +1)
            if ((mask & bit) != 0)
                difficultys.add(i + 1);
        }

        return difficultys;
    }

    /**
     * 難易度idを変換 (List型 → ビットマスク)
     */
    public static int difficultyListToMask(List<Integer> difficultys) {
        
        // Listがnullなら0を返す
        int mask = 0;
        if (difficultys == null) return mask;

        // idをビットに加算していく
        for (int difficulty : difficultys)
            mask |= (1 << (difficulty-1));

        return mask;
    }
    
    // 全レコードからインスタンスリストを生成
    // public List<UserBean> getAllRecords() {

    //     final String sql = "SELECT * FROM users";
    //     List<UserBean> list = executeQuery(sql);
    //     return list;
    // }


    // レコードの新規作成
    // public boolean insertRecord(String id, String password) {

    //     String sql = "INSERT INTO users(id, password) VALUES (?, ?)";
    //     int rowCount = executeUpdate(sql, id, password);
    //     return (rowCount != 0);
    // }




    // レコードの削除
    // public boolean deleteRecord(int id) {

    //     String sql = "DELETE FROM users WHERE id = ?";
    //     int rowCount = executeUpdate(sql, id);
    //     return (rowCount != 0);
    // }
}