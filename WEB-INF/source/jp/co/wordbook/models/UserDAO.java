package jp.co.wordbook.models;

import java.sql.*;
import java.util.List;

/**
 * usersテーブルへアクセスするクラス
 */
public class UserDAO extends DataAccessObject<UserBean> {

    // Entityを生成
    @Override
    protected UserBean createEntity(ResultSet results) throws SQLException {

        return new UserBean(
            results.getString("id"),
            results.getString("password")
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
        List<UserBean> list = executeQuery(sql, id, password);

        return (!list.isEmpty());
    }
    

    // レコードからインスタンスを生成
    public UserBean getRecord(String id) {
    
        final String sql = "SELECT * FROM users WHERE id = ?";
        List<UserBean> list = executeQuery(sql, id);

        return (list.isEmpty())
            ? null
            : list.get(0);
    }


    // 全レコードからインスタンスリストを生成
    public List<UserBean> getAllRecords() {

        final String sql = "SELECT * FROM users";
        List<UserBean> list = executeQuery(sql);
        return list;
    }


    // レコードの新規作成
    public boolean insertRecord(String id, String password) {

        String sql = "INSERT INTO users(id, password) VALUES (?, ?)";
        int rowCount = executeUpdate(sql, id, password);
        return (rowCount != 0);
    }


    // レコードの上書き
    public boolean updateRecord(String id, String password) {

        String sql = "UPDATE users SET password = ? WHERE id = ?;";
        int rowCount = executeUpdate(sql, password, id);
        return (rowCount != 0);
    }


    // レコードの削除
    public boolean deleteRecord(int id) {

        String sql = "DELETE FROM users WHERE id = ?";
        int rowCount = executeUpdate(sql, id);
        return (rowCount != 0);
    }
}