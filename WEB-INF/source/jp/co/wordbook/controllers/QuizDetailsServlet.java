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

        // データベースから取得
        Quiz quiz = Quiz.getRecord(quiz_id);
        Subject subject = Subject.getRecord(quiz.subject_id);
        Difficulty difficulty = Difficulty.getRecord(quiz.difficulty_id);

        // リクエストへ設定
        request.setAttribute("quiz", quiz);
        request.setAttribute("subject_name", subject.name);
        request.setAttribute("difficulty_name", difficulty.name);

        // JSPへ送信
        String view = "/WEB-INF/views/quizdetails.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
