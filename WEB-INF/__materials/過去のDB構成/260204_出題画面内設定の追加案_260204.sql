-- DB構成_変更案_260204


-- 現在出題中の情報 テーブル (26.02.04 変更)
CREATE TABLE user_quiz_settings (
    user_id           VARCHAR(255) NOT NULL PRIMARY KEY, 
    subject_id        INT,               -- 科目ID
    difficulty_mask   TINYINT NOT NULL,  -- 難易度id 立っているビット=難易度id
    is_swap_mode      BOOLEAN NOT NULL,  -- 問題文と正解文を入れ替えるフラグ
    lot_size          INT NOT NULL,      -- 一度に出題する問題数
    answered_count    INT NOT NULL,      -- 既出問題数

    -- 追加分 (toquiz.jsp内の設定)
    is_speech_enabled BOOLEAN NOT NULL,  -- 読み上げ機能の 有効/無効
    is_input_enabled BOOLEAN NOT NULL   -- ユーザー回答欄の 有効/無効
);

------------------------------------------------------------------------------
-- 現在の構成から、更新するクエリ
------------------------------------------------------------------------------

-- DBを選択
USE mywordbook;

-- difficulties テーブル自体を削除
DROP TABLE difficulties;

-- -- カラムを挿入する場合は、DEFAULTが無いと NOT NULL にできない。
ALTER TABLE user_quiz_settings ADD is_speech_enabled BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE user_quiz_settings ADD is_input_enabled BOOLEAN NOT NULL DEFAULT FALSE;

-- -- DEFAULTを削除
ALTER TABLE user_quiz_settings ALTER COLUMN is_speech_enabled DROP DEFAULT;
ALTER TABLE user_quiz_settings ALTER COLUMN is_input_enabled DROP DEFAULT;
