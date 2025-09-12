package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
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
        // ユーザーIDを取得
        String userId = Session.getUserId(request);
        
        // データベースから取得
        SubjectDAO subjectDAO = new SubjectDAO();
        List<SubjectBean> subjects = subjectDAO.getAllRecords(userId);

        // 科目/問題 のどちらを編集するかで、JSPを変更する
        boolean isQuizEdit = "quiz".equals(request.getParameter("edit"));

        String heading = (isQuizEdit)
            ? "科目を選択してください"
            : "科目一覧";

        String paragraph = (isQuizEdit)
            ? "問題を編集します。"
            : "科目を編集します。";

        String url = (isQuizEdit)
            ? "quizlist"
            : "subjectedit";

        // リクエストへ設定
        request.setAttribute("subjects", subjects);
        request.setAttribute("isQuizEdit", isQuizEdit);
        request.setAttribute("heading", heading);
        request.setAttribute("paragraph", paragraph);
        request.setAttribute("url", url);
        
        // JSPへ送信
        String view = "/WEB-INF/views/subjectlist.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}