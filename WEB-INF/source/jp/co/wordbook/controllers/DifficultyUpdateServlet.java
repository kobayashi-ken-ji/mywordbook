package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 1つの問題の難易度を上書き (ページ遷移なし)
@WebServlet("/difficultyupdate")
public class DifficultyUpdateServlet  extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // パラメータから取得
        int quiz_id       = Integer.parseInt(request.getParameter("quizid"));
        int difficulty_id = Integer.parseInt(request.getParameter("difficultyid"));

        // データベースへ難易度を上書き
        QuizBean.updateDifficulty(quiz_id, difficulty_id);

        return;
    }
}

