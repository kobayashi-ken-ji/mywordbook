package jp.co.wordbook.controllers;

import java.io.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import jp.co.wordbook.models.*;

// 科目リストページ
@WebServlet("/subjectlist")
public class SubjectListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // データベースから取得
        List<Subject> subjects = Subject.getRecords();

        // リクエストへ設定
        request.setAttribute("subjects", subjects);
        
        // JSPへ送信
        String view = "/WEB-INF/views/subjectlist.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}