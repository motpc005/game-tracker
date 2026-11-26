-- game tracker: database structure
-- run this in MySQL Workbench to rebuild the table from scratch

create database if not exists game_tracker;
use game_tracker;

create table if not exists `game` (
  `id`            int          not null auto_increment,
  `title`         varchar(100) not null,
  `genre`         varchar(50)  not null,
  `platform`      varchar(20)  not null default 'steam',
  `status`        varchar(20)  not null default 'backlog',  -- backlog / playing / finished / replay
  `rating`        int          default null,                -- 1 to 10, enforced below
  `hours_played`  decimal(5,1) default null,
  `notes`         text,
  `date_added`    date         default (curdate()),
  `date_finished` date         default null,
  primary key (`id`),
  constraint `game_chk_1` check ((`rating` between 1 and 10))
) engine=InnoDB default charset=utf8mb4 collate=utf8mb4_0900_ai_ci;