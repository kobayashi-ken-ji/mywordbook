package jp.co.wordbook;

import java.io.IOException;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.function.Consumer;

// サーブレットの汎用処理ユーティリティー
public class ServletSupport {
    
    // データベース情報
    private final static String URL      = "jdbc:mysql://localhost/mywordbook";
    private final static String USER     = "root";
    private final static String PASSWORD = "";


    // データベースへ接続 / 処理実行 / JSPへ送信
    static public void accessAndDispatch(
        String sql, Consumer<PreparedStatement> process, String view,
        HttpServletRequest request, HttpServletResponse response
    )
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

        // JSPへ送信
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
