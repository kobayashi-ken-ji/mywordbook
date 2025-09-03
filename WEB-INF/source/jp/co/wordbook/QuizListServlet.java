package jp.co.wordbook;

import java.io.*;
import java.sql.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class QuizListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        
        int subjectid = Integer.parseInt(request.getParameter("subjectid"));

        // データベースへ接続 / 処理実行
        ServletSupport.access(

            // 科目IDで検索を絞る
            "SELECT * FROM quizzes WHERE subject_id = ?",

            statement -> {
                try {
                    statement.setInt(1, subjectid);
                    
                    // SQLリザルト → Quizリスト化 → リクエストへ追加
                    ResultSet results = statement.executeQuery();
                    List<Quiz> quizzes = Quiz.createList(results);
                    request.setAttribute("quizzes", quizzes);
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        );

        ServletSupport.access(

            // 科目IDで検索を絞る
            "SELECT * FROM subjects WHERE id = ?",

            statement -> {
                try {
                    statement.setInt(1, subjectid);
                    
                    // SQLリザルト → 科目リスト化
                    ResultSet results = statement.executeQuery();
                    List<Subject> subjects = Subject.createList(results);

                    // 科目名を取得 → リクエストへ追加
                    String subjectName = subjects.get(0).name;
                    request.setAttribute("subjectname", subjectName);
                    
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        );

        // JSPへ送信
        ServletSupport.dispatch("/WEB-INF/views/quizlist.jsp", request, response);
    }
}
