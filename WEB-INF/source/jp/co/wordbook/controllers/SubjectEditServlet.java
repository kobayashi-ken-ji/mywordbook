package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 科目編集ページ
@WebServlet("/subjectedit")
public class SubjectEditServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // セッションから取得
        String userId = Session.getUserId(request);

        boolean isNew;
        SubjectDTO subject;

        try {
            // リクエストから取得
            int subjectId = Parameter.getInt(request, "subjectid");

            // idが0なら新規作成
            isNew = (subjectId == 0);

            // データベースから取得 or 新規作成
            subject = (isNew)
                ? new SubjectDTO(0, "", "")
                : new SubjectDAO().getRecord(subjectId, userId);
        }
                
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // リクエストへ設定
        request.setAttribute("subject", subject);
        request.setAttribute("isNew", isNew);
        
        // JSPへ送信
        String view = "/WEB-INF/views/subjectedit.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}