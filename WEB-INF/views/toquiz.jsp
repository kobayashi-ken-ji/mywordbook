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
                    <input type="checkbox" id="input-enable" checked>
                    <span>回答欄 表示</span>
                </label>
                <label>
                    <input type="checkbox" id="speak-enable">
                    <span>英語読み上げ</span>
                </label>
                <button class="quit-button" id="quit-button">出題終了</button> 
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
                    <c:forEach var="difficulty" items="${difficulties}">
                        <label>
                            <input
                                type="radio" name="difficultyids"
                                value="${difficulty.id}"
                            >
                            <span>${difficulty.name}</span>
                        </label>
                    </c:forEach>
                </div>
                <br>

                <button class="red" id="answer-button">答え</button>
                <button id="next-button">次の問題</button>
            </div>
        </div>

    <%-- 変数 quizJson, subjectName を作成するスクリプト --%>
    ${jsonscript}
    <script src="script/toquiz.js" charset="utf-8"></script>

    </body>
</html>