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
        SubjectDAO subjectDAO = new SubjectDAO();
        DifficultyDAO difficultyDAO = new DifficultyDAO();

        // セッション、パラメータから取得
        String userId = Session.getUserId(request);
        int quiz_id   = NoNull.parseInt(request.getParameter("quizid"), -1);
        String state  = request.getParameter("state");

        // 見出し
        String heading = ("update".equals(state))
            ? "問題を保存しました"
            : "問題詳細";


        // データベースから取得
        //      取得できない、ユーザーと紐づいていない場合は
        //      不正入力のインフォメーションページへ
        QuizBean quiz = new QuizDAO().getRecord(quiz_id);
        if (quiz == null) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        DifficultyBean difficulty = difficultyDAO.getRecord(quiz.getDifficulty_id());
        SubjectBean subject = subjectDAO.getRecord(quiz.getSubject_id(), userId);
        if (subject == null) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

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
