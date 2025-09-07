<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.wordbook.*" %>
<%
    // リクエストから取得
    List<Subject> subjects = (List<Subject>)request.getAttribute("subjects");
    List<Difficulty> difficulties = (List<Difficulty>)request.getAttribute("difficulties");
%>

<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>

        <div class="window-width align-center">
            <a class="button" href="logout">ログアウト</a>

            <form class="white-area buttonlike" action="toquiz" method="get" name="form">
                <h1>出題設定</h1>

                <div>
                    科目<br>
                    <select id="subject-select" name="subjectid">
                        <% for (Subject subject : subjects) { 
                            int    id   = subject.id;
                            String name = subject.name;
                        %>
                            <option value='<%= id %>'><%= name %></option>
                        <% } %>
                    </select>
                </div>
                <br>

                <div>
                    出題範囲<br>

                    <% for (Difficulty difficulty : difficulties) { 
                        String diffId = "difficultyid" + difficulty.id;
                        String checked = (2 <= difficulty.id)
                            ? "checked" : "";
                    %>
                        <label>
                            <input
                                type="checkbox" name="difficultyids"
                                value="<%= difficulty.id %>"
                                id="<%= diffId %>" <%= checked %>
                            >
                            <span><%= difficulty.name %></span>
                        </label>
                    <% } %>
                </div>
                <br>

                <div>
                    出題方式<br>
                    <label>
                        <input type="radio" name="format" value="normal" checked>
                        <span>通常</span>
                    </label>
                    <label>
                        <input type="radio" name="format" value="swap">
                        <span>問題 ⇔ 答え</span>
                    </label>
                </div>
                <br>

                <button type="submit" class="red wide" onclick="topStartButton()">
                    出題スタート
                </button>
                <br>
            </form>
            <br>
                

            <div>
                <a href="subjectlist?edit=subject">科目を編集</a> | 
                <a href="subjectlist?edit=quiz"   >問題を編集</a>
            </div>
        </div>
        <script src="script/onclick.js" charset="utf-8"></script>
    </body>
</html>