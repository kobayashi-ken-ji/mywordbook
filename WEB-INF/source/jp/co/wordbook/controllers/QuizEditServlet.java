package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 問題編集ページ
@WebServlet("/quizedit")
public class QuizEditServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        List<SubjectDTO>    subjects;
        List<DifficultyDTO> difficulties;
        boolean isNew;
        QuizDTO quiz;

        // セッションから取得
        String userId = Session.getUserId(request);

        try {
            // リクエストから取得
            int quizId    = Parameter.getInt(request, "id");
            int subjectId = Parameter.getInt(request, "subjectid");

            // idが0なら新規作成
            isNew = (quizId == 0);

            SubjectDAO    subjectDAO    = new SubjectDAO();
            DifficultyDAO difficultyDAO = new DifficultyDAO();

            // データベースから取得
            subjects     = subjectDAO.getAllRecords(userId);
            difficulties = difficultyDAO.getAllRecords();
            quiz = (isNew)
                ? new QuizDTO(0, subjectId, 2, "","", "", false)
                : new QuizDAO().getRecord(quizId);

            // ユーザーと紐づいているかチェック
            subjectDAO.userHasSubject(quiz.getSubjectId(), userId);
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // キャンセルボタンの遷移先
        String cancelURL = (isNew)
            ? "quizlist?subjectid=" + quiz.getSubjectId()               // 新規作成 → 問題一覧へ
            : "quizdetails?quizid=" + quiz.getId() + "&state=normal";   // 編集 → 問題詳細へ

        // リクエストへ設定
        request.setAttribute("quiz", quiz);
        request.setAttribute("subjects", subjects);
        request.setAttribute("difficulties", difficulties);
        request.setAttribute("cancelURL", cancelURL);

        // JSPへ送信
        String view = "/WEB-INF/views/quizedit.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}

