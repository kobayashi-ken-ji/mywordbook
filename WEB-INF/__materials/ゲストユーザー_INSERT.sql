-- ユーザー
INSERT
    INTO users(id, password)
    VALUES
    ("test", "test"),
    ("guest", "guest");


-- 科目
INSERT
    INTO subjects(name, user_id)
    VALUES
        ("英語 - 単語", "guest"),
        ("英語 - 熟語", "guest"),
        ("英語 - 構文", "guest"),
        ("英語 - 活用", "guest");

-- 問題
INSERT
    INTO quizzes(subject_id, difficulty_id, question, answer, explanation)
    VALUES
        (8, 2, "January", "1月", ""),
        (8, 2, "February", "2月", ""),
        (8, 2, "March", "3月", ""),
        (8, 2, "April", "4月", ""),
        (8, 2, "May", "5月", ""),
        (8, 2, "June", "6月", ""),
        (8, 2, "July", "7月", ""),
        (8, 2, "August", "8月", ""),
        (8, 2, "September", "9月", ""),
        (8, 2, "October", "10月", ""),
        (8, 2, "November", "11月", ""),
        (8, 2, "December", "12月", "");

INSERT
    INTO quizzes(subject_id, difficulty_id, question, answer, explanation)
    VALUES
        (9, 2, "kind of", "ちょっと, なんとなく, まぁね", ""),
        (9, 2, "How do you do?", "はじめまして, ごきげんよう", ""),
        (9, 2, "stay up late", "夜更かしする", ""),
        (9, 2, "be free", "暇なとき", ""),
        (9, 2, "all over the world", "世界中で", ""),
        (9, 2, "all day long", "一日中ずっと", "");

INSERT
    INTO quizzes(subject_id, difficulty_id, question, answer, explanation)
    VALUES
        (10, 2, "What ~ !", "なんて～なんだ! (名詞を含む語句)", "What a delisious cake!\nWhat a hot day!"),
        (10, 2, "How ~ !", "なんて～なんだ! (形容詞or副詞)", "How cute!\nHow fun!"),
        (10, 2, "How do you like ~", "〜はどうですか？ (感想を尋ねる)", "How do you like the taste?"),
        (10, 2, "be過去形 ~ing", "～していた [過去進行形]", "When I got home, my sister was watching TV.\nWhat were you doing then?\nI was not studying then."),
        (10, 2, "be動詞 going to 原形", "～するつもり, ～でしょう", "I am going to visit Australia next summer.\nAre you going to visit Australia next summer?\nMy father is not going to buy a car next month."),
        (10, 2, "will 原形", "～するつもり (その場で決めたこと)", "I will buy this bag.\nWill he play baseball next Sunday?\nHe will not play baseball next Sunday.");

INSERT
    INTO quizzes(subject_id, difficulty_id, question, answer, explanation)
    VALUES
        (11, 2, "be", "be - was/were - been", ""),
        (11, 2, "do", "do - did - done", "ドゥ ディドゥ ダン"),
        (11, 2, "will", "will - would", ""),
        (11, 2, "can", "can - could", ""),
        (11, 2, "go", "go - went - gone", ""),
        (11, 2, "break", "break - broke - broken", ""),
        (11, 2, "make", "make - made - made", ""),
        (11, 2, "take", "take - took - taken", ""),
        (11, 2, "speak", "speak - spoke - spoken", ""),
        (11, 2, "read", "read - read - read", "リード レッド レッド"),
        (11, 2, "write", "write - wrote - written", "ライト ロート リテン"),
        (11, 2, "know", "know - knew - known", ""),
        (11, 2, "buy", "buy - bought - bought", ""),
        (11, 2, "sell", "sell - sold - sold", ""),
        (11, 2, "come", "come - came - come", ""),
        (11, 2, "eat", "eat - ate - eaten", ""),
        (11, 2, "get", "get - got - got/gotten", ""),
        (11, 2, "give", "give - gave - given", ""),
        (11, 2, "have", "have - had - had", ""),
        (11, 2, "meet", "meet - met - met", ""),
        (11, 2, "run", "run - ran - run", ""),
        (11, 2, "teach", "teach - taught - taught", ""),
        (11, 2, "see", "see - saw - seen", ""),
        (11, 2, "say", "say - said - said", ""),
        (11, 2, "steal", "steal - stole - stolen", ""),
        (11, 2, "tell", "tell - told - told", ""),
        (11, 2, "lose", "lose - lost - lost", ""),
        (11, 2, "win", "win - won - won", ""),
        (11, 2, "catch", "catch - caught - caught", "キャッチ カート カート"),
        (11, 2, "send", "send - sent - sent", ""),
        (11, 2, "find", "find - found - found", ""),
        (11, 2, "wake", "wake - woke - woken", ""),
        (11, 2, "begin", "begin - began - begun", "ビギン ビギャン ビガン"),
        (11, 2, "leave", "leave - left - left", ""),
        (11, 2, "forget", "forget - forgot - forgotten", ""),
        (11, 2, "hear", "hear - heard - heard", "ヒア ハード ハード"),
        (11, 2, "think", "think - thought - thought", ""),
        (11, 2, "may", "may - might", ""),
        (11, 2, "spend", "spend - spent - spent", ""),
        (11, 2, "hold", "hold - held - held", ""),
        (11, 2, "bring", "bring - brought - brought", ""),
        (11, 2, "feel", "feel - felt - felt", ""),
        (11, 2, "build", "build - built - built", ""),
        (11, 2, "let", "let - let - let", ""),
        (11, 2, "grow", "grow - grew - grown", ""),
        (11, 2, "stand", "stand - stood - stood", ""),
        (11, 2, "understand", "understand - understood - understood", ""),
        (11, 2, "choose", "choose - chose - chosen", "チューズ チョーズ チョーゼン"),
        (11, 2, "keep", "keep - kept - kept", ""),
        (11, 2, "cut", "cut - cut - cut", ""),
        (11, 2, "sit", "sit - sat - sat", ""),
        (11, 2, "become", "become - became - become", ""),
        (11, 2, "fly", "fly - flew - flown", "フライ フルウ フローン"),
        (11, 2, "drive", "drive - drove - driven", "ドライヴ ドローヴ ドリヴン"),
        (11, 2, "put", "put - put - put", ""),
        (11, 2, "throw", "throw - threw - thrown", "");



