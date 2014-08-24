alter table members add column company_id bigint;
alter table members add constraint members_company_id_fk foreign key (company_id) references companies(id);

