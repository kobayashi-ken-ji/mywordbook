package jp.co.wordbook;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ToQuizServlet  extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // パラメータから取得
        int subject_id = Integer.parseInt(request.getParameter("subjectid"));
        boolean isReverse = "reverse".equals(request.getParameter("format"));

        // チェックボタンは複数のため、配列で取得
        String[] difficulty_ids = request.getParameterValues("difficultyid");

        System.out.println(Arrays.toString(difficulty_ids));

        // データベースから取得
        List<Quiz> quizzes = Quiz.getRecords(subject_id, difficulty_ids);
        Subject subject = Subject.getRecord(subject_id);

        System.out.println(quizzes.size());

        // リクエストへ設定
        request.setAttribute("isreverse", isReverse);
        request.setAttribute("subjectname", subject.name);
        request.setAttribute("quizzes", quizzes);

        // JSPへ送信
        String view = "/WEB-INF/views/toquiz.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}