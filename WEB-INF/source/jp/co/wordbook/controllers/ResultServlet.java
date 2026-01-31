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

    // public void doGet(HttpServletRequest request, HttpServletResponse response)
    //     throws ServletException, IOException
    // {
    //     String subjectname; 
    //     int correctcount;   
    //     int quizcount;      
    //     int rate;           

    //     // リクエストから取得
    //     try {
    //         subjectname  = Parameter.getString(request, "subjectname");
    //         correctcount = Parameter.getInt(request, "correctcount");
    //         quizcount    = Parameter.getInt(request, "quizcount");
    //         rate         = Parameter.getInt(request, "rate");
    //     }
        
    //     // パラメータが不正 → インフォメーションページへ
    //     catch (ParameterException e) {
    //         e.printStackTrace();
    //         Information.forwardDataWasIncorrect(request, response);
    //         return;
    //     }

    //     // リクエストへ設定
    //     request.setAttribute("subjectname", subjectname);
    //     request.setAttribute("correctcount", correctcount);
    //     request.setAttribute("quizcount", quizcount);
    //     request.setAttribute("rate", rate);

    //     // JSPへ送信
    //     String view = "/WEB-INF/views/result.jsp";
    //     RequestDispatcher dispatcher = request.getRequestDispatcher(view);
    //     dispatcher.forward(request, response);
    // }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        request.setCharacterEncoding("utf-8");
        String userId = Session.getUserId(request);

        String subjectname; 
        int correctcount;   
        int quizcount;      
        int noRetestCount;
        String[] stringQuizIds;

        QuizSettingDAO quizSettingDAO = new QuizSettingDAO();
        QuizSettingDTO quizSetting;


        try {
            // リクエストから取得
            subjectname   = Parameter.getString(request, "subjectname");
            correctcount  = Parameter.getInt(request, "correctcount");
            quizcount     = Parameter.getInt(request, "quizcount");
            noRetestCount = Parameter.getInt(request, "no_retest_count");
            stringQuizIds = Parameter.getStringArray(request, "quizIds");

            // DBから取得
            quizSetting = quizSettingDAO.getRecord(userId);
        }
        
        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // 配列を受け取る
        // String[] stringQuizIds = request.getParameterValues("quizIds");

        // String[] → List<Integer> 変換
        List<Integer> quizIds = Arrays.stream(stringQuizIds)
            .map(Integer::valueOf)
            .collect(Collectors.toList());

        // 指定した問題を全て 「出題済み=true」にする
        QuizDAO quizDAO = new QuizDAO();
        quizDAO.setAsked(quizIds);

        // 既出題数を増やす
        quizSettingDAO.addAnsweredCount(userId, quizcount);
        
        // 全ての問題が出題済み → 出題をリセットするためのフラグを立てる
        final boolean completed = (quizDAO.getUnaskedCount(quizSetting) == 0);
        if (completed)
            quizSettingDAO.resetAnsweredCount(userId);

        // リクエストへ設定
        request.setAttribute("subjectname", subjectname);
        request.setAttribute("correctcount", correctcount);
        request.setAttribute("quizcount", quizcount);
        request.setAttribute("noRetestCount", noRetestCount);
        request.setAttribute("completed", completed);


        // JSPへ送信
        String view = "/WEB-INF/views/result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}