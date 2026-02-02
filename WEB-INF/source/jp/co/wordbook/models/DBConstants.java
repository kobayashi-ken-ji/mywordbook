package jp.co.wordbook.models;

// データベースへ接続するための情報
public class DBConstants {

    // インスタンス化を防ぐために private化
    private DBConstants() {}

    // [!] LinuxではURLにUTF8と明記する必要がある
    public static final String
        URL      = "jdbc:mysql://localhost/mywordbook?useUnicode=true&characterEncoding=UTF-8",
        USER     = "root",
        PASSWORD = "",
        JDBC     = "com.mysql.jdbc.Driver";
}
