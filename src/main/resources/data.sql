ALTER SEQUENCE products_id_seq RESTART WITH 7;^
truncate table products cascade;^

insert into products(id, name)
values (1, 'Финансовая система'), (2,'Кадровая система'), (3,'Интеграионная система'), (4, 'ВКС'), (5, 'ЭДО'), (6,'Unkown');^

ALTER SEQUENCE infosystems_id_seq RESTART WITH 8;^
truncate table infosystems cascade;^
insert into infosystems(id, name, use_web_hook, url_web_hook, primary_product_id) values
(1, '1C-Бухгалтерия', false, '',1),
(2,'Diasoft', false, '',1),
(3,'Портал', false, '',2),
(4, 'ВКС', false, '',4),
(5, 'Kafka', false, '',3),
--(6, 'Контур Толк', true, 'http://localhost:8081/api/webhooktest');
(6, 'Контур Толк', true, 'http://localhost:8081/api/webhooktest',4),
(7, 'Unkown', false, '',6);^


ALTER SEQUENCE task_types_id_seq RESTART WITH 4;^
truncate table task_types cascade;^
insert into task_types(id, name) values
 (1, 'Ошибка'),
 (2, 'Доработка'),
 (3, 'Настройка');^

ALTER SEQUENCE users_id_seq RESTART WITH 4;^
INSERT INTO users(id, Username, Password, EMail,inform_about_tasks) VALUES
 (1, 'USER', '$2a$12$3xhExkLmROmGyfVAAFq16.UWFVJx3qEUBK9Lxe1Is.X3geAs9ttvC','user@mail.ru', true),
 (2, 'ADMIN','$2a$12$3xhExkLmROmGyfVAAFq16.UWFVJx3qEUBK9Lxe1Is.X3geAs9ttvC','admin@mail.ru', false),
 (3, 'SYSTEM','','system@mail.ru', false);
 ^

INSERT INTO roles(id, name)
  VALUES (1, 'USER'), (2, 'ADMIN');
  ^

insert into users_roles(user_id, role_id)
values (1, 1), (2, 2), (3, 2);
^



ALTER SEQUENCE available_user_products_id_seq RESTART WITH 7;
insert into available_user_products(id, user_id, product_id) values
(1, 2, 1), --ADMIN
(2, 2, 2), --ADMIN
(3, 2, 3),  --ADMIN
(4, 2, 4);  --ADMIN
--(1, 1, 1),
--(2, 1, 4),
^



--INSERT INTO acl_sid (id, principal, sid) VALUES
--(1, true, 'ADMIN'),
--(2, true, 'USER'),
--(3, false, 'ROLE_ADMIN'),
--(4, false, 'ROLE_USER');

ALTER SEQUENCE acl_sid_id_seq RESTART WITH 1;^
INSERT INTO acl_sid (principal, sid) VALUES
(true, 'ADMIN'),
(true, 'USER'),
(false, 'ROLE_ADMIN'),
(false, 'ROLE_USER');
^


--INSERT INTO acl_class (id, class) VALUES
--(1, 'ru.otus.hw.models.Product');
--(2, 'ru.otus.hw.models.Book'),

ALTER SEQUENCE acl_class_id_seq RESTART WITH 1;
INSERT INTO acl_class (class) VALUES
('ru.otus.hw.models.Product'),
('ru.otus.hw.models.Book');

^

--(3, 'ru.otus.hw.models.Task');

-- таблицу acl_object_identity заполняю при запуске приложения, это критично, т.к. id заполняется по sequence, и он ничего не значет про INSERT при запуске
--INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
--(1, 2, 1, NULL, 3, false),-- book
--(2, 2, 2, NULL, 3, false),
--(3, 2, 3, NULL, 3, false);

ALTER SEQUENCE acl_object_identity_id_seq RESTART WITH 1;

INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, NULL, 3, false),-- Product
(1, 2, NULL, 3, false),
(1, 3, NULL, 3, false),
(1, 4, NULL, 3, false),
(1, 5, NULL, 3, false),
(1, 6, NULL, 3, false);
^

--(4, 2, 1, NULL, 3, false),--Product ROLE_ADMIN
--(5, 2, 2, NULL, 3, false),--Product ROLE_ADMIN
--(6, 2, 3, NULL, 3, false),--Product ROLE_ADMIN
--(7, 2, 4, NULL, 3, false);--Product ROLE_ADMIN
--(8, 3, 1, NULL, 3, 0),--Task ROLE_ADMIN
--(9, 3, 2, NULL, 3, 0),--Task ROLE_ADMIN
--(10, 3, 3, NULL, 3, 0),--Task ROLE_ADMIN
--(11, 3, 4, NULL, 2, 0);--Task ROLE_ADMIN


-- таблицу acl_entry заполняю при запуске приложения, это критично, т.к. id заполняется по sequence, и он ничего не значет про INSERT при запуске
--INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
--                       granting, audit_success, audit_failure) VALUES
--книга 1
--(1, 1, 1, 1, 1, true, true, true),--ADMIN READ
--(2, 1, 2, 1, 15, true, true, true),--ADMIN FULL
--(2, 1, 2, 1, 2, true, true, true),--ADMIN WRITE
--(8, 1, 4, 1, 8, true, true, true),--ADMIN DELETE
--(3, 1, 3, 3, 1, true, true, true),--ROLE_ADMIN READ
--книга 2
--(4, 2, 1, 2, 1, true, true, true),
--(5, 2, 2, 3, 1, true, true, true),
--книга 3
--(6, 3, 1, 3, 1, true, true, true),
--(7, 3, 2, 3, 2, true, true, true);

--продукт 3(id=6)
--(9, 6, 1, 2, 1, true, true, true),--USER
--(10, 6, 2, 2, 2, true, true, true),--USER
--(11, 7, 1, 1, 1, true, true, true),--ADMIN
--(12, 7, 2, 1, 2, true, true, true),--ADMIN
--(13, 4, 1, 3, 1, true, true, true);--ROLE_ADMIN

ALTER SEQUENCE acl_entry_id_seq RESTART WITH 1;

INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
--продукт 3(id=6)
(1, 1, 3, 1, true, true, true),--ROLE_ADMIN
(1, 2, 3, 2, true, true, true),--ROLE_ADMIN
(2, 1, 3, 1, true, true, true),--ROLE_ADMIN
(2, 2, 3, 2, true, true, true),--ROLE_ADMIN
(3, 1, 3, 1, true, true, true),--ROLE_ADMIN
(3, 2, 3, 2, true, true, true),--ROLE_ADMIN
(4, 1, 3, 1, true, true, true),--ROLE_ADMIN
(4, 2, 3, 2, true, true, true),--ROLE_ADMIN
(5, 1, 3, 1, true, true, true),--ROLE_ADMIN
(5, 2, 3, 2, true, true, true),--ROLE_ADMIN
(6, 1, 3, 1, true, true, true),--ROLE_ADMIN
(6, 2, 3, 2, true, true, true),--ROLE_ADMIN

(1, 3, 2, 1, true, true, true),--USER
(1, 4, 2, 2, true, true, true);--USER
^

ALTER SEQUENCE tasks_id_seq RESTART WITH 6;
insert into tasks(id, name, product_id,tasktype_id, author_id, laborcosts, priority, created) values
(1, 'Доработать 1C-Бухгалтерия',1,1,1,0,0,'2024-12-09 14:02:30'),
(2,' Ошибка в  Diasoft',2, 1,1,0,0,'2024-12-10 14:02:30'),
(3,'Обновить Портал', 3, 1,1,0,0,'2024-12-11 14:02:30'),
(4,'Усовершенствовать ВКС',4, 1,1,0,0,'2024-12-19 14:02:30'),
(5, 'Обновить 1C-Бухгалтерия',1,1,1,0,0,'2024-09-02 10:02:00');
^

--ALTER SEQUENCE comments_id_seq RESTART WITH 5;
insert into comments(comment, task_id, author_id, created)
values ('comment_1_1', 1,1, '2024-12-09 14:02:30'), ('comment_1_2', 1, 2, '2025-02-09 10:10:30'), ('comment_2_1', 2, 1, '2025-01-10 16:30:30'), ('comment_3_1', 3, 2, '2025-01-10 11:30:30');
^

ALTER SEQUENCE sprints_id_seq RESTART WITH 3;
insert into sprints(id, name, product_id, begin_date, end_date) values
(1, 'Спринт 1',1,'2024-12-09 14:02:30','2025-01-09 14:02:30'),
(2,' Спринт 2',2,'2024-10-10 14:02:30','2024-11-10 14:02:30');
^

ALTER SEQUENCE sprint_composition_id_seq RESTART WITH 3;
insert into sprint_composition(id, sprint_id, task_id) values
(1, 1, 1),
(2, 2, 2),
(3, 1, 5);
^


