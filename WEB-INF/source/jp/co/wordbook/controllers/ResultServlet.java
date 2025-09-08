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
        // パラメータから取得
        String subjectname = request.getParameter("subjectname");
        int correctcount   = Integer.parseInt(request.getParameter("correctcount"));
        int quizcount      = Integer.parseInt(request.getParameter("quizcount"));
        int rate           = Integer.parseInt(request.getParameter("rate"));

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