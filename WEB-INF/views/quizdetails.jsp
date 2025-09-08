<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>


        <div class="window-width align-center">
            <div class="white-area">

                <h1>問題詳細</h1>

                <!-- テーブルヘッダーを縦並べ -->
                <table class="td-align-left ">
                    <tr>
                        <th>ID</th>
                        <td>${quiz.idString}</td>
                    </tr>
                    <tr>
                        <th>科目</th>
                        <td>${subject_name}</td>
                    </tr>
                    <tr>
                        <th>問題</th>
                        <td>${quiz.question}</td>
                    </tr>
                    <tr>
                        <th>正解</th>
                        <td>${quiz.answer}</td>
                    </tr>
                    <tr>
                        <th>説明</th>
                        <td>${quiz.explanation}</td>
                    </tr>
                    <tr>
                        <th>難易度</th>
                        <td>${difficulty_name}</td>
                    </tr>
                </table>
                <br>
                
                <a class="button" href="quizedit?id=${quiz.id}">編集</a>
                <a class="button" href="quizdestroy?id=${quiz.id}" onclick="quizDeleteButton()">削除</a>
            </div>

            <a class="button red wide" href="quizlist?subjectid=${quiz.subject_id}">
                戻る
            </a>
        </div>
        <script src="script/onclick.js" charset="utf-8"></script>
    </body>
</html>