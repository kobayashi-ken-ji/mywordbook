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
        // セッション、パラメータから取得
        String userId = Session.getUserId(request);
        int subject_id = NoNull.parseInt(request.getParameter("subjectid"), -1);

        // データベースから取得
        SubjectBean subject = new SubjectDAO().getRecord(subject_id, userId);
        List<QuizBean> quizzes = new QuizDAO().getAllRecords(subject_id);

        // 科目とユーザーが紐づいていない → 不正入力のインフォメーションページへ
        if (subject == null) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // リクエストへ設定
        request.setAttribute("subject", subject);
        request.setAttribute("quizzes", quizzes);

        // JSPへ送信
        String view = "/WEB-INF/views/quizlist.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
