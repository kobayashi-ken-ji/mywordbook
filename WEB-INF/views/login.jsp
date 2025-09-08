<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
    <%@ include file="header.jsp"%>

    <body class="bg-main">
        <div class="hedder">単語帳アプリ</div>

        <div class="window-width align-center">
            <form class="white-area" action="login" method="post">

                <h1>ログイン</h1>
                <p>${message}</p>
                <br>
                <div>
                    <label for="userid">ユーザーID</label>
                    <input type="text" id="userid" name="userid" required> 
                </div>
                <div>
                    <label for="password">パスワード</label>
                    <input type="text" id="password" name="password" required> 
                </div>
                <br>
                <button type="submit" class="red">ログイン</button>

            </form>
        </div>
    </body>
</html>