package jp.co.wordbook;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

// 1つの問題の難易度を上書き (ページ遷移なし)
public class DifficultyUpdateServlet  extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // パラメータから取得
        int quiz_id       = Integer.parseInt(request.getParameter("quizid"));
        int difficulty_id = Integer.parseInt(request.getParameter("difficultyid"));

        // データベースへ難易度を上書き
        Quiz.updateDifficulty(quiz_id, difficulty_id);

        return;
    }
}

