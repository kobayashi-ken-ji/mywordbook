package jp.co.wordbook;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


// データベースのテーブル情報を、クラス化するための共通処理
interface DatabaseTable {

    // 実装クラスを生成
    public DatabaseTable create(ResultSet results) throws SQLException;


    // 実装クラスのリストを生成
    default List<? super DatabaseTable> createList(ResultSet results) {

        List<? super DatabaseTable> list = new ArrayList<>();

        try {
            // 実装先をインスタンス化
            while (results.next())
                list.add(create(results));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
