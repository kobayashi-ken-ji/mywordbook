package jp.co.wordbook;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// 出題ページ
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
        List<Quiz> quizzes = Quiz.getRecords(subject_id, difficulty_ids);
        List<Difficulty> difficulties = Difficulty.getRecords();
        Subject subject = Subject.getRecord(subject_id);

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
            for (Quiz quiz : quizzes)
                quiz.swapQuestionAndAnswer();
        }

        // Javascriptでクイズデータ(JSON)を使用できるようにする
        String jsonscript = 
            "<script charset=\"utf-8\">\n" +
                "let quizJson = " + Quiz.getJson(quizzes) + ";\n" +
                "let subjectName = \"" + subject.name + "\";\n" +
            "</script>";

        // リクエストへ設定
        request.setAttribute("difficulties", difficulties);
        request.setAttribute("subjectname", subject.name);
        request.setAttribute("jsonscript", jsonscript);

        // JSPへ送信
        String view = "/WEB-INF/views/toquiz.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}