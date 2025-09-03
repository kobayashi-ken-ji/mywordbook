package jp.co.wordbook;

import java.io.*;
import java.sql.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class TopServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // データベースへ接続 / 処理実行
        ServletSupport.access(
            "SELECT * FROM subjects",
            statement -> {
                try {
                    // SQLリザルト → Subjectリスト化 → リクエストへ追加
                    ResultSet results = statement.executeQuery();
                    List<Subject> subjects = Subject.createList(results);
                    request.setAttribute("subjects", subjects);
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        );

        // JSPへ送信
        ServletSupport.dispatch("/WEB-INF/views/top.jsp", request, response);
    }
}
