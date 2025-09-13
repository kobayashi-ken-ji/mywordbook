package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 結果ページ
@WebServlet("/result")
public class ResultServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String subjectname; 
        int correctcount;   
        int quizcount;      
        int rate;           

        // リクエストから取得
        try {
            subjectname  = Parameter.getString(request, "subjectname");
            correctcount = Parameter.getInt(request, "correctcount");
            quizcount    = Parameter.getInt(request, "quizcount");
            rate         = Parameter.getInt(request, "rate");
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
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