package jp.co.wordbook;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// 科目を新規作成、上書き、削除
public class SubjectChangeServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");

        // パラメータ取得、インスタンス生成
        boolean isDelete = "delete".equals(request.getParameter("button"));
        int     id       = Integer.parseInt(request.getParameter("subjectid"));
        String  name     = request.getParameter("subjectname");
        Subject subject  = new Subject(id, name);

        // レコードから削除 (今回の科目が設定されている問題も削除)
        if (isDelete) {
            Quiz.destroyRecords(subject.id);
            subject.destroyRecord();
        }

        // レコードを 上書き or 挿入
        else subject.updateRecord();

        // ページ遷移
        response.sendRedirect("./subjectlist?edit=subject");
    }
}