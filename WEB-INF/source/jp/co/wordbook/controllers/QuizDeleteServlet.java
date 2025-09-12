package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題を削除 → インフォメーションページへ
@WebServlet("/quizdelete")
public class QuizDeleteServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        final int IRREG = -1;
        
        // セッション、パラメータから取得
        String userId = Session.getUserId(request);
        int quiz_id = NoNull.parseInt(request.getParameter("id"), IRREG);

        // 不正な入力 → インフォメーションページへ
        if (quiz_id == IRREG) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // データベースから取得
        QuizDAO quizDAO = new QuizDAO();
        QuizBean quiz = quizDAO.getRecord(quiz_id);


        // 問題が取得できない、ユーザーと紐づいていない 
        //  → 不正入力のインフォメーションページへ
        if (quiz == null ||
            !new SubjectDAO().userHasSubject(quiz.getSubject_id(), userId)
        ) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // データベースから削除
        boolean success = quizDAO.deleteRecord(quiz_id);
        
        // 見出し
        String heading = (success)
            ? "問題を削除しました"
            : "問題を削除できませんでした";


        // infomation.jsp へ送信
        Information.forward(
            request, response, 
            heading,
            "問題文 : " + quiz.getQuestion(),
            "問題一覧へ",
            "quizlist?subjectid=" + quiz.getSubject_id()
        );
    }
}