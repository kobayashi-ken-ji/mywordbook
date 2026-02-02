package jp.co.wordbook.models;

import java.sql.*;
import java.util.*;

/**
 * DAOの共通処理
 * 継承先は、各テーブルのDAO
 * Tには子クラス用DTOを指定
 */
public abstract class DataAccessObject<T> {

    // T型(子クラス用DTO) のインスタンスを生成
    protected abstract T createEntity(ResultSet results) throws SQLException;

    //-------------------------------------------------------------------------
    // データベースへの接続/切断
    //-------------------------------------------------------------------------
 
    /**
     * データベースに接続
     * ※ try-with-resources 内での実行が必要
     */
    protected Connection getConnection() throws ClassNotFoundException, SQLException {

        // 現在の環境ではforName()が必須
        Class.forName(DBConstants.JDBC);
        return DriverManager.getConnection(
            DBConstants.URL, DBConstants.USER, DBConstants.PASSWORD);
    }

    /**
     * DB操作関数のインターフェース
     * apply()内で Connection.prepareStatement() を行い、DBを操作する
     */
    @FunctionalInterface
    interface DBOperation<R> {

        /**
         * DBを操作するメソッド
         * @param connection DB接続 (接続/切断はメソッド実行側が行う)
         * @return 実行側に渡す値
         * @throws Exception 全ての例外
         */
        R apply(Connection connection) throws Exception;
    }


    /**
     * DBへ接続し、引数のDB操作を実行する (トランザクションなし)
     * 例外発生時には、printStackTrace を行う
     * @param operation DB操作
     * @return  operation.apply() の戻り値 / 例外発生時はnull
     */
    protected <R> R execute(DBOperation<R> operation) {

        // DBへの接続、切断
        try (Connection connection = getConnection()) {

            // 受け取ったDB処理を実行
            return operation.apply(connection);
        }

        // DB接続、DB処理の両方の例外をキャッチ
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * DBへ接続し、引数のDB操作を実行する (トランザクションを使用)
     * 例外発生時には、ロールバックと printStackTrace を行う
     * @param operation  DB操作 (Connection.commit() の直前まで)
     * @return  operation.apply() の戻り値 / 例外発生時はnull
     */
    protected <R> R executeWithTransaction(DBOperation<R> operation) {

        // DBへの接続、切断
        try (Connection connection = getConnection()) {

            try {
                // トランザクションを開始
                connection.setAutoCommit(false);

                // 受け取ったDB処理を実行
                R result = operation.apply(connection);
                connection.commit();
                return result;
            }

            // 失敗時はロールバックして、変更を元に戻す
            catch (Exception e) {
                e.printStackTrace();
                connection.rollback();
                return null;
            }
        }
        
        // DB接続の例外処理
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
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

        return execute(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                // SQLの?に値を設定
                int index = 1;
                for (Object parameter : parameters)
                    statement.setObject(index++, parameter);

                // SQL実行
                ResultSet results = statement.executeQuery();

                // インスタンス化、リストに追加
                List<T> resultList = new ArrayList<>();
                while (results.next())
                    resultList.add(createEntity(results));

                return resultList;
            }
        });
    }


    /**
     * レコードを新規作成、上書き、削除
     * @param sql           SQL文
     * @param parameters    SQLの?に設定する値
     * @return              操作したレコード数
     */
    protected int executeUpdate(String sql, Object... parameters) {

        return execute(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                // SQLの?に値を設定
                int index = 1;
                for (Object parameter : parameters)
                    statement.setObject(index++, parameter);

                // SQL実行
                int rowCount = statement.executeUpdate();
                return rowCount;
            } 
        });
    }


    /**
     * 該当レコードの総数を取得 (SELECT COUNT 文)
     * @param sql           SQL文
     * @param parameters    SQLの?に設定する値
     * @return              レコード情報のリスト (nullなし)
     */
    protected int executeQueryGetCount(String sql, Object... parameters) {

        return execute(connection -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                // SQLの?に値を設定
                int index = 1;
                for (Object parameter : parameters)
                    statement.setObject(index++, parameter);

                // SQL実行
                ResultSet results = statement.executeQuery();

                // カウント数を取得
                return (results.next())
                    ? results.getInt(1)
                    : 0;
            }            
        });
    }


    /**
     * 複数レコードをアップデート (新規作成 | 上書き | 削除)
     * SQL文(１レコードの操作)を使い回し、複数回実行する
     * @param sql １レコードを操作するSQL文
     * @param parameters
     *      Object              : SQL文の?に渡す値
     *      List<Object>        : SQL1回分(1レコード分) をまとめたもの
     *      List<List<Object>>  : SQL複数回分をまとめたもの
     * @param expectedUpdateCount 期待される変更数 (SQL1回分)
     * @return 成否 (1つでも更新数が少なければfalse)
     */
    protected boolean executeUpdateBatch(
        String sql, List<List<Object>> parametersList, int expectedUpdateCount) {

        // DBへの接続、切断
        int[] updateCounts = executeWithTransaction(connection -> {

            // DBへの操作
            try (PreparedStatement statement = connection.prepareStatement(sql)) {

                // SQL文１回分 (１レコード操作分) のパラメータを取得
                for (List<Object> parameters : parametersList) {
                    
                    // SQL文の?に値を設定
                    int index = 1;
                    for (Object parameter : parameters)
                        statement.setObject(index++, parameter);

                    // バッチに追加
                    statement.addBatch();
                }

                // 一括で送信/反映
                return statement.executeBatch();
            }
        });

        // nullチェック
        if (updateCounts == null) return false;

        // 戻り値 : 期待変更数と各SQL結果が同じかどうか
        for (int updateCount : updateCounts) {
            if (updateCount != expectedUpdateCount) return false;
        }
        return true;
    }
    
    
    /**
     * レコードを新規作成し、自動生成値を取得
     * @param columnIndex   取得する、自動生成キー(IDなど) のカラムの番号
     * @param sql           SQL文
     * @param parameters    SQL文の?に設定する値
     * @return columnIndexの値 (Long, String などへのキャストが必要 / 例外時はnull)
     */
    protected Object executeInsert(int columnIndex, String sql, Object... parameters) {
        return execute(connection -> {
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

                // 指定されたキーの値を返す
                return (result.next())
                    ? result.getObject(columnIndex) // generatedValue
                    : null;
            }
        });
    }
}
