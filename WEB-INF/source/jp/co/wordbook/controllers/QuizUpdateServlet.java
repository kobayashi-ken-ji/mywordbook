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
        String userId = Session.getUserId(request);

        int    quiz_id;
        int    subject_id;
        int    difficulty_id;
        String question;
        String answer;
        String explanation;

        try {
            // リクエストから取得
            quiz_id       = Parameter.getInt(request, "quizid");
            subject_id    = Parameter.getInt(request, "subjectid");
            difficulty_id = Parameter.getInt(request, "difficultyid");
            question      = Parameter.getString(request, "question");
            answer        = Parameter.getString(request, "answer");
            explanation   = Parameter.getString(request, "explanation");

            // データベースから取得
            // 科目とユーザーが紐づいているか否か
            new SubjectDAO().userHasSubject(subject_id, userId);
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
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