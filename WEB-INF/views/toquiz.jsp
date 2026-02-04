<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>

        <div class="window-width align-center">

            <div class="buttonlike">
                <label>
                    <input type="checkbox" id="input-enable"
                        ${quizSetting.isInputEnabled ? "checked" : "" }
                    >
                    <span>回答欄 表示</span>
                </label>
                <label>
                    <input type="checkbox" id="speak-enable"
                        ${quizSetting.isSpeechEnabled ? "checked" : "" }
                    >
                    <span>英語読み上げ</span>
                </label>
                <button class="quit-button" id="quit-button">出題終了</button> 
            </div>

            <div class="white-area quiz">

                ${subjectName}
                <div>全体 : <span id="total-count">0問目 / 0問中</span></div>
                <div>今回 : <span id="count-in-lot">0問目 / 0問中</span></div>

                <div class="question">
                    <p id="question">出題</p>
                </div>
                
                <input type="text" class="input" id="input">
                <p class="answer" id="answer">正解文</p>
                <div class="explanation" id="explanation">説明文</div>

                <div  class="buttonlike devide4">
                    <c:forEach var="difficulty" items="${difficultyMap}">
                        <label>
                            <input
                                type="radio" name="difficultyids"
                                value="${difficulty.key}"
                            >
                            <span>${difficulty.value}</span>
                        </label>
                    </c:forEach>
                </div>
                <br>

                <%-- 不可視のフォーム  クイズID配列送信用 --%>
                <form 
                    id="form" action="result", method="POST" 
                    style="display:none"
                ></form>
                
                <button class="red double" id="answer-button">答え</button><br>
                <button id="retest-button">後で再出題</button>
                <button id="next-button">次の問題</button>
            </div>
        </div>

    <%-- 変数 quizJson, subjectName を作成するスクリプト --%>
    ${jsonScript}
    <script src="script/toquiz.js" charset="utf-8"></script>

    </body>
</html>