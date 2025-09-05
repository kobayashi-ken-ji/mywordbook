-- mywordbook
--      SQL文のバックアップ

------------------------------------------------------------------------------
-- テーブルを作成
------------------------------------------------------------------------------

-- 問題テーブル
CREATE TABLE quizzes (
    id              INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    subject_id      INT,            -- 科目ID
    difficulty_id   INT,            -- 難易度ID
    question        VARCHAR(255),   -- 問題文
    answer          VARCHAR(255),   -- 正解文
    explanation     TEXT            -- 説明文
);

-- 科目テーブル
CREATE TABLE subjects (
    id      INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(255)                -- 科目名
);

-- 難易度テーブル
CREATE TABLE difficulties (
    id      INT NOT NULL PRIMARY KEY,   -- 1, 2, 3
    name    VARCHAR(15)                 -- 簡単, 普通, 難しい
);

------------------------------------------------------------------------------
-- データ挿入
------------------------------------------------------------------------------

-- 難易度
INSERT
    INTO difficulties(id, name)
    VALUES
        (1, "簡単"),
        (2, "普通"),
        (3, "難しい");

-- 科目名
INSERT
    INTO subjects(id, name)
    VALUES
        (1, "英語 - 単語"),
        (2, "英語 - 熟語"),
        (3, "英語 - 構文"),
        (4, "英語 - 活用");

-- 問題
--  【Mery】JSON→SQL挿入文.js  を使用してINSERT文を作成
INSERT 
    INTO quizzes(subject_id, difficulty_id, question, answer, explanation)
    VALUES
        (1, 2, "January" , "1月", ""),
        (1, 2, "February", "2月", "");


------------------------------------------------------------------------------
-- データ取得
------------------------------------------------------------------------------

-- ID, 問題文, 正解文, 説明文, 科目名, 難易度名
SELECT quizzes.id, question, answer, explanation, subjects.name, difficulties.name
FROM quizzes 
    LEFT JOIN subjects ON quizzes.subject_id = subjects.id
    LEFT JOIN difficulties ON quizzes.difficulty_id = difficulties.id;


-- クイズ情報 + 科目名, 難易度名
SELECT quizzes.id, subject_id, difficulty_id, explanation, question, answer, subjects.name AS subject_name, difficulties.name AS difficulty_name
FROM quizzes
	LEFT JOIN subjects ON subjects.id = quizzes.subject_id
    LEFT JOIN difficulties ON difficulties.id = quizzes.difficulty_id
WHERE quizzes.id = 1;