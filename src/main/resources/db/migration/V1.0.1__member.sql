-- 변경 사항 --
-- member 테이블 컬럼 추가 --

ALTER TABLE `member`
    ADD COLUMN `subject` TEXT NOT NULL,
    ADD COLUMN `name` VARCHAR(255) NOT NULL,
    ADD COLUMN `thumb_url` TEXT NOT NULL,
    ADD COLUMN `role` VARCHAR(255) NOT NULL;