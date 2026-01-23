package jp.co.wordbook.controllers;

import java.io.*;
import java.util.*;
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
        // セッションから取得
        String userId = Session.getUserId(request);

        String[] difficulty_ids;
        String format;
        SubjectDTO subject;
        List<QuizDTO> quizzes;
        List<DifficultyDTO> difficulties;

        try {
            // リクエストから取得
            difficulty_ids  = Parameter.getStringArray(request, "difficultyids");
            format          = Parameter.getString(request, "format");
            int subject_id  = Parameter.getInt(request, "subjectid");

            // データベースから取得
            subject      = new SubjectDAO().getRecord(subject_id, userId);  // ユーザーを照合
            quizzes      = new QuizDAO().getAllRecords(subject_id, difficulty_ids);
            difficulties = new DifficultyDAO().getAllRecords();
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
        if ("swap".equals(format)) {
            for (QuizDTO quiz : quizzes) {
                String newAnswer   = quiz.getQuestion();
                String newQuestion = quiz.getAnswer();
                quiz.setQuestion(newQuestion);
                quiz.setAnswer(newAnswer);
            }
        }

        // Javascriptでクイズデータ(JSON)を使用できるようにする
        //      JSP内javascriptにもEL式で渡せるが、
        //      エディタがjavascript内のEL式を解釈できずに警告を出すため、
        //      スクリプトごと送る
        String jsonscript = 
            "<script charset=\"utf-8\">\n" +
                "let quizJson = " + quizlistToJson(quizzes) + ";\n" +
                "let subjectName = \"" + subject.getName() + "\";\n" +
            "</script>";

        // リクエストへ設定
        request.setAttribute("difficulties", difficulties);
        request.setAttribute("subjectname", subject.getName());
        request.setAttribute("jsonscript", jsonscript);

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
                quiz.getDifficulty_id(),
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