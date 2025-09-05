<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.co.wordbook.*" %>
<%
    // サーブレットから受け取り
    boolean isReverse  = (boolean)request.getAttribute("isreverse");
    String subjectname = (String)request.getAttribute("subjectname");
    List<Quiz> quizzes = (List<Quiz>)request.getAttribute("quizzes");
%>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>


        <div class="window-width align-center">

            <div class="quiz-menu">
                <select id="input-select">
                    <option value="enable" >回答欄を表示</option>
                    <option value="disable">回答欄を非表示</option>
                </select>

                <button>出題終了</button> 
            </div>

            <div class="white-area quiz">

                英語 - 単語
                <div id="count-text">0問目 / 0問中</div>

                <div class="question">
                    <p id="question-text">出題</p>
                </div>

                
                <input type="text" class="answer" id="answer-input">
                
                <p class="correct" id="correct-text">答え</p>

                <div class="explanation" id="explanation-text">説明</div>

                
                <div class="buttonlike">
                    <label>
                        <input type="radio" name="learning-level" checked>
                        <span>難しい</span>
                    </label>
                    <label>
                        <input type="radio" name="learning-level">
                        <span>普通</span>
                    </label>
                    <label>
                        <input type="radio" name="learning-level">
                        <span>簡単</span>
                    </label>
                </div>
                <br>


                <button class="red" id="answer-button">答え</button>
                <button id="next-button">次の問題</button>
            </div>
        </div>
        <!-- <script src="script/quizpage.js" charset="utf-8"></script> -->

    </body>
</html>