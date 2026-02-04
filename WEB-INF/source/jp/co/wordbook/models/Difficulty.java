package jp.co.wordbook.models;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/** 難易度のID、UI名を定義 */
public class Difficulty
{
    /** 難易度のマップ (ID, UI名) */
    public static final Map<Integer, String> MAP;
    static {
        // 順序を保持
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "完璧");
        map.put(2, "ほぼ覚");
        map.put(3, "うろ覚");
        map.put(4, "苦手");

        // 内部も変更不可能化
        MAP = Collections.unmodifiableMap(map);
    }

    /** 難易度IDが有効か否か */
    public static boolean isValid(int difficultyId) {
        return MAP.containsKey(difficultyId);
    }
}
