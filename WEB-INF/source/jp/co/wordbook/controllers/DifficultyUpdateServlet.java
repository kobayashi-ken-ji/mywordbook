package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題の難易度を上書き (出題ページからの簡易設定のため、遷移なし)
@WebServlet("/difficultyupdate")
public class DifficultyUpdateServlet  extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        QuizDAO quizDAO = new QuizDAO();
        SubjectDAO subjectDAO = new SubjectDAO();
        int quiz_id;
        int difficulty_id;

        // セッションから取得
        String userId = Session.getUserId(request);

        try {
            // リクエストから取得
            quiz_id       = Parameter.getInt(request, "quizid");
            difficulty_id = Parameter.getInt(request, "difficultyid");

            // データベースから取得
            // ユーザーと科目が結びついているかチェック
            QuizDTO quiz = quizDAO.getRecord(quiz_id);
            subjectDAO.userHasSubject(quiz.getSubject_id(), userId);
        }
        
        // パラメータが不正  →  ページ遷移なし、エラー告知無し のためreturn
        catch (ParameterException e) {
            e.printStackTrace();
            return;
        }

        // データベースへ難易度を上書き
        quizDAO.updateDifficulty(quiz_id, difficulty_id);
    }
}

