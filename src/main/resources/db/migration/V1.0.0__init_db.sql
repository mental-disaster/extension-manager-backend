-- 확장자 테이블
CREATE TABLE IF NOT EXISTS extension (
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    name            TEXT NOT NULL UNIQUE CHECK(length(name) <= 20), -- 확장자명(ex. sh, exe)
    type            TEXT NOT NULL CHECK(type IN ('FIXED', 'CUSTOM')) DEFAULT 'CUSTOM', -- 확장자 타입
    is_blocked      BOOLEAN NOT NULL DEFAULT TRUE, -- 차단 여부
    created_at      TEXT NOT NULL DEFAULT (datetime('now')), -- 생성 일자
    updated_at      TEXT NOT NULL DEFAULT (datetime('now')) -- 수정 일자
);

-- 기본 고정 확장자 데이터
INSERT INTO extension(name, type, is_blocked)
VALUES
    ('bat', 'FIXED', FALSE),
    ('cmd', 'FIXED', FALSE),
    ('com', 'FIXED', FALSE),
    ('cpl', 'FIXED', FALSE),
    ('exe', 'FIXED', FALSE),
    ('scr', 'FIXED', FALSE),
    ('js', 'FIXED', FALSE)
ON CONFLICT(name) DO NOTHING ;
