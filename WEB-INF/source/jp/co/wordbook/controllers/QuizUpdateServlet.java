package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題の新規作成、上書き
@WebServlet("/quizupdate")
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
        QuizDAO quizDAO = new QuizDAO();
        quiz_id = quizDAO.updateRecord(
            quiz_id, subject_id, difficulty_id, explanation, question, answer);
        
        // 保存に失敗 → インフォメーションを表示
        if (quiz_id == 0) {
            request.setAttribute("heading", "問題を保存できませんでした");
            request.setAttribute("paragraph", "");
            request.setAttribute("buttonname", "問題一覧へ");
            request.setAttribute("url", "quizlist?subjectid=" + subject_id);

            // JSPへ送信
            String view = "/WEB-INF/views/information.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(view);
            dispatcher.forward(request, response);
            return;
        }

        // ページ遷移
        response.sendRedirect("./quizdetails?quizid=" + quiz_id + "&state=update");
    }
}