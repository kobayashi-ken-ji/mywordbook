package jp.co.wordbook.controllers;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// 結果ページ
@WebServlet("/result")
public class ResultServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");
        String userId = Session.getUserId(request);

        String subjectName; 
        int correctCount;   
        int quizCount;      
        int noRetestCount;
        String[] stringQuizIds;

        QuizSettingDAO quizSettingDAO = new QuizSettingDAO();
        QuizSettingDTO quizSetting;


        try {
            // リクエストから取得
            subjectName   = Parameter.getString(request, "subjectName");
            correctCount  = Parameter.getInt(request, "correctCount");
            quizCount     = Parameter.getInt(request, "quizCount");
            noRetestCount = Parameter.getInt(request, "noRetestCount");
            stringQuizIds = Parameter.getStringArrayOrEmpty(request, "quizIds");

            // DBから取得
            quizSetting = quizSettingDAO.getRecord(userId);
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // 
        final int percentCorrect  = 100 * correctCount  / quizCount;
        final int percentNoRetest = 100 * noRetestCount / quizCount;

        // String[] → List<Integer> 変換
        List<Integer> quizIds = Arrays.stream(stringQuizIds)
            .map(Integer::valueOf)
            .collect(Collectors.toList());

        // 指定した問題を全て 「出題済み=true」にする
        QuizDAO quizDAO = new QuizDAO();
        quizDAO.setAsked(quizIds);

        // 既出題数を増やす
        quizSettingDAO.addAnsweredCount(userId, quizCount);
        
        // 全ての問題が出題済み → 出題をリセットするためのフラグを立てる
        final boolean completed = (quizDAO.getUnaskedCount(quizSetting) == 0);
        if (completed)
            quizSettingDAO.resetAnsweredCount(userId);

        // リクエストへ設定
        request.setAttribute("subjectName", subjectName);
        request.setAttribute("correctCount", correctCount);
        request.setAttribute("quizCount", quizCount);
        request.setAttribute("noRetestCount", noRetestCount);
        request.setAttribute("percentCorrect", percentCorrect);
        request.setAttribute("percentNoRetest", percentNoRetest);
        request.setAttribute("completed", completed);


        // JSPへ送信
        String view = "/WEB-INF/views/result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}