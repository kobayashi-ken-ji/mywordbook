package jp.co.wordbook;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * データベースへの共通処理
 * 実装先 : DBのテーブル用クラス
 */
interface DatabaseAccess<T> {

    // 実装側のインスタンスを生成
    abstract T constructor(ResultSet results)  throws SQLException;


    /**
     * インスタンスのリストを生成
     * @param sql     SQL文
     * @param numbers SQLの?部分に代入する数値 省略可
     */
    default List<T> createList(String sql, int... numbers)
        throws ServletException, IOException
    {
        List<T> list = new ArrayList<>();
        access(sql, statement -> {
            try {
                // SQLの?に値を設定
                int parameterIndex = 1;
                for (int number : numbers)
                    statement.setInt(parameterIndex++, number);

                // SQL実行
                ResultSet results = statement.executeQuery();

                // インスタンス化、リストに追加
                while (results.next())
                    list.add(constructor(results));
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return list;
    }


    /**
     * データベースへ接続 / 処理実行 / 切断
     * @param sql       SQL文
     * @param process   SQLの?へ値を設定、SQL実行、値の取得 などの処理
     */
    default void access(String sql, Consumer<PreparedStatement> process)
        throws ServletException, IOException
    {
        // データベース情報
        final String URL      = "jdbc:mysql://localhost/mywordbook";
        final String USER     = "root";
        final String PASSWORD = "";

        // JDBCドライバの読み込み (必須)
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // データベースに接続
        // RETURN_GENERATED_KEYS で新規作成したIDを取得できるようになる
        try (
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {
            // 渡された処理を実行
            process.accept(statement);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

