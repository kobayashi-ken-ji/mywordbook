package jp.co.wordbook;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class QuizDetailsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        int quizid = Integer.parseInt(request.getParameter("quizid"));

        // データベースからクイズを取得、リクエストへセット
        Quiz.setQuizToRequest(quizid, "quiz", request);

        // JSPへ送信
        ServletSupport.dispatch("/WEB-INF/views/quizdetails.jsp", request, response);
    }
}
