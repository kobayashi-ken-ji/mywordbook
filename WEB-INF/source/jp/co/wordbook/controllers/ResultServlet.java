package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

// 結果ページ
@WebServlet("/result")
public class ResultServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        final int IRREG = -1;

        // パラメータから取得
        String subjectname = request.getParameter("subjectname");
        int correctcount   = NoNull.parseInt(request.getParameter("correctcount"), IRREG);
        int quizcount      = NoNull.parseInt(request.getParameter("quizcount"), IRREG);
        int rate           = NoNull.parseInt(request.getParameter("rate"), IRREG);

        // 不正な入力 → インフォメーションページへ
        if (subjectname  == null  ||
            correctcount == IRREG ||
            quizcount    == IRREG ||
            rate         == IRREG
        ) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // リクエストへ設定
        request.setAttribute("subjectname", subjectname);
        request.setAttribute("correctcount", correctcount);
        request.setAttribute("quizcount", quizcount);
        request.setAttribute("rate", rate);

        // JSPへ送信
        String view = "/WEB-INF/views/result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}