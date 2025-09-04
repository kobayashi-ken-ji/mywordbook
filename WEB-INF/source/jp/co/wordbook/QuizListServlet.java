package jp.co.wordbook;

import java.io.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class QuizListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // 科目IDを取得
        int subject_id = Integer.parseInt(request.getParameter("subjectid"));

        // データベースから取得
        Subject subject = Subject.getFromDatabase(subject_id);
        List<Quiz> quizzes = Quiz.getListFromDatabase(subject_id);

        // リクエストへ設定
        request.setAttribute("subjectname", subject.name);
        request.setAttribute("quizzes", quizzes);

        // JSPへ送信
        String view = "/WEB-INF/views/quizlist.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
