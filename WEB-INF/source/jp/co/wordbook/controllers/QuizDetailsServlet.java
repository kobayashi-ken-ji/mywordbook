package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題詳細ページ
@WebServlet("/quizdetails")
public class QuizDetailsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String state;
        QuizBean quiz;
        SubjectBean subject;
        DifficultyBean difficulty;

        // セッションから取得
        String userId = Session.getUserId(request);

        try {
            // リクエストから取得
            int quiz_id = Parameter.getInt(request, "quizid");
            state       = Parameter.getString(request, "state");

            // データベースから取得
            quiz       = new QuizDAO().getRecord(quiz_id);
            subject    = new SubjectDAO().getRecord(quiz.getSubject_id(), userId);  // ユーザーを照合
            difficulty = new DifficultyDAO().getRecord(quiz.getDifficulty_id());
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // 見出し
        String heading = ("update".equals(state))
            ? "問題を保存しました"
            : "問題詳細";


        // リクエストへ設定
        request.setAttribute("heading", heading);
        request.setAttribute("quiz", quiz);
        request.setAttribute("subject_name", subject.getName());
        request.setAttribute("difficulty_name", difficulty.getName());

        // JSPへ送信
        String view = "/WEB-INF/views/quizdetails.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
