ALTER TABLE USERS
ADD COLUMN role_id INT references ROLES(id) default NULL;