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
        final int IRREG = -1;
        SubjectDAO subjectDAO = new SubjectDAO();
        DifficultyDAO difficultyDAO = new DifficultyDAO();

        // パラメータから取得
        int quiz_id    = NoNull.parseInt(request.getParameter("id"), IRREG);
        int subject_id = NoNull.parseInt(request.getParameter("subjectid"), IRREG);

        // 不正な入力 → インフォメーションページへ
        if (quiz_id == IRREG  ||  subject_id == IRREG) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // セッション、データベースから取得
        String userId = Session.getUserId(request);
        List<SubjectBean>    subjects     = subjectDAO.getAllRecords(userId);
        List<DifficultyBean> difficulties = difficultyDAO.getAllRecords();
        
        // 問題を新規作成 or 編集
        boolean isNew = (quiz_id == 0);
        QuizBean quiz = (isNew)
            ? new QuizBean(0, subject_id, 2, "","", "")
            : new QuizDAO().getRecord(quiz_id);

            
        // 問題が取得できない、ユーザーと紐づいていない 
        //  → 不正入力のインフォメーションページへ
        if (quiz == null ||
            !subjectDAO.userHasSubject(quiz.getSubject_id(), userId)
        ) {
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // キャンセルボタンの遷移先
        String cancelURL = (isNew)
            ? "quizlist?subjectid=" + quiz.getSubject_id()  // 新規作成 → 問題一覧へ
            : "quizdetails?quizid=" + quiz.getId();         // 編集 → 問題詳細へ

        // リクエストへ設定
        request.setAttribute("quiz", quiz);
        request.setAttribute("subjects", subjects);
        request.setAttribute("difficulties", difficulties);
        request.setAttribute("cancelurl", cancelURL);

        // JSPへ送信
        String view = "/WEB-INF/views/quizedit.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}

