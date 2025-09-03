package jp.co.wordbook;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// データベースへの接続を簡易化するためのクラス
// ユーティリティー
public class AccessSupport {

    // コンストラクタ
    public AccessSupport(ResultSet results) {}


    // データベース情報
    private final static String URL      = "jdbc:mysql://localhost/mywordbook";
    private final static String USER     = "root";
    private final static String PASSWORD = "";


    // データベースへ接続 / 処理実行 / 切断
    public void access(String sql, Consumer<PreparedStatement> process)
        throws ServletException, IOException
    {
        // JDBCドライバの読み込み (必須)
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // データベースに接続
        try (
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            // 渡された処理を実行
            process.accept(statement);

        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    // SQLのリザルトからSubjectリストを生成
    public List<? super AccessSupport> createList(ResultSet results) {

        List<? super AccessSupport> list = new ArrayList<>();

        // 継承先のコンストラクタを使用
        try {
            while (results.next())
                list.add(new AccessSupport(results));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
