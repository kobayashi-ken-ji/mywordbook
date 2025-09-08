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
                        <td>${subjectname}</td>
                    </tr>
                    <tr>
                        <th>正解数</th>
                        <td>${correctcount}問 / ${quizcount}問中</td>
                    </tr>
                    <tr>
                        <th>正答率</th>
                        <td>${rate}%</td>
                    </tr>
                </table>
            </div>

            <a class="button red wide" href="top">トップ画面へ</a>
        </div>
    </body>
</html>