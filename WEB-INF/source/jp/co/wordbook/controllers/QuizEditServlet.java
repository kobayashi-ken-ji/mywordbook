package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題編集ページ
@WebServlet("/quizedit")
public class QuizEditServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // パラメータから取得
        int quiz_id    = Integer.parseInt(request.getParameter("id"));
        int subject_id = Integer.parseInt(
            nonNull(request.getParameter("subjectid"), "0"));

        // データベースから取得
        List<SubjectBean>    subjects     = new SubjectDAO().getAllRecords();
        List<DifficultyBean> difficulties = new DifficultyDAO().getAllRecords();
        
        // 問題を新規作成 or 編集
        QuizBean quiz = (quiz_id < 1)
            ? new QuizBean(0, subject_id, 2, "","", "")
            : new QuizDAO().getRecord(quiz_id);

        // リクエストへ設定
        request.setAttribute("quiz", quiz);
        request.setAttribute("subjects", subjects);
        request.setAttribute("difficulties", difficulties);

        // JSPへ送信
        String view = "/WEB-INF/views/quizedit.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }

    
    // nullの場合は指定値を返す
    private <T> T nonNull(T value, T valueWhenNull) {
        return (value != null) ? value : valueWhenNull;
    }
}

