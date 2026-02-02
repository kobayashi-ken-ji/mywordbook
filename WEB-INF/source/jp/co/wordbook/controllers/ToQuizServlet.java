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

        // DTO
        SubjectDTO subject;
        List<QuizDTO> quizzes;
        List<DifficultyDTO> difficulties;
        
        // QuizSettingの情報
        int           subjectId;
        boolean       isSwapMode;       // 出題文と正解文を入れ替える
        int           lotSize;          // 一度に出題する問題数
        int           answeredCount;    // 既出題数
        List<Integer> difficultyIds;

        int unaskedCount = 0;   // 未出題数
        
        // パラメータチェック
        try {
            QuizDAO quizDAO = new QuizDAO();
            QuizSettingDAO quizSettingDAO = new QuizSettingDAO();
            QuizSettingDTO quizSetting;

            // 「新しく開始 / 続きから」ボタンが押されたか
            String actionString = request.getParameter("action");

            // 「新しく開始」の処理
            if ("new-start".equals(actionString)) { 

                //-------------------------
                // DBに QuizSetting を書込む
                //-------------------------

                String format;
                String[] difficultyIdStrings;

                // リクエストから取得
                difficultyIdStrings
                          = Parameter.getStringArray(request, "difficultyids");
                format    = Parameter.getString(request, "format");
                subjectId = Parameter.getInt(request, "subjectid");
                lotSize   = Parameter.getInt(request, "lot-size");

                // ユーザーを照合
                subject = new SubjectDAO().getRecord(subjectId, userId);
        
                isSwapMode = "swap".equals(format);

                // 難易度id  String[] → List<Integer>  変換
                difficultyIds = Arrays.asList(difficultyIdStrings).stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

                // 出題設定をDBへ更新、既出題数をリセット
                quizSettingDAO.updateRecord(
                    userId, subjectId, isSwapMode, lotSize, 0, difficultyIds);
                
                // DTOに詰める
                quizSetting = new QuizSettingDTO();
                quizSetting.setSubjectId(subjectId);
                quizSetting.setIsSwapMode(isSwapMode);
                quizSetting.setLotSize(lotSize);
                quizSetting.setDifficultyIds(difficultyIds);

                //-------------------------
                // 各問題の出題済みフラグを初期化
                //      ユーザーが自分のタイミングで、はじめからスタートすることも可能なため、
                //      出題リザルト時ではなく、ここで実行する
                //-------------------------

                quizDAO.resetAllAsked(userId);
                answeredCount = 0;
            }

            // 「続きから」の処理
            else {
                // 「一度の出題数」のみDBへ更新
                lotSize = Parameter.getInt(request, "lot-size");
                if (lotSize != 0)
                    quizSettingDAO.updateLotSize(userId, lotSize);

                // それ以外はDBから取得
                quizSetting = quizSettingDAO.getRecord(userId);
                subjectId     = quizSetting.getSubjectId();
                isSwapMode    = quizSetting.getIsSwapMode();
                answeredCount = quizSetting.getAnsweredCount();
                difficultyIds = quizSetting.getDifficultyIds();

                // 科目情報を取得
                subject = new SubjectDAO().getRecord(subjectId, userId);
            }

            // データベースから取得
            difficulties = new DifficultyDAO().getAllRecords();
            quizzes      = quizDAO.getUnaskedRecords(quizSetting);
            unaskedCount = quizDAO.getUnaskedCount(quizSetting);
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
        if (isSwapMode) {
            for (QuizDTO quiz : quizzes) {
                String newAnswer   = quiz.getQuestion();
                String newQuestion = quiz.getAnswer();
                quiz.setQuestion(newQuestion);
                quiz.setAnswer(newAnswer);
            }
        }

        // 総出題数 = 既出題数 + 未出題数
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
        request.setAttribute("difficulties", difficulties);
        request.setAttribute("subjectName", subject.getName());
        request.setAttribute("jsonScript", jsonScript);

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