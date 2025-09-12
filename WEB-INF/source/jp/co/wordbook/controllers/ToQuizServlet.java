package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 出題ページ
@WebServlet("/toquiz")
public class ToQuizServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // ユーザーIDを取得
        String userId = Session.getUserId(request);

        // パラメータから取得
        String[] difficulty_ids = request.getParameterValues("difficultyids");
        String format           = request.getParameter("format");
        String subjectIdString  = request.getParameter("subjectid");

        // 不正な入力 → インフォメーションページへ
        if (difficulty_ids == null  ||
            subjectIdString == null ||
            format == null
        ) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // データベースから取得
        int subject_id = Integer.parseInt(subjectIdString);
        SubjectBean subject = new SubjectDAO().getRecord(subject_id, userId);
        List<QuizBean> quizzes = new QuizDAO().getAllRecords(subject_id, difficulty_ids);
        List<DifficultyBean> difficulties = new DifficultyDAO().getAllRecords();

        // 科目レコードがない → インフォメーションページへ
        if (subject == null) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // 問題数が0 → インフォメーションページへ
        if (quizzes.size() == 0) {
            Information.forward(
                request, response,
                "出題範囲の問題数は<br>0問でした。",
                "出題設定 (科目、出題範囲) をご確認ください。",
                "トップ画面へ",
                "top"
            );
            return;
        }

        // 問題文 ⇔ 正解文 の入れ替え
        if ("swap".equals(format)) {
            for (QuizBean quiz : quizzes)
                quiz.swapQuestionAndAnswer();
        }

        // Javascriptでクイズデータ(JSON)を使用できるようにする
        //      JSP内javascriptにもEL式で渡せるが、
        //      エディタがjavascript内のEL式を解釈できずに警告を出すため、
        //      スクリプトごと送る
        String jsonscript = 
            "<script charset=\"utf-8\">\n" +
                "let quizJson = " + QuizBean.getJson(quizzes) + ";\n" +
                "let subjectName = \"" + subject.getName() + "\";\n" +
            "</script>";

        // リクエストへ設定
        request.setAttribute("difficulties", difficulties);
        request.setAttribute("subjectname", subject.getName());
        request.setAttribute("jsonscript", jsonscript);

        // JSPへ送信
        String view = "/WEB-INF/views/toquiz.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}