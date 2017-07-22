INSERT INTO role (id, description, name) VALUES
(1,	'Super user for Tennant management',	'ROLE_MASTER'),
(2,	'Admin User For Tenants',	'ROLE_ADMIN');

INSERT INTO users (id, modified_at, enabled, account_expired, account_locked, credential_expired, email, mobile, name, otp, otp_sent_at, password, retry_count, username, company_id) VALUES
(1,	'2017-06-28 00:25:42.25',	true, false,	false,	false,	'zahid7292@gmail.com', '8987525008',	'Md Zahid Raza',	NULL,	NULL,	'$2a$10$8gha167uTM3jhArZwFk6UOxgNChnMhWfrR59ND/TtzvTynwo2129C',	NULL,	'zahid7292',	NULL),
(2,	'2017-06-28 00:25:42.25',	true, false,	false,	false,	'taufeeque@gmail.com', '8987525008',	'Md Taufeeque Alam',	NULL,	NULL,	'$2a$10$8gha167uTM3jhArZwFk6UOxgNChnMhWfrR59ND/TtzvTynwo2129C',	NULL,	'taufeeque8',	NULL),
(3,	'2017-06-28 00:25:42.25',	true, false,	false,	false,	'jawed.akhtar1993@gmail.com', '8987525008',	'Md Jawed Akhtar',	NULL,	NULL,	'$2a$10$8gha167uTM3jhArZwFk6UOxgNChnMhWfrR59ND/TtzvTynwo2129C',	NULL,	'jawed_akhtar',	NULL);

INSERT INTO user_role_rel (user_id, role_id) VALUES
(1,	1),
(2, 2),
(3, 1);

INSERT INTO `company` (`id`,`enabled`,`modified_at`,`address`,`db_name`,`description`,`name`) VALUES
(1,'1','2017-07-22 22:09:55','Bangalore','tnt_tna_laguna',NULL,'Laguna Clothing Pvt. Ltd.');