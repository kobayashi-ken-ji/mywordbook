package jp.co.wordbook.controllers;

/**
 * nullチェックを行うユーティリティー
 */
public class NoNull {
    
    /**
     * 文字列をint型に変換
     * @param string            文字列
     * @param valueWhenNull     文字列が null または 解析不能 の場合に返す値
     * @return int型に変換された値
     */
    public static int parseInt(String string, int valueWhenNull) {
        
        try {
            return Integer.parseInt(string);

        } catch (NumberFormatException e) {
            // e.printStackTrace();
            return valueWhenNull;
        }
    }
}
