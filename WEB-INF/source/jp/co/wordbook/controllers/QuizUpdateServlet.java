package jp.co.wordbook.controllers;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題の新規作成、上書き
@WebServlet("/quizupdate")
public class QuizUpdateServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");
        final int IRREG = -1;
        
        // パラメータから取得
        int    quiz_id       = NoNull.parseInt(request.getParameter("quizid"), IRREG);
        int    subject_id    = NoNull.parseInt(request.getParameter("subjectid"), IRREG);
        int    difficulty_id = NoNull.parseInt(request.getParameter("difficultyid"), IRREG);
        String question      = request.getParameter("question");
        String answer        = request.getParameter("answer");
        String explanation   = request.getParameter("explanation");

        // 科目とユーザーが紐づいているか否か
        String userId = Session.getUserId(request);
        boolean userHasSubject = new SubjectDAO().userHasSubject(subject_id, userId);

        // 不正な入力 → インフォメーションページへ
        if (quiz_id       == IRREG ||
            subject_id    == IRREG ||
            difficulty_id == IRREG ||
            question      == null  ||
            answer        == null  ||
            explanation   == null  ||
            !userHasSubject 
        ) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // レコードを 上書き or 挿入
        quiz_id = new QuizDAO().updateRecord(
            quiz_id, subject_id, difficulty_id, explanation, question, answer);
        
        // 保存に失敗 → インフォメーションを表示
        if (quiz_id == 0) {
            Information.forward(
                request, response,
                "問題を保存できませんでした",
                "",
                "問題一覧へ",
                "quizlist?subjectid=" + subject_id
            );
            return;
        }

        // ページ遷移
        response.sendRedirect("./quizdetails?quizid=" + quiz_id + "&state=update");
    }
}