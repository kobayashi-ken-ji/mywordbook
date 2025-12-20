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

                <div class="small-font">
                    <br>
                    現在、ポートフォリオとして公開しております。<br>
                    以下の共用アカウントでログインが可能です。<br><br>

                    ユーザーID : guest<br>
                    パスワード : guest<br>
                </div>
                <br>
                <table class="login">
                    <tr>
                        <th><label for="userid">ユーザーID</label></th>
                        <td><input type="text" id="userid" name="userid" required></td>
                    </tr>
                    <tr>
                        <th><label for="password">パスワード</label></th>
                        <td><input type="text" id="password" name="password" required> </td>
                    </tr>
                </table>
                <br>
                <button type="submit" class="red">ログイン</button>

            </form>
        </div>
    </body>
</html>