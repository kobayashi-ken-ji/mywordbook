package jp.co.wordbook.models;

import java.sql.*;
import java.util.*;

/**
 * DAOの共通処理
 * 継承先は、各テーブルのDAO
 * TにはEntityを指定
 */
public abstract class DataAccessObject<T> {

    private Connection connection;

    // データベース情報
    // [!] LinuxではURLにUTF8と明記する必要がある
    private static final String
        URL      = "jdbc:mysql://localhost/mywordbook?useUnicode=true&characterEncoding=UTF-8",
        USER     = "root",
        PASSWORD = "",
        JDBC     = "com.mysql.jdbc.Driver";

    // T型のインスタンスを生成
    protected abstract T createEntity(ResultSet results) throws SQLException;

    //-------------------------------------------------------------------------
    // データベースへの接続/切断
    //-------------------------------------------------------------------------

    /**
     * データベースに接続
     * 使用後は必ず disconnect() を実行
     */
    protected void connect() {

        try {
            Class.forName(JDBC);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * データベース接続を切断
     */
    protected void disconnect() {

        try {
            if (connection != null)
                connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //-------------------------------------------------------------------------
    // SQLの実行
    //-------------------------------------------------------------------------

    /**
     * レコードを取得
     * @param sql           SQL文
     * @param parameters    SQLの?に設定する値
     * @return              レコード情報のリスト (nullなし)
     */
    protected List<T> executeQuery(String sql, Object... parameters) {

        List<T> list = new ArrayList<>();
        connect();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            // SQLの?に値を設定
            int index = 1;
            for (Object parameter : parameters)
                statement.setObject(index++, parameter);

            // SQL実行
            ResultSet results = statement.executeQuery();

            // インスタンス化、リストに追加
            while (results.next())
                list.add(createEntity(results));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return list;
    }


    /**
     * レコードを新規作成、上書き、削除
     * @param sql           SQL文
     * @param parameters    SQLの?に設定する値
     * @return              操作したレコード数
     */
    protected int executeUpdate(String sql, Object... parameters) {

        int rowCount = 0;
        connect();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            // SQLの?に値を設定
            int index = 1;
            for (Object parameter : parameters)
                statement.setObject(index++, parameter);

            // SQL実行
            rowCount = statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return rowCount;
    }


    /**
     * 該当レコードの総数を取得 (SELECT COUNT 文)
     * @param sql           SQL文
     * @param parameters    SQLの?に設定する値
     * @return              レコード情報のリスト (nullなし)
     */
    protected int executeQueryGetCount(String sql, Object... parameters) {

        int count = 0;
        connect();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            // SQLの?に値を設定
            int index = 1;
            for (Object parameter : parameters)
                statement.setObject(index++, parameter);

            // SQL実行
            ResultSet results = statement.executeQuery();

            // カウント数を取得
            if (results.next())
                count = results.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return count;
    }

    // /**
    //  * レコードを新規作成、上書き、削除 (一括更新版)
    //  * @param sql           SQL文
    //  * @param parameters    SQLの?に設定する値
    //  * @return              操作したレコード数
    //  */
    // protected void executeUpdateBatch(String sql, List<T> entities, Object... parameters) {

    //     connect();

    //     try (PreparedStatement statement = connection.prepareStatement(sql)) {

    //         // トランザクションを開始
    //         connection.setAutoCommit(false);

    //         // エンティティ(DTO) 全て
    //         for (T entity : entities) {
                
    //             // SQLの?に値を設定
    //             int index = 1;
    //             for (Object parameter : parameters)
    //                 statement.setObject(index++, parameter);

    //             // バッチに追加
    //             statement.addBatch();
    //         }

    //         // 一括で送信/反映
    //         statement.executeBatch();
    //         connection.commit();

    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }

    //     disconnect();
    // }
    
    
    /**
     * レコードを新規作成
     * @param columnIndex   自動生成されるカラムの番号
     * @param sql           SQL文
     * @param parameters    SQL文の?に設定する値
     * @return 最初に自動生成されたキー値を取得 (Long, String などへのキャストが必要)
     */
    protected Object executeInsert(int columnIndex, String sql, Object... parameters) {

        Object generatedValue = null;
        connect();

        try (
            PreparedStatement statement = 
                connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            // SQLの?に値を設定
            int index = 1;
            for (Object parameter : parameters)
                statement.setObject(index++, parameter);

            // SQL実行
            statement.executeUpdate();

            // 自動生成されたキー値を取得
            ResultSet result = statement.getGeneratedKeys();
            if (result.next())
                generatedValue = result.getObject(columnIndex);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
        return generatedValue;
    }
}
