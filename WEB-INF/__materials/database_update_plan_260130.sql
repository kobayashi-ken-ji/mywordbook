-- データベースの構成を変更する案 260129

--------------------------------------------------------------
-- テーブル内の変更
--      DEFAULT値は全てJava側で定義する
--------------------------------------------------------------

-- 難易度テーブル ※変更なし
CREATE TABLE difficulties (
    id      INT NOT NULL PRIMARY KEY,   -- 1, 2, 3, 4
    name    VARCHAR(15)                 -- 完璧, ほぼ覚, うろ覚, 苦手
);

-- 難易度 改修案
INSERT
    INTO difficulties(id, name)
    VALUES
        (1, "完璧"),
        (2, "ほぼ覚"),
        (3, "うろ覚"),
        (4, "苦手");

--------------------------------------------------------------

-- ユーザーテーブル 改修案 → 切り分けることで変更しなくてよくなった
CREATE TABLE users (
    id       VARCHAR(255) NOT NULL PRIMARY KEY,  -- ユーザーID
    password VARCHAR(255) NOT NULL,              -- パスワード
    
    -- 現在出題中の情報
    -- active_subject_id        INT,               -- 科目ID
    -- active_difficulty_mask   TINYINT NOT,       -- 難易度id 立っているビット=難易度id
    -- active_answered_count    INT NOT NULL,      -- 既出題数
    -- active_question_lot_size INT NOT NULL       -- 一度に出題する問題数
);

--------------------------------------------------------------

-- 現在出題中の情報 テーブル
CREATE TABLE user_quiz_settings (
    user_id           VARCHAR(255) NOT NULL PRIMARY KEY, 
    subject_id        INT,               -- 科目ID
    difficulty_mask   TINYINT NOT NULL,  -- 難易度id 立っているビット=難易度id
    is_swap_mode      BOOLEAN NOT NULL,  -- 問題文と正解文を入れ替えるフラグ
    lot_size          INT NOT NULL,      -- 一度に出題する問題数
    answered_count    INT NOT NULL       -- 既出問題数
);

--------------------------------------------------------------

-- 問題テーブル 改修案
CREATE TABLE quizzes (
    id              INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    subject_id      INT NOT NULL,           -- 科目ID (各ユーザーが保有、他者と共有されない)
    difficulty_id   INT NOT NULL,           -- 難易度ID
    question        VARCHAR(255) NOT NULL,  -- 問題文
    answer          VARCHAR(255) NOT NULL,  -- 正解文
    explanation     TEXT,                   -- 説明文
    is_asked        BOOLEAN NOT NULL,       -- 出題済みフラグ (内部的にTINYINT)

    -- 複合インデックス
    -- 検索条件は下記を参照
    -- idまで含めると、ランダム抽出の際にメモリ内だけで処理が完結する
    INDEX idx_subj_asked_diff_id (subject_id, is_asked, difficulty_id, id)
);

-- 検索条件(簡易版)
SELECT *
FROM quizzes
WHERE
    subject_id = ? AND
    is_asked = FALSE AND
    difficulty_id IN (?, ...)   -- 以下に詳細
ORDER BY RAND()
LIMIT ?;


-- 検索条件(高速版)
-- 問題テーブルから、ユーザーの未出題問題を取得する
--      内部結合することで、ランダム抽出したIDで検索
--      ※ IN句を使うと、DBが「先にサブクエリを完了させるか否か」の判断で迷う可能性があるため
SELECT *
FROM quizzes
INNER JOIN (

    -- IDだけを高速にランダム抽出
    SELECT id
    FROM quizzes
    WHERE
        subject_id = ? AND
        is_asked = FALSE AND
        difficulty_id IN (?, ...)
    ORDER BY RAND()
    LIMIT ?

) AS random_ids ON quizzes.id = random_ids.id;


-- 未出題件数のみ取得
SELECT COUNT(*)
FROM quizzes
WHERE
    subject_id = ? AND
    is_asked = FALSE AND
    difficulty_id IN (?, ...);



------------------------------------------------------------------------------
-- 現在の構成から、更新するクエリ
------------------------------------------------------------------------------

-- -- カラムを挿入する場合は、DEFAULTが無いと NOT NULL にできない。
-- SHOW TABLES;
-- ALTER TABLE quizzes ADD is_asked BOOLEAN NOT NULL DEFAULT FALSE;            -- 出題済みフラグ
-- ALTER TABLE users ADD active_subject_id INT;                                -- 科目ID
-- ALTER TABLE users ADD active_difficulty_mask TINYINT NOT NULL DEFAULT 14;   -- 難易度id 立っているビット=難易度id
-- ALTER TABLE users ADD active_answered_count INT NOT NULL DEFAULT 0;         -- 既出題数
-- ALTER TABLE users ADD active_question_lot_size INT NOT NULL DEFAULT 10;     -- 一度に出題する問題数

-- -- DEFAULTを削除
-- ALTER TABLE quizzes ALTER COLUMN is_asked DROP DEFAULT;
-- ALTER TABLE users ALTER COLUMN active_difficulty_mask DROP DEFAULT;
-- ALTER TABLE users ALTER COLUMN active_answered_count DROP DEFAULT;
-- ALTER TABLE users ALTER COLUMN active_question_lot_size DROP DEFAULT;

-- 複合インデックスを設定
ALTER TABLE quizzes ADD INDEX idx_subj_asked_diff_id (subject_id, is_asked, difficulty_id, id);

-- 難易度名を削除 (その後に全てを挿入する)
DELETE FROM difficulties;

-- 出題状態テーブルに「ユーザーid、各カラムの初期値」を反映させる
-- IGNORE   新しいユーザーだけ追加し、既存のユーザーはそのまま
INSERT IGNORE
    INTO user_quiz_settings
    SELECT id, NULL, 4, FALSE, 20, 0 FROM users;

-- 出題設定を1レコード編集する
UPDATE user_quiz_settings
SET subject_id = 1, difficulty_mask = 2, is_swap_mode = true, lot_size = 20
WHERE user_id = 'a';



-- ユーザの保持するクイズの、出題済みフラグを変更
UPDATE quizzes
SET is_asked = FALSE
WHERE subject_id IN (
    SELECT id
    FROM subjects
    WHERE user_id = 'a'
);

-- こちらの方が高速な可能性がある
UPDATE quizzes
JOIN subjects ON quizzes.subject_id = subjects.id
SET quizzes.is_asked = FALSE
WHERE subjects.user_id = 'a' AND quizzes.is_asked = TRUE;


-- 出題済みフラグをTRUEに
UPDATE quizzes
SET is_asked = TRUE
WHERE id IN (46, 47);