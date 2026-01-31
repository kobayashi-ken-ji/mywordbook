package jp.co.wordbook.models;

/**
 * リクエストのパラメータ値の不正を表す例外。
 * 以下のクラスからthrowされる。
 * @see Parameter
 * @see QuizDAO
 * @see SubjectDAO
 * @see DifficultyDAO
 * @see QuizSettingDAO
 */
public class ParameterException extends Exception {
    
    public ParameterException(String message) {
        super(message);
    }
}
