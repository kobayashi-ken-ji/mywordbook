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
        // パラメータから取得
        int subject_id = Integer.parseInt(request.getParameter("subjectid"));
        boolean isSwap = "swap".equals(request.getParameter("format"));

        // 難易度(チェックボタン) は複数のため、配列で取得
        String[] difficulty_ids = request.getParameterValues("difficultyids");

        // データベースから取得
        List<QuizBean> quizzes = QuizBean.getRecords(subject_id, difficulty_ids);
        List<DifficultyBean> difficulties = DifficultyBean.getRecords();
        SubjectBean subject = SubjectBean.getRecord(subject_id);

        // 問題数が0 → 情報ページへ
        if (quizzes.size() == 0) {

            // リクエストへ設定
            request.setAttribute("heading", "出題範囲の問題数は<br>0問でした。");
            request.setAttribute("paragraph", "出題設定 (科目、難易度) をご確認ください。");
            request.setAttribute("url", "top");
            request.setAttribute("buttonname", "トップ画面へ");

            // JSPへ送信
            String view = "/WEB-INF/views/information.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(view);
            dispatcher.forward(request, response);

            return;
        }

        // 問題文 ⇔ 正解文 の入れ替え
        if (isSwap) {
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