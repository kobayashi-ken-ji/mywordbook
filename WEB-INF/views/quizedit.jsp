<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.wordbook.models.*" %>
<%
    // リクエストから取得
    Quiz quiz = (Quiz)request.getAttribute("quiz");
    List<Subject>    subjects    = (List<Subject>)request.getAttribute("subjects");
    List<Difficulty> difficulties = (List<Difficulty>)request.getAttribute("difficulties");

    String quiz_id = (quiz.id == 0)
        ? "- 新規作成 -"
        : String.valueOf(quiz.id);
%>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>


        <div class="window-width align-center">
            <form class="white-area" action="quizupdate" method="post" name="form">

                <h1>問題編集</h1>

                <table class="td-align-left ">
                    <tr>
                        <th>ID</th>
                        <td><%= quiz_id %></td>
                    </tr>
                    <tr>
                        <th>科目</th>
                        <td>
                            <select id="subject-select" name="subjectid">
                                <% for (Subject subject : subjects) {
                                    String selected = (subject.id == quiz.subject_id)
                                        ? "selected" : "";
                                %>
                                    <option value='<%= subject.id %>' <%= selected %>>
                                        <%= subject.name %>
                                    </option>
                                <% } %>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>問題文</th>
                        <td><input type="text" name="question" value="<%= quiz.question %>"></td>
                    </tr>
                    <tr>
                        <th>正解文</th>
                        <td><input type="text" name="answer" value="<%= quiz.answer %>"></td>
                    </tr>
                    <tr>
                        <th>説明文</th>
                        <td><textarea name="explanation"><%= quiz.explanation %></textarea></td>
                    </tr>
                    <tr>
                        <th>難易度</th>
                        <td class="buttonlike">
                            <% for (Difficulty difficulty : difficulties) { 
                                String checked = (difficulty.id == quiz.difficulty_id)
                                    ? "checked" : "";
                            %>
                                <label>
                                    <input
                                        type="radio" name="difficultyid"
                                        value="<%= difficulty.id %>" <%= checked %>
                                    >
                                    <span><%= difficulty.name %></span>
                                </label>
                            <% } %>
                        </td>
                    </tr>
                </table>
                <br>
                
                <input  type="hidden" name="quizid" value="<%= quiz.id %>">
                <button type="submit" onclick="quizSaveButton()">保存</button>
                <a class="button red" onclick="history.back()">キャンセル</a>
            </form>
        </div>
        <script src="script/onclick.js" charset="utf-8"></script>
    </body>
</html>