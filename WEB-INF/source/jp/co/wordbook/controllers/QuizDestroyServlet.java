package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import jp.co.wordbook.models.*;

// 問題を削除 → 問題リストへ戻る
@WebServlet("/quizdestroy")
public class QuizDestroyServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        int quiz_id = Integer.parseInt(request.getParameter("id"));

        // データベースから取得、削除
        QuizDAO quizDAO = new QuizDAO();
        QuizBean quiz = quizDAO.getRecord(quiz_id);
        quizDAO.destroyRecord(quiz_id);

        // ページ遷移
        response.sendRedirect("./quizlist?subjectid=" + quiz.getSubject_id());
    }
}