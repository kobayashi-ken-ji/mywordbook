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
        final int IRREG = -1;

        // パラメータから取得
        int quiz_id       = NoNull.parseInt(request.getParameter("quizid"), IRREG);
        int difficulty_id = NoNull.parseInt(request.getParameter("difficultyid"), IRREG);

        // 不正入力 → 遷移しないため、処理なし
        if (quiz_id == IRREG  ||  difficulty_id == IRREG)
            return;

        // データベースへ難易度を上書き
        new QuizDAO().updateDifficulty(quiz_id, difficulty_id);
    }
}

