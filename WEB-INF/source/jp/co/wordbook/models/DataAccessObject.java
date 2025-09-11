package jp.co.wordbook.models;

import java.sql.*;
import java.util.*;

/**
 * DAOの共通処理
 * 継承先は、各テーブルのDAO
 * TにはBeanを指定
 */
public abstract class DataAccessObject<T> {

    private Connection connection;

    // データベース情報
    private static final String
        URL      = "jdbc:mysql://localhost/mywordbook",
        USER     = "root",
        PASSWORD = "",
        JDBC     = "com.mysql.jdbc.Driver";


    // T型のインスタンスを生成
    protected abstract T createEntity(ResultSet results) throws SQLException;

    //-------------------------------------------------------------------------
    // データベースへの接続/切断
    //-------------------------------------------------------------------------

    // データベースに接続
    protected void connect() {

        try {
            Class.forName(JDBC);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    // データベース接続を切断
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

    // レコードを取得
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


    // レコードを新規作成、上書き、削除
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
