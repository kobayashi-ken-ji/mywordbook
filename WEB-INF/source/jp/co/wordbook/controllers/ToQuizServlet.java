package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.wordbook.models.*;

// 出題ページ
@WebServlet("/toquiz")
public class ToQuizServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // セッションから取得、セッションを延長
        String userId = Session.getUserId(request);
        Session.setSession(request, userId);

        // try文の中で代入、外で使用
        int unaskedCount;
        SubjectDTO subject;
        List<QuizDTO> quizzes;
        QuizSettingDTO quizSetting;
        
        // パラメータチェック
        try {
            QuizDAO quizDAO = new QuizDAO();
            SubjectDAO subjectDAO = new SubjectDAO();
            QuizSettingDAO quizSettingDAO = new QuizSettingDAO();

            // 現在の設定を取得
            quizSetting = quizSettingDAO.getRecord(userId);

            // 「新しく開始 / 続きから」ボタンが押されたか
            String action = request.getParameter("action");

            // 「新しく開始」の処理
            if ("new-start".equals(action)) { 

                //-------------------------
                // DBに QuizSetting を書込む
                //-------------------------

                // リクエストから取得
                String[] difficultyIdStrings
                              = Parameter.getStringArray(request, "difficultyids");
                String format = Parameter.getString(request, "format");
                int subjectId = Parameter.getInt(request, "subjectid");
                int lotSize   = Parameter.getInt(request, "lot-size");

                // ユーザーを照合
                subject = subjectDAO.getRecord(subjectId, userId);
        
                // 出題文と正解文を入れ替える
                boolean isSwapMode = "swap".equals(format);

                // 難易度id  String[] → List<Integer>  変換
                List<Integer> difficultyIds = Arrays.asList(difficultyIdStrings).stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

                // 出題設定をDBへ更新、既出題数をリセット
                quizSettingDAO.updateRecord(
                    userId, subjectId, isSwapMode, lotSize, 0, difficultyIds);
                
                // DTOに詰める
                quizSetting.setSubjectId(subjectId);
                quizSetting.setIsSwapMode(isSwapMode);
                quizSetting.setLotSize(lotSize);
                quizSetting.setDifficultyIds(difficultyIds);
                quizSetting.setAnsweredCount(0);

                //-------------------------
                // 各問題の出題済みフラグを初期化
                //      ユーザーが自分のタイミングで、はじめからスタートすることも可能なため、
                //      出題リザルト時ではなく、ここで実行する
                //-------------------------

                quizDAO.resetAllAsked(userId);
            }

            // 「続きから」の処理
            else {
                // 「一度の出題数」のみ DB,DTO へ更新
                int lotSize = Parameter.getInt(request, "lot-size");
                if (lotSize != 0) {
                    quizSettingDAO.updateLotSize(userId, lotSize);
                    quizSetting.setLotSize(lotSize);
                }

                // それ以外はDB値を取得
                int subjectId = quizSetting.getSubjectId();
                subject = subjectDAO.getRecord(subjectId, userId);
            }

            // quizSetting値の条件で、データベースから取得
            quizzes      = quizDAO.getUnaskedRecords(quizSetting);
            unaskedCount = quizDAO.getUnaskedCount(quizSetting);    // 未出題数 (全体)
        }

        // パラメータが不正 → インフォメーションページへ
        catch (ParameterException e) {
            e.printStackTrace();
            Information.forwardDataWasIncorrect(request, response);
            return;
        }

        // 問題数が0 → インフォメーションページへ
        if (quizzes.size() == 0) {
            Information.forward(
                request, response,
                "出題範囲の問題数は<br>0問でした。",
                "出題設定 (科目、出題範囲) をご確認ください。",
                "トップ画面へ",
                "top"
            );
            return;
        }

        // 問題文 ⇔ 正解文 の入れ替え
        if (quizSetting.getIsSwapMode()) {
            for (QuizDTO quiz : quizzes) {
                String newAnswer   = quiz.getQuestion();
                String newQuestion = quiz.getAnswer();
                quiz.setQuestion(newQuestion);
                quiz.setAnswer(newAnswer);
            }
        }

        // 総出題数 = 既出題数 + 未出題数
        final int answeredCount = quizSetting.getAnsweredCount();
        final int totalSize = answeredCount + unaskedCount;

        // Javascriptでクイズデータ(JSON)を使用できるようにする
        //      JSP内javascriptにもEL式で渡せるが、
        //      エディタがjavascript内のEL式を解釈できずに警告を出すため、
        //      スクリプトごと送る
        String jsonScript = 
            "<script charset=\"utf-8\">\n" +
                "let quizJson = " + quizlistToJson(quizzes) + ";\n" +
                "let subjectName = \"" + subject.getName() + "\";\n" +
                "let answeredCount = " + answeredCount + ";\n" +
                "let totalSize = " + totalSize + ";\n" +
            "</script>";

        // リクエストへ設定
        request.setAttribute("difficultyMap", Difficulty.MAP);
        request.setAttribute("subjectName", subject.getName());
        request.setAttribute("jsonScript", jsonScript);
        request.setAttribute("quizSetting", quizSetting);

        // JSPへ送信
        String view = "/WEB-INF/views/toquiz.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(view);
        dispatcher.forward(request, response);
    }


    /** 
     * クイズリスト → JSONテキスト へ変換 (jacksonを使用)
     * @param list  問題のリスト
     * @return      JSONテキスト
     */
    private static String quizlistToJson(List<QuizDTO> list) 
        throws JsonProcessingException {

        // List → 2次元配列 へ変換
        // {{id, question, answer, explanation}, ...}
        final Object[][] quizArray = list.stream()
            .map(quiz -> new Object[] {
                quiz.getId(),
                quiz.getDifficultyId(),
                quiz.getQuestion(),
                quiz.getAnswer(),
                quiz.getExplanation(),
            })
            .toArray(Object[][]::new);

        // 配列 → JSON化
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(quizArray);
        return json;
    }
}