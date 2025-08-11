-- очищаю базу

CREATE OR REPLACE FUNCTION public.drop_tables(
	username character varying)
    RETURNS void
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE
    statements CURSOR FOR
        SELECT tablename FROM pg_tables
        WHERE tableowner = username AND schemaname = 'public';
BEGIN
    FOR stmt IN statements LOOP
        EXECUTE 'drop TABLE ' || quote_ident(stmt.tablename) || ' CASCADE;';
    END LOOP;
END;
$BODY$;^

select drop_tables('postgres');^

-- объекты ACL Security

create table acl_sid(
	id bigserial not null primary key,
	principal boolean not null,
	sid varchar(100) not null,
	constraint unique_uk_1 unique(sid,principal)
);^

create table acl_class(
	id bigserial not null primary key,
	class varchar(100) not null,
	constraint unique_uk_2 unique(class)
);^

create table acl_object_identity(
	id bigserial primary key,
	object_id_class bigint not null,
	object_id_identity varchar(36) not null,
	parent_object bigint,
	owner_sid bigint,
	entries_inheriting boolean not null,
	constraint unique_uk_3 unique(object_id_class,object_id_identity),
	constraint foreign_fk_1 foreign key(parent_object)references acl_object_identity(id),
	constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
	constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id)
);^

create table acl_entry(
	id bigserial primary key,
	acl_object_identity bigint not null,
	ace_order int not null,
	sid bigint not null,
	mask integer not null,
	granting boolean not null,
	audit_success boolean not null,
	audit_failure boolean not null,
	constraint unique_uk_4 unique(acl_object_identity,ace_order),
	constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
	constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);^




-- прикладные объекты



create table users (
    id bigserial,
    Username varchar(50),
    EMail varchar(50),
    Password varchar(100),
    inform_about_tasks  boolean not null,
    primary key (id)
);^

create table roles (
    id bigserial,
    name varchar(100),
    primary key (id)
);^


create table users_roles (
    user_id bigint references users(id) on delete cascade,
    role_id bigint references roles(id) on delete cascade,
    primary key (user_id, role_id)
);^



create table products (
    id bigserial,
    name varchar(255),
    primary key (id)
);^

create table infosystems (
    id bigserial,
    name varchar(255),
    use_web_hook boolean not null,
    url_web_hook varchar(255),
    primary_product_id bigint references products(id) on delete cascade,
    primary key (id)
);^

create table available_user_products (
    id bigserial,
    user_id bigint not NULL references users(id) on delete cascade,
    product_id bigint not NULL references products(id) on delete cascade,
    primary key (id)
);^

create table task_types (
    id bigserial,
    name varchar(50),
    primary key (id)
);^

create table tasks (
    id bigserial,
    name varchar(255),
    created timestamp,
    laborcosts numeric(10,2),
    priority numeric(10,2),
    product_id bigint references products(id) on delete cascade,
    system_id bigint references infosystems(id) on delete cascade,
    author_id bigint references users(id) on delete cascade,
    responsible_id bigint references users(id) on delete cascade,
    programmer_id bigint references users(id) on delete cascade,
    analyst_id bigint references users(id) on delete cascade,
    tasktype_id bigint references task_types(id) on delete cascade,
    description TEXT,
    primary key (id)
);^
--    private long id;
--    private String name; //name
--    private Float laborcosts; //laborcosts
--    private Float priority;//priority
--    private Product product;//product_id
--    private System infoSystem;//system_id
--    private CustomUser author;//author_id
--    private CustomUser responsible;//responsible_id
--    private TaskType taskType;//taskType_id
--    private String description;//description



create table comments (
    id bigserial,
    task_id bigint references tasks(id) on delete cascade,
    comment varchar(255),
    author_id bigint references users(id) on delete cascade,
    created timestamp,
    primary key (id)
);^


create table sprints (
    id bigserial,
    name varchar(255),
    product_id bigint references products(id) on delete cascade,
    begin_date timestamp,
    end_date timestamp,
    primary key (id)
);^

create table sprint_composition (
    id bigserial,
    sprint_id bigint references sprints(id) on delete cascade,
    task_id bigint references tasks(id) on delete cascade,
    primary key (id)
);^


