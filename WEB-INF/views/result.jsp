<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>

        <div class="window-width align-center">
            <div class="white-area">

                <h1>結果</h1>
                <table>
                    <tr>
                        <th>科目</th>
                        <td>${subjectName}</td>
                    </tr>
                    <tr>
                        <th>正解数</th>
                        <td>${correctCount}問 / ${quizCount}問中 (${percentCorrect}%)</td>
                    </tr>
                    <tr>
                        <th>再出題<br>不要数</th>
                        <td>${noRetestCount}問 / ${quizCount}問中 (${percentNoRetest}%)</td>
                    </tr>
                </table>
            </div>

            <a class="button wide ${completed ? 'red' : 'blue'}" href="top">トップ画面へ</a>
            <a class="button wide red" ${completed ? 'hidden' : '' }
                href="toquiz?action=continue&lot-size=0">次の出題へ</a>
        </div>
    </body>
</html>