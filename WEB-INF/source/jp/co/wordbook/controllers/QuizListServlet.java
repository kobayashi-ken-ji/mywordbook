package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題一覧ページ
@WebServlet("/quizlist")
public class QuizListServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // セッションから取得
        String userId = Session.getUserId(request);

        SubjectDTO subject;
        List<QuizDTO> quizzes;

        try {
            // リクエストから取得
            int subjectId = Parameter.getInt(request, "subjectid");

            // データベースから取得
            subject = new SubjectDAO().getRecord(subjectId, userId);  // ユーザーを照合
            quizzes = new QuizDAO().getAllRecords(subjectId);
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }        

        // リクエストへ設定
        request.setAttribute("subject", subject);
        request.setAttribute("quizzes", quizzes);

        // JSPへ送信
        String view = "/WEB-INF/views/quizlist.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
