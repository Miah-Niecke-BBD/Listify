CREATE LOGIN apiUser1 WITH PASSWORD = 'X7s*3F$1wZ@qP9kY';
USE ListifyDB;  
CREATE USER apiUser1 FOR LOGIN apiUser1;

GRANT SELECT ON DATABASE::ListifyDB TO apiUser1;
GRANT EXECUTE TO apiUser1;
REVOKE INSERT, UPDATE, DELETE ON DATABASE::ListifyDB TO apiUser1;


CREATE LOGIN apiUser2 WITH PASSWORD = 'G2h#8VbZ!1xT&7pR';
USE ListifyDB;  
CREATE USER apiUser2 FOR LOGIN apiUser2;

GRANT SELECT ON DATABASE::ListifyDB TO apiUser2;
GRANT EXECUTE TO apiUser2;
REVOKE INSERT, UPDATE, DELETE ON DATABASE::ListifyDB TO apiUser2;


CREATE LOGIN apiUser3 WITH PASSWORD = 'L4k@9WxPz6Q!tYjB';
CREATE USER apiUser3 FOR LOGIN apiUser3;

GRANT SELECT ON DATABASE::ListifyDB TO apiUser3;
GRANT EXECUTE TO apiUser3;
REVOKE INSERT, UPDATE, DELETE ON DATABASE::ListifyDB TO apiUser3;


CREATE LOGIN backupUser WITH PASSWORD = 'Backup123!';
CREATE USER backupUser FOR LOGIN backupUser;

GRANT SELECT ON DATABASE::ListifyDB TO backupUser;
GRANT EXECUTE TO backupUser;
GRANT INSERT, UPDATE, DELETE ON DATABASE::ListifyDB TO backupUser;