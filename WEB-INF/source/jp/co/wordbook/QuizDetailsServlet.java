package jp.co.wordbook;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class QuizDetailsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        int quiz_id = Integer.parseInt(request.getParameter("quizid"));

        // データベースから取得
        Quiz quiz = Quiz.getFromDatabase(quiz_id);
        Subject subject = Subject.getFromDatabase(quiz.subject_id);

        // リクエストへ設定
        request.setAttribute("quiz", quiz);
        request.setAttribute("subject_name", subject.name);
        request.setAttribute("difficulty_name", "未実装");

        // JSPへ送信
        String view = "/WEB-INF/views/quizdetails.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
