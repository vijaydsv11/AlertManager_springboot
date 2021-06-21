INSERT INTO role (id,role_name,status,dateCreated) VALUES (1,'SUPER_ADMIN','ACTIVE', NULL);
INSERT INTO role (id,role_name,status,dateCreated) VALUES (2,'MANAGER','INACTIVE', NULL);
INSERT INTO role (id,role_name,status,dateCreated) VALUES (3,'USER','ACTIVE',NULL);
INSERT INTO `Risk_Assessment`.`risk_user` (`username`, `password`, `user_firstname`, `user_lastname`, `user_middlename`, `user_email`, 
 `employee_id`, `department`, `phone_no`, `invalid_auth_req_count`, `last_password_reset`,
 `role_id`) VALUES ('Admin', 'password', 'admin', '', '', 'admin@gmail.com', '001', '', '', '0', NULL, '1');


INSERT INTO module (id,name,dateCreated) VALUES (1,'Risk', NULL);
INSERT INTO module (id,name,dateCreated) VALUES (2,'CR', NULL);


 
