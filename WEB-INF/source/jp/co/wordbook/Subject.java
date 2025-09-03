package jp.co.wordbook;
import java.sql.*;

// DBの科目情報を格納する
public class Subject implements DatabaseTable
{
    public int id;
    public String name;

    // コンストラクタ
    public Subject(int id, String name) {
        this.id = id;
        this.name = name;
    }


    // 自クラスを生成
    @Override
    public Subject create(ResultSet results) throws SQLException {

        return new Subject(
            results.getInt("id"),
            results.getString("name")
        );
    }
}