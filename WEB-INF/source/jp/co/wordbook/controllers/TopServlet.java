package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// トップページ
@WebServlet("/top")
public class TopServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // セッションから取得
        String userId = Session.getUserId(request);

        // データベースから取得
        SubjectDAO subjectDAO = new SubjectDAO();
        DifficultyDAO difficultyDAO = new DifficultyDAO();

        List<SubjectBean> subjects = subjectDAO.getAllRecords(userId);  // ユーザーを照合
        List<DifficultyBean> difficulties = difficultyDAO.getAllRecords();

        // リクエストへ設定
        request.setAttribute("nosubjects", (subjects.size() == 0));
        request.setAttribute("subjects", subjects);
        request.setAttribute("difficulties", difficulties);

        // JSPへ送信
        String view = "/WEB-INF/views/top.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
