package jp.co.wordbook;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// 問題の新規作成、上書き
public class QuizUpdateServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");
        
        // パラメータから取得
        int    quiz_id       = Integer.parseInt(request.getParameter("quizid"));
        int    subject_id    = Integer.parseInt(request.getParameter("subjectid"));
        int    difficulty_id = Integer.parseInt(request.getParameter("difficultyid"));
        String question      = request.getParameter("question");
        String answer        = request.getParameter("answer");
        String explanation   = request.getParameter("explanation");

        // レコードを 上書き or 挿入
        Quiz quiz = new Quiz(quiz_id, subject_id, difficulty_id, explanation, question, answer);
        quiz.updateRecord();

        // ページ遷移
        response.sendRedirect("./quizdetails?quizid=" + quiz.id);
    }
}