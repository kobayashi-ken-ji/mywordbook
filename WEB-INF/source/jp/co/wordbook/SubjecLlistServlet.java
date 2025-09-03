package jp.co.wordbook;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class SubjecLlistServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // データベースへ接続 / 処理実行 / JSPへ送信
        ServletSupport.accessAndDispatch(

            "SELECT * FROM subjects",
            statement -> {
                try {
                    ResultSet results = statement.executeQuery();
                    
                    // SQLのリザルトからSubjectリストを生成
                    List<Subject> subjects = Subject.createList(results);

                    // リクエストに追加
                    request.setAttribute("subjects", subjects);
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            },
            
            "/WEB-INF/views/subjectlist.jsp",
            request, response
        );
    }
}