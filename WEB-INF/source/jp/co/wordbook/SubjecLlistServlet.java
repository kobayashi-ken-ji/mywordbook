package jp.co.wordbook;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class SubjecLlistServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // データベースへ接続 / 処理実行 / JSPへ送信
        ServletSupport.access(

            "SELECT * FROM subjects",
            statement -> {
                try {
                    ResultSet results = statement.executeQuery();
                    
                    // SQLのリザルトからSubjectリストを生成
                    var subjects = (new Subject(0,null)).createList(results);

                    // リクエストに追加
                    request.setAttribute("subjects", subjects);
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        );

        // JSPへ送信
        ServletSupport.dispatch("/WEB-INF/views/subjectlist.jsp", request, response);
    }
}