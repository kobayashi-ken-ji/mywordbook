package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import jp.co.wordbook.models.*;

// トップページ
@WebServlet("/top")
public class TopServlet extends HttpServlet {
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // セッションから取得
        String userId = Session.getUserId(request);

        // DAOを生成
        QuizSettingDAO quizSettingDAO = new QuizSettingDAO();
        SubjectDAO subjectDAO = new SubjectDAO();
        DifficultyDAO difficultyDAO = new DifficultyDAO();

        // DTOを取得
        List<SubjectDTO> subjects = subjectDAO.getAllRecords(userId);  // ユーザーを照合
        List<DifficultyDTO> difficulties = difficultyDAO.getAllRecords();
        QuizSettingDTO quizSetting;
        try {
            quizSetting = quizSettingDAO.getRecord(userId);
        }

        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        //-----------------------------------------------------------

        // 「難易度のチェックボタン」の初期値を設定
        List<Integer> activeDifficultyIds = quizSetting.getDifficultyIds();
        for (DifficultyDTO difficulty : difficulties) {

            // ユーザー設定の中に、この難易度が含まれている → チェックを入れる
            boolean checked = activeDifficultyIds.contains(difficulty.getId());
            difficulty.SetChecked(checked);
        }

        // 「一度に出題する問題数」のプルダウンの要素
        List<Integer> lotSizes = Arrays.asList(10, 20, 30, 40, 50);

        //-----------------------------------------------------------

        // リクエストへ設定
        request.setAttribute("nosubjects", (subjects.size() == 0));
        request.setAttribute("subjects", subjects);
        request.setAttribute("difficulties", difficulties);
        request.setAttribute("quizSetting", quizSetting);
        request.setAttribute("lotSizes", lotSizes);

        // JSPへ送信
        String view = "/WEB-INF/views/top.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }
}
