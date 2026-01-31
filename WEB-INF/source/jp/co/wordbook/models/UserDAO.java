package jp.co.wordbook.models;

import java.sql.*;
// import java.util.ArrayList;
import java.util.List;

/**
 * usersテーブルへアクセスするクラス
 */
public class UserDAO extends DataAccessObject<UserDTO> {

    // Entityを生成
    @Override
    protected UserDTO createEntity(ResultSet results) throws SQLException {

        return new UserDTO(
            results.getString("id"),
            results.getString("password")
            // results.getInt("active_subject_id"),
            // results.getInt("active_answered_count"),
            // results.getInt("active_question_lot_size"),
            // difficultyMaskToList(results.getInt("active_difficulty_mask"))
            // // results.getInt("active_difficulty_mask"),
        );
    }


    /**
     * ユーザーが居るか検索
     * @param id        ユーザーID
     * @param password  ログインパスワード
     * @return          引数に合致するユーザーがいるか否か
     */
    public boolean searchUser(String id, String password) {

        final String sql = "SELECT * FROM users WHERE id=? AND password=?";
        List<UserDTO> list = executeQuery(sql, id, password);

        return (!list.isEmpty());
    }
    

    // レコードからインスタンスを生成
    public UserDTO getRecord(String id) throws ParameterException {
    
        final String sql = "SELECT * FROM users WHERE id = ?";
        List<UserDTO> list = executeQuery(sql, id);

        if (list.isEmpty())
            throw new ParameterException("userレコードを取得できません。");

        return list.get(0);
    }

    // //-------------------------------------------------------------------------
    // // 難易度idの変換ユーティリティ   ビットマスク(DB側) ←→ List型(Java側)
    // //-------------------------------------------------------------------------

    // // 難易度idは 1~4 の4段階
    // public static final int DIFFICULTY_ID_LENGTH = 4;

    // /**
    //  * 難易度idを変換 (ビットマスク → List型)
    //  */
    // public static List<Integer> difficultyMaskToList(int difficultyMask) {

    //     final int mask = difficultyMask;
    //     List<Integer> difficultys = new ArrayList<Integer>();

    //     // 有効になっているidを取り出す
    //     for (int i=0; i<DIFFICULTY_ID_LENGTH; i++) {
    //         final int bit = (1 << i);

    //         // ビットが立っていれば有効なid (idは1から始まるため +1)
    //         if ((mask & bit) != 0)
    //             difficultys.add(i + 1);
    //     }

    //     return difficultys;
    // }

    // /**
    //  * 難易度idを変換 (List型 → ビットマスク)
    //  */
    // public static int difficultyListToMask(List<Integer> difficultys) {
        
    //     // Listがnullなら0を返す
    //     int mask = 0;
    //     if (difficultys == null) return mask;

    //     // idをビットに加算していく
    //     for (int difficulty : difficultys)
    //         mask |= (1 << (difficulty-1));

    //     return mask;
    // }
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


    // レコードの上書き
    // public boolean updateRecord(String id, String password) {

    //     String sql = "UPDATE users SET password = ? WHERE id = ?;";
    //     int rowCount = executeUpdate(sql, password, id);
    //     return (rowCount != 0);
    // }


    // レコードの削除
    // public boolean deleteRecord(int id) {

    //     String sql = "DELETE FROM users WHERE id = ?";
    //     int rowCount = executeUpdate(sql, id);
    //     return (rowCount != 0);
    // }
}