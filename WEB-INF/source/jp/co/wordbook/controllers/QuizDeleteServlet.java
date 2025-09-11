package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題を削除 → インフォメーションページへ
@WebServlet("/quizdelete")
public class QuizDeleteServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        int quiz_id = Integer.parseInt(request.getParameter("id"));

        // データベースから取得、削除
        QuizDAO quizDAO = new QuizDAO();
        QuizBean quiz = quizDAO.getRecord(quiz_id);
        boolean success = quizDAO.deleteRecord(quiz_id);
        
        // 見出し
        String heading = (success)
            ? "問題を削除しました"
            : "問題を削除できませんでした";

        // リクエストへ設定
        request.setAttribute("heading", heading);
        request.setAttribute("paragraph", "問題文 : " + quiz.getQuestion());
        request.setAttribute("buttonname", "問題一覧へ");
        request.setAttribute("url", "quizlist?subjectid=" + quiz.getSubject_id());

        // JSPへ送信
        String view = "/WEB-INF/views/information.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}