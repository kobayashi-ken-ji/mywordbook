package jp.co.wordbook;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// 科目編集ページ
public class SubjectEditServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        int subjectid = Integer.parseInt(request.getParameter("subjectid"));

        // データベースから取得 or 新規作成
        Subject subject = (subjectid == 0)
            ? new Subject(0, "")
            : Subject.getRecord(subjectid);

        // リクエストへ設定
        request.setAttribute("subject", subject);
        
        // JSPへ送信
        String view = "/WEB-INF/views/subjectedit.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}