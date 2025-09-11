package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題一覧ページ
@WebServlet("/quizlist")
public class QuizListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // 科目IDを取得
        int subject_id = Integer.parseInt(request.getParameter("subjectid"));

        // データベースから取得
        SubjectBean subject = new SubjectDAO().getRecord(subject_id);
        List<QuizBean> quizzes = new QuizDAO().getAllRecords(subject_id);

        // リクエストへ設定
        request.setAttribute("subject", subject);
        request.setAttribute("quizzes", quizzes);

        // JSPへ送信
        String view = "/WEB-INF/views/quizlist.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
