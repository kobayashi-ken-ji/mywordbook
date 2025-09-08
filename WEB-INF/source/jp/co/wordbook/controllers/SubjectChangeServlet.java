package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import jp.co.wordbook.models.*;

// 科目を新規作成、上書き、削除
@WebServlet("/subjectchange")
public class SubjectChangeServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");

        // パラメータ取得、インスタンス生成
        boolean isDelete    = "delete".equals(request.getParameter("button"));
        int     id          = Integer.parseInt(request.getParameter("subjectid"));
        String  name        = request.getParameter("subjectname");
        SubjectBean subject = new SubjectBean(id, name);

        // レコードから削除 (この科目のidが設定されている問題も削除)
        if (isDelete) {
            QuizBean.destroyRecords(subject.getId());
            subject.destroyRecord();
        }

        // レコードを 上書き or 挿入
        else subject.updateRecord();

        // ページ遷移
        response.sendRedirect("./subjectlist?edit=subject");
    }
}