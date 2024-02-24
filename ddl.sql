create table credits (account integer not null, credits integer not null);
partition table credits on column account;

create table debits (account integer not null, debits integer not null);
partition table debits on column account;
    
create stream totals export to target totals_file (total integer not null);
    
insert into debits values (0, 0);
insert into debits values (1, 0);
insert into debits values (2, 0);
insert into debits values (3, 0);
insert into debits values (4, 0);
insert into debits values (5, 0);
insert into debits values (6, 0);
insert into debits values (7, 0);
insert into debits values (8, 0);
insert into debits values (9, 0);

insert into credits values (0, 0);
insert into credits values (1, 0);
insert into credits values (2, 0);
insert into credits values (3, 0);
insert into credits values (4, 0);
insert into credits values (5, 0);
insert into credits values (6, 0);
insert into credits values (7, 0);
insert into credits values (8, 0);
insert into credits values (9, 0);

create procedure from class InsertCredit;
create procedure from class InsertDebit;
create compound procedure from class Transact;   
create procedure from class WriteTotal; 
create task ExportTotals on schedule every 1000 milliseconds procedure WriteTotal;

create procedure Total as select sum(balance) from 
(select credits.account as account, credits-debits as balance from credits, debits where credits.account = debits.account) a;
