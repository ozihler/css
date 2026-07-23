create index idx_memberships_admin_listing
    on memberships (activated_on desc, id asc);

create index idx_memberships_admin_status_lookup
    on memberships (paused_from, resume_on, activated_on desc, id asc);
