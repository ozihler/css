create table memberships (
    id varchar(36) primary key,
    normalized_email varchar(320) not null,
    member_name varchar(200) not null,
    plan_code varchar(32) not null,
    activated_on date not null,
    paused_from date,
    resume_on date,
    version bigint not null default 0,
    constraint uk_memberships_normalized_email unique (normalized_email),
    constraint ck_memberships_plan_code check (plan_code in ('STANDARD', 'PREMIUM')),
    constraint ck_memberships_pause_dates check (
        (paused_from is null and resume_on is null)
        or (paused_from is not null and resume_on is not null and resume_on > paused_from)
    )
);
