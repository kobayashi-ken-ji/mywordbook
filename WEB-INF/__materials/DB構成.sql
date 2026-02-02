-- mywordbookデータベースの情報
--      ・テーブルの構成
--      ・レコードの挿入、削除、変更 のメモ

------------------------------------------------------------------------------
-- データベースを作成
------------------------------------------------------------------------------

CREATE DATABASE mywordbook CHARACTER SET utf8 COLLATE utf8_general_ci;
SHOW DATABASES;     -- DB一覧
USE mywordbook;     -- DBを選択
SELECT DATABASE();  -- 選択中のDBを表示
DROP DATABASE  mywordbook;  -- 削除

------------------------------------------------------------------------------
-- テーブルを作成
------------------------------------------------------------------------------

-- 問題テーブル (26.01.31 修正)
CREATE TABLE quizzes (
    id              INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    subject_id      INT NOT NULL,           -- 科目ID (各ユーザーが保有、他者と共有されない)
    difficulty_id   INT NOT NULL,           -- 難易度ID
    question        VARCHAR(255) NOT NULL,  -- 問題文
    answer          VARCHAR(255) NOT NULL,  -- 正解文
    explanation     TEXT,                   -- 説明文
    is_asked        BOOLEAN NOT NULL,       -- 出題済みフラグ (内部的にTINYINT)

    -- 複合インデックス
    -- 検索条件は「データ取得」の項目を参照
    -- idまで含めると、ランダム抽出の際にメモリ内だけで処理が完結する
    INDEX idx_subj_asked_diff_id (subject_id, is_asked, difficulty_id, id)
);

-- 科目テーブル
CREATE TABLE subjects (
    id      INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(255) NOT NULL,  -- 科目名
    user_id VARCHAR(255) NOT NULL   -- ユーザーID
);

-- 難易度テーブル
CREATE TABLE difficulties (
    id      INT NOT NULL PRIMARY KEY,   -- 1, 2, 3, 4
    name    VARCHAR(15) NOT NULL        -- 完璧, ほぼ覚, うろ覚, 苦手
);

-- ユーザーテーブル
CREATE TABLE users (
    id       VARCHAR(255) NOT NULL PRIMARY KEY,  -- ユーザーID
    password VARCHAR(255) NOT NULL               -- パスワード
);

-- 現在出題中の情報 テーブル (26.01.31 新規)
CREATE TABLE user_quiz_settings (
    user_id           VARCHAR(255) NOT NULL PRIMARY KEY, 
    subject_id        INT,               -- 科目ID
    difficulty_mask   TINYINT NOT NULL,  -- 難易度id 立っているビット=難易度id
    is_swap_mode      BOOLEAN NOT NULL,  -- 問題文と正解文を入れ替えるフラグ
    lot_size          INT NOT NULL,      -- 一度に出題する問題数
    answered_count    INT NOT NULL       -- 既出問題数
);

------------------------------------------------------------------------------
-- データ挿入
------------------------------------------------------------------------------

-- 難易度 (26.01.31 修正)
INSERT
    INTO difficulties(id, name)
    VALUES
        (1, "完璧"),
        (2, "ほぼ覚"),
        (3, "うろ覚"),
        (4, "苦手");


-- 科目名
INSERT
    INTO subjects(id, name, user_id)
    VALUES
        (1, "英語 - 単語", "guest"),
        (2, "英語 - 熟語", "guest"),
        (3, "英語 - 構文", "guest"),
        (4, "英語 - 活用", "guest");

INSERT
    INTO subjects(name, user_id)
    VALUES
        ("英語 - 単語", "guest"),
        ("英語 - 熟語", "guest"),
        ("英語 - 構文", "guest"),
        ("英語 - 活用", "guest");

-- 問題
--  【Mery】JSON→SQL挿入文.js  を使用してINSERT文を作成
INSERT 
    INTO quizzes(subject_id, difficulty_id, question, answer, explanation)
    VALUES
        (1, 2, "January" , "1月", ""),
        (1, 2, "February", "2月", "");

-- ユーザー
INSERT
    INTO users(id, password)
    VALUES
    ("test", "test"),
    ("guest", "guest");

------------------------------------------------------------------------------
-- データ取得
------------------------------------------------------------------------------

-- レコード一覧
SELECT * FROM difficulties;
SELECT * FROM subjects;
SELECT * FROM quizzes LIMIT 100;
SELECT * FROM users;
SELECT * FROM user_quiz_settings;

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


-- 問題テーブルから、ユーザーの未出題問題を取得する (複合インデックスを使用)
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
-- データ削除
------------------------------------------------------------------------------

-- レコードの削除
DELETE FROM quizzes WHERE subject_id = 2;

-- テーブル内全てのレコードを削除
DELETE FROM difficulties;

------------------------------------------------------------------------------
-- データ変更
------------------------------------------------------------------------------

UPDATE users SET id="b" WHERE id="a";
UPDATE users SET password="b" WHERE password="a";
UPDATE subjects SET user_id="guest" WHERE user_id="a";


-- 現在の構成から、更新するクエリ
--      ・カラムを増やす (既にあるレコードの為に、初期値をFALSEにする)
--      ・DEFAULT指定を外す (Java側で初期値を指定することを明確化)
--      ・複合インデックスを作成
ALTER TABLE quizzes ADD is_asked BOOLEAN NOT NULL DEFAULT FALSE; 
ALTER TABLE quizzes ALTER COLUMN is_asked DROP DEFAULT;
ALTER TABLE quizzes ADD INDEX idx_subj_asked_diff_id (subject_id, is_asked, difficulty_id, id);


-- usersテーブルからカラムを分けたとき、ユーザー数と同数のレコードを作成する
--      IGNORE   新しいユーザーだけ追加し、既存のユーザーはそのまま
--      id の部分のみ users から取得している
INSERT IGNORE
    INTO user_quiz_settings
    SELECT id, NULL, 4, FALSE, 20, 0 FROM users;

-- IN句を使った更新
UPDATE quizzes
SET is_asked = TRUE
WHERE id IN (46, 47);


-- 出題設定を1レコード編集する
UPDATE user_quiz_settings
SET subject_id = 1, difficulty_mask = 2, is_swap_mode = true, lot_size = 20
WHERE user_id = 'a';


-- ユーザの保持するクイズの、出題済みフラグを変更 (IN句使用よりも高速)
UPDATE quizzes
JOIN subjects ON quizzes.subject_id = subjects.id
SET quizzes.is_asked = FALSE
WHERE subjects.user_id = 'a' AND quizzes.is_asked = TRUE;

-- IN句版
UPDATE quizzes
SET is_asked = FALSE
WHERE subject_id IN (
    SELECT id
    FROM subjects
    WHERE user_id = 'a'
);
