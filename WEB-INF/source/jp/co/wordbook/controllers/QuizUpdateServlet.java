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

        int    quizId;
        int    subjectId;
        int    difficultyId;
        String question;
        String answer;
        String explanation;

        try {
            // リクエストから取得
            quizId       = Parameter.getInt(request, "quizid");
            subjectId    = Parameter.getInt(request, "subjectid");
            difficultyId = Parameter.getInt(request, "difficultyid");
            question     = Parameter.getString(request, "question");
            answer       = Parameter.getString(request, "answer");
            explanation  = Parameter.getString(request, "explanation");

            // データベースから取得
            // 科目とユーザーが紐づいているか否か
            new SubjectDAO().userHasSubject(subjectId, userId);
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // レコードを 上書き or 挿入
        quizId = new QuizDAO().updateRecord(
            quizId, subjectId, difficultyId, explanation, question, answer);
        
        // 保存に失敗 → インフォメーションを表示
        if (quizId == 0) {
            Information.forward(
                request, response,
                "問題を保存できませんでした",
                "",
                "問題一覧へ",
                "quizlist?subjectid=" + subjectId
            );
            return;
        }

        // ページ遷移
        response.sendRedirect("./quizdetails?quizid=" + quizId + "&state=update");
    }
}