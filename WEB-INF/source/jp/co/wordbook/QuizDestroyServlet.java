package jp.co.wordbook;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// 問題を削除 → 問題リストへ戻る
public class QuizDestroyServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        int quiz_id = Integer.parseInt(request.getParameter("id"));

        // データベースから取得
        Quiz quiz = Quiz.getRecord(quiz_id);
        quiz.destroyRecord();

        // ページ遷移
        response.sendRedirect("./quizlist?subjectid=" + quiz.subject_id);
    }
}