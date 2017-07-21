--
INSERT INTO oauth_client_details (client_id, client_secret, scope, authorized_grant_types, web_server_redirect_uri, authorities,
 access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES
("client", "secret", "read,write,trust", "password,authorization_code,refresh_token,implicit", null, null, 43200, 43200, null, true),
("client-web", "secret-web", "read,write,trust", "password,authorization_code,refresh_token,implicit", null, null, 3600, 7200, null, true);

--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO role (id, description, name) VALUES
(1,	'Super user for Tennant management',	'ROLE_MASTER'),
(2,	'Admin User For Tenants',	'ROLE_ADMIN');

--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO users (id, modified_at, enabled, account_expired, account_locked, credential_expired, email, mobile, name, otp, otp_sent_at, password, retry_count, username, company_id) VALUES
(1,	'2017-06-28 00:25:42.25',	true, false,	false,	false,	'zahid7292@gmail.com', '8987525008',	'Md Zahid Raza',	NULL,	NULL,	'$2a$10$8gha167uTM3jhArZwFk6UOxgNChnMhWfrR59ND/TtzvTynwo2129C',	NULL,	'zahid7292',	NULL);


--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

INSERT INTO user_role (user_id, role_id) VALUES
(1,	1);

--
-- Data for Name: url_interceptor; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--
--
--INSERT INTO url_interceptor (id, access, http_method, url) VALUES
--(1,	'ROLE_MASTER',	'POST', '/api/users'),
--(2,	'ROLE_MASTER',	'GET',	'/api/users'),
--(3,	'ROLE_MASTER',	'GET',	'/api/users/{\\\\d+}'),
--(4,	'ROLE_MASTER',	'PUT',	'/api/users/{\\\\d+}'),
--(5,	'ROLE_MASTER',	'PATCH',	'/api/users/{\\\\d+}'),
--(6,	'ROLE_MASTER',	'DELETE',	'/api/users/{\\\\d+}'),
--(33,	'ROLE_ADMIN',	'POST', '/api/users'),
--(34,	'ROLE_ADMIN',	'GET',	'/api/users'),
--(35,	'ROLE_ADMIN',	'GET',	'/api/users/{\\\\d+}'),
--(36,	'ROLE_ADMIN',	'PUT',	'/api/users/{\\\\d+}'),
--(37,	'ROLE_ADMIN',	'PATCH',	'/api/users/{\\\\d+}'),
--(38,	'ROLE_ADMIN',	'DELETE',	'/api/users/{\\\\d+}'),
--(7,	'ROLE_MASTER',	'POST', '/api/roles'),
--(8,	'ROLE_MASTER',	'GET',	'/api/roles'),
--(9,	'ROLE_MASTER',	'GET',	'/api/roles/{\\\\d+}'),
--(10,	'ROLE_MASTER',	'PUT',	'/api/roles/{\\\\d+}'),
--(11,	'ROLE_MASTER',	'PATCH',	'/api/roles/{\\\\d+}'),
--(12,	'ROLE_MASTER',	'DELETE',	'/api/roles/{\\\\d+}'),
--(13,	'ROLE_MASTER',	'POST', '/api/companies'),
--(14,	'ROLE_MASTER',	'GET',	'/api/companies'),
--(15,	'ROLE_MASTER',	'GET',	'/api/companies/{\\\\d+}'),
--(16,	'ROLE_MASTER',	'PUT',	'/api/companies/{\\\\d+}'),
--(17,	'ROLE_MASTER',	'PATCH',	'/api/companies/{\\\\d+}'),
--(18,	'ROLE_MASTER',	'DELETE',	'/api/companies/{\\\\d+}'),
--(19,	'ROLE_MASTER',	'POST', '/api/interceptors'),
--(20,	'ROLE_MASTER',	'GET',	'/api/interceptors'),
--(21,	'ROLE_MASTER',	'GET',	'/api/interceptors/{\\\\d+}'),
--(22,	'ROLE_MASTER',	'PUT',	'/api/interceptors/{\\\\d+}'),
--(23,	'ROLE_MASTER',	'PATCH',	'/api/interceptors/{\\\\d+}'),
--(24,	'ROLE_MASTER',	'DELETE',	'/api/interceptors/{\\\\d+}'),
--(25,	'ROLE_ADMIN',	'POST', '/api/products'),
--(26,	'ROLE_ADMIN',	'GET',	'/api/products'),
--(27,	'ROLE_ADMIN',	'GET',	'/api/products/{\\\\d+}'),
--(28,	'ROLE_ADMIN',	'PUT',	'/api/products/{\\\\d+}'),
--(29,	'ROLE_ADMIN',	'PATCH',	'/api/products/{\\\\d+}'),
--(30,	'ROLE_ADMIN',	'DELETE',	'/api/products/{\\\\d+}'),
--(31,	'ROLE_MASTER',	'GET', '/metrics'),
--(32,	'ROLE_MASTER',	'GET',	'/auditevents');

--
-- PostgreSQL database dump complete
--