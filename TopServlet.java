import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;

public class TopServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // データベース情報
        String url = "jdbc:mysql://localhost/mywordbook";
        String user = "root";
        String password = "";

        // JDBCドライバのチェック
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 科目を全て取得
        String sql = "SELECT * FROM subjects";

        try (
            // データベースに接続
            Connection connection = DriverManager.getConnection(url, user, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet results = statement.executeQuery();
        ) {
            // 科目List
            HashMap<Integer, String> subjects = new HashMap<>();

            // リストに追加
            while (results.next()) {
                 
                subjects.put(
                    (Integer)results.getInt("id"),
                    results.getString("name")
                );
            }

            // リクエストに追加
            request.setAttribute("subjects", subjects);

        } catch (Exception e) {
            System.out.println(e);
        }

        // JSPへ送信
        String view = "/WEB-INF/views/top.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
