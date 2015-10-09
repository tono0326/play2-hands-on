DROP TABLE IF EXISTS USERS;
create table USERS (
 ID IDENTITY,
 NAME VARCHAR NOT NULL,
 COMPANY_ID INTEGER,
 EMAIL VARCHAR NOT NULL,
 PASSWORD VARCHAR NOT NULL);

DROP TABLE IF EXISTS COMPANIES;
create table COMPANIES (ID INTEGER NOT NULL, NAME VARCHAR NOT NULL);

alter table USERS ADD CONSTRAINT IDX_USERS_FK0 FOREIGN KEY (COMPANY_ID) REFERENCES COMPANIES (ID);
alter table COMPANIES ADD CONSTRAINT IDX_COMPANIES_PK PRIMARY KEY (ID);

insert into COMPANIES values (1, 'Biz Reach');
insert into COMPANIES values (2, 'Recruit');
insert into COMPANIES values (3, 'DODA');

insert into USERS(NAME, COMPANY_ID, EMAIL, PASSWORD) values ('Naoki Takezoe', 1, 'takezoe@bizreach.com', 'takezoe');
insert into USERS(NAME, EMAIL, PASSWORD) values ('Takako Shimamoto', 'shimamoto@bizreach.com', 'shimamoto');

