package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 科目を新規作成、上書き、削除
@WebServlet("/subjectupdate")
public class SubjectUpdateServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");

        // パラメータ取得、インスタンス生成
        boolean isDelete    = "delete".equals(request.getParameter("button"));
        int     subject_id  = Integer.parseInt(request.getParameter("subjectid"));
        String  name        = request.getParameter("subjectname");


        // 見出し
        String heading = "";

        // レコードから削除 (この科目が設定されている問題も削除)
        if (isDelete) {
            new QuizDAO().deleteRecords(subject_id);
            boolean success = new SubjectDAO().deleteRecord(subject_id);
            heading = (success)
                ? "科目を削除しました"
                : "科目を削除できませんでした";
        }

        // レコードを 上書き or 挿入
        else {
            boolean success = new SubjectDAO().updateRecord(subject_id, name);
            heading = (success)
                ? "科目を保存しました"
                : "科目を保存できませんでした";
        }

        // リクエストへ設定
        request.setAttribute("heading", heading);
        request.setAttribute("paragraph", "科目名 : " + name);
        request.setAttribute("buttonname", "科目一覧へ");
        request.setAttribute("url", "subjectlist?edit=subject");

        // JSPへ送信
        String view = "/WEB-INF/views/information.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}