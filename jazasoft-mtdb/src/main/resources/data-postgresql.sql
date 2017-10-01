
INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities,
 access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES
('client', 'secret', 'read,write,trust', 'password,authorization_code,refresh_token,implicit', null, null, 43200, 43200, null, true),
('client-web', 'secret-web', 'read,write,trust', 'password,authorization_code,refresh_token,implicit', null, null, 3600, 7200, null, true);

--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO role (id, description, name) VALUES
(1,	'Super user for Tennant management',	'ROLE_MASTER'),
(2,	'Admin User For Tenants',	'ROLE_ADMIN');



--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mdzahidraza
--

--SELECT pg_catalog.setval('role_id_seq', 2, true);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO users (id, modified_at, enabled, account_expired, account_locked, credential_expired, email, mobile, name, otp, otp_sent_at, password, retry_count, username, company_id) VALUES
(1,	'2017-06-28 00:25:42.25',	true, false,	false,	false,	'zahid7292@gmail.com', '8987525008',	'Md Zahid Raza',	NULL,	NULL,	'$2a$10$8gha167uTM3jhArZwFk6UOxgNChnMhWfrR59ND/TtzvTynwo2129C',	NULL,	'zahid7292',	NULL);



--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mdzahidraza
--

--SELECT pg_catalog.setval('users_id_seq', 1, true);

--
-- Data for Name: user_role_rel; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO user_role_rel (user_id, role_id) VALUES
(1,	1);

