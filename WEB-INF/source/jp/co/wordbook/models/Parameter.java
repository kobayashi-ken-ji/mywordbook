package jp.co.wordbook.models;

import javax.servlet.http.*;

/**
 * リクエストのパラメータを取得するユーティリティー。
 * 不正な入力を ParameterException として投げる。
 */
public class Parameter {

    /**
     * リクエストのパラメータから数値を取得
     * @param request               サーブレットのリクエスト
     * @param parameterName         リクエストから取得するパラメータの名前
     * @return                      0以上の整数
     * @throws ParameterException   文字列を数値に変換できない場合、数値が0未満の場合
     */
    public static int getInt(HttpServletRequest request, String parameterName) 
        throws ParameterException
    {
        int number = -1;
        
        try {
            number = Integer.parseInt(request.getParameter(parameterName));
        } catch (NumberFormatException e) {
            throw new ParameterException("Stringをintへ変換できません。");
        }

        if (number < 0)
            throw new ParameterException("数値が0未満です。");
        
        return number;
    }


    /**
     * リクエストのパラメータから文字列を取得
     * @param request               サーブレットのリクエスト
     * @param parameterName         リクエストから取得するパラメータの名前
     * @return                      文字列 (nullなし)
     * @throws ParameterException   文字列がnullの場合
     */
    public static String getString(HttpServletRequest request, String parameterName)
        throws ParameterException
    {
        String string = request.getParameter(parameterName);

        if (string == null)
            throw new ParameterException("文字列がnullです。");

        return string;
    }

    
    /**
     * パラメータから文字列配列を取得
     * @param request               サーブレットのリクエスト
     * @param parameterName         リクエストから取得するパラメータの名前
     * @return                      文字列配列 (nullなし)
     * @throws ParameterException   配列がnullの場合
     */
    public static String[] getStringArray(HttpServletRequest request, String parameterName)
        throws ParameterException
    {
        String[] strings = request.getParameterValues(parameterName);

        if (strings == null)
            throw new ParameterException("文字列配列がnullです。");

        return strings;
    }
}
