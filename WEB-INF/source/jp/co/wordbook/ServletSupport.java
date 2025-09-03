package jp.co.wordbook;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Consumer;

// データベースへの接続を簡易化するためのクラス
// ユーティリティー
public class ServletSupport {

    // データベース情報
    private final static String URL      = "jdbc:mysql://localhost/mywordbook";
    private final static String USER     = "root";
    private final static String PASSWORD = "";


    // データベースへ接続 / 処理実行 / 切断
    public static void access(String sql, Consumer<PreparedStatement> process)
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


    // JSPへ送信
    public static void dispatch
        (String view, HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
