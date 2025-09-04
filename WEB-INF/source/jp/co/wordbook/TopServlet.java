package jp.co.wordbook;

import java.io.*;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class TopServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // データベースから取得
        List<Subject> subjects = Subject.getListFromDatabase();

        // リクエストへ設定
        request.setAttribute("subjects", subjects);

        // JSPへ送信
        String view = "/WEB-INF/views/top.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
