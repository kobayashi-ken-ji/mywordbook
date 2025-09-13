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
        // セッションから取得
        String userId = Session.getUserId(request);

        String[] difficulty_ids;
        String format;
        SubjectBean subject;
        List<QuizBean> quizzes;
        List<DifficultyBean> difficulties;

        try {
            // リクエストから取得
            difficulty_ids  = Parameter.getStringArray(request, "difficultyids");
            format          = Parameter.getString(request, "format");
            int subject_id  = Parameter.getInt(request, "subjectid");

            // データベースから取得
            subject      = new SubjectDAO().getRecord(subject_id, userId);  // ユーザーを照合
            quizzes      = new QuizDAO().getAllRecords(subject_id, difficulty_ids);
            difficulties = new DifficultyDAO().getAllRecords();
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
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