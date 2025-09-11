package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題詳細ページ
@WebServlet("/quizdetails")
public class QuizDetailsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        int quiz_id = Integer.parseInt(request.getParameter("quizid"));
        String state = request.getParameter("state");

        // 見出し
        String heading = ("update".equals(state))
            ? "問題を保存しました"
            : "問題詳細";

        // データベースから取得
        QuizBean quiz = new QuizDAO().getRecord(quiz_id);
        SubjectBean subject = new SubjectDAO().getRecord(quiz.getSubject_id());
        DifficultyBean difficulty = new DifficultyDAO().getRecord(quiz.getDifficulty_id());

        // リクエストへ設定
        request.setAttribute("heading", heading);
        request.setAttribute("quiz", quiz);
        request.setAttribute("subject_name", subject.getName());
        request.setAttribute("difficulty_name", difficulty.getName());

        // JSPへ送信
        String view = "/WEB-INF/views/quizdetails.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
