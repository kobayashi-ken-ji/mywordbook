<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.wordbook.models.*" %>
<%
    List<Difficulty> difficulties = (List<Difficulty>)request.getAttribute("difficulties");
%>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>


        <div class="window-width align-center">

            <div class="quiz-menu">
                <select id="input-enable">
                    <option value="true" >回答欄を表示</option>
                    <option value="false">回答欄を非表示</option>
                </select>

                <button id="quit-button">出題終了</button> 
            </div>

            <div class="white-area quiz">

                ${subjectname}
                <div id="count">0問目 / 0問中</div>

                <div class="question">
                    <p id="question">出題</p>
                </div>

                
                <input type="text" class="input" id="input">
                
                <p class="answer" id="answer">正解文</p>

                <div class="explanation" id="explanation">説明文</div>

                
                <div class="buttonlike">
                    <% for (Difficulty difficulty : difficulties) { %>
                        <label>
                            <input
                                type="radio" name="difficultyids"
                                value="<%= difficulty.id %>"
                            >
                            <span><%= difficulty.name %></span>
                        </label>
                    <% } %>
                </div>
                <br>


                <button class="red" id="answer-button">答え</button>
                <button id="next-button">次の問題</button>
            </div>
        </div>

    ${jsonscript}
    <script src="script/toquiz.js" charset="utf-8"></script>

    </body>
</html>