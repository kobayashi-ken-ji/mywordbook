package jp.co.wordbook;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// トップページ
public class TopServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // データベースから取得
        List<Subject> subjects = Subject.getRecords();
        List<Difficulty> difficulties = Difficulty.getRecords();

        // リクエストへ設定
        request.setAttribute("subjects", subjects);
        request.setAttribute("difficulties", difficulties);

        // JSPへ送信
        String view = "/WEB-INF/views/top.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
