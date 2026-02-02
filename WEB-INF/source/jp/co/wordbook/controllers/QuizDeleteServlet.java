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
        int quizId;
        QuizDTO quiz;
        QuizDAO quizDAO = new QuizDAO();

        // セッションから取得
        String userId = Session.getUserId(request);

        try {
            // リクエスト、データベースから取得
            quizId = Parameter.getInt(request, "id");
            quiz    = quizDAO.getRecord(quizId);

            // ユーザーと紐づいているかチェック
            new SubjectDAO().userHasSubject(quiz.getSubjectId(), userId);
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // データベースから削除
        boolean success = quizDAO.deleteRecord(quizId);
        
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
            "quizlist?subjectid=" + quiz.getSubjectId()
        );
    }
}