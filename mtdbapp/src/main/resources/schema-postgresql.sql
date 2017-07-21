--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.3
-- Dumped by pg_dump version 9.6.3

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: company; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE company (
    id bigint NOT NULL,
    enabled boolean NOT NULL,
    modified_at timestamp without time zone,
    address character varying(255),
    db_name character varying(255),
    description character varying(255),
    name character varying(255)
);


ALTER TABLE company OWNER TO mdzahidraza;

--
-- Name: company_id_seq; Type: SEQUENCE; Schema: public; Owner: mdzahidraza
--

CREATE SEQUENCE company_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE company_id_seq OWNER TO mdzahidraza;

--
-- Name: company_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mdzahidraza
--

ALTER SEQUENCE company_id_seq OWNED BY company.id;


--
-- Name: role; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE role (
    id bigint NOT NULL,
    description character varying(255),
    name character varying(255),
    company_id bigint
);


ALTER TABLE role OWNER TO mdzahidraza;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: mdzahidraza
--

CREATE SEQUENCE role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE role_id_seq OWNER TO mdzahidraza;

--
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mdzahidraza
--

ALTER SEQUENCE role_id_seq OWNED BY role.id;

--
-- Name: users; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE users (
    id bigint NOT NULL,
    enabled boolean NOT NULL,
    modified_at timestamp without time zone,
    account_expired boolean,
    account_locked boolean,
    credential_expired boolean,
    email character varying(255) NOT NULL,
    mobile character varying(255),
    name character varying(255) NOT NULL,
    otp character varying(255),
    otp_sent_at timestamp without time zone,
    password character varying(255) NOT NULL,
    retry_count integer,
    username character varying(255) NOT NULL,
    company_id bigint
);


ALTER TABLE users OWNER TO mdzahidraza;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: mdzahidraza
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO mdzahidraza;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mdzahidraza
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: user_role; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE user_role OWNER TO mdzahidraza;


--
-- Name: url_interceptor; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE url_interceptor (
    id bigint NOT NULL,
    access character varying(255),
    http_method character varying(255),
    url character varying(255)
);


ALTER TABLE url_interceptor OWNER TO mdzahidraza;

--
-- Name: url_interceptor_id_seq; Type: SEQUENCE; Schema: public; Owner: mdzahidraza
--

CREATE SEQUENCE url_interceptor_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE url_interceptor_id_seq OWNER TO mdzahidraza;

--
-- Name: url_interceptor_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mdzahidraza
--

ALTER SEQUENCE url_interceptor_id_seq OWNED BY url_interceptor.id;

--
-- Name: company id; Type: DEFAULT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY company ALTER COLUMN id SET DEFAULT nextval('company_id_seq'::regclass);


--
-- Name: role id; Type: DEFAULT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY role ALTER COLUMN id SET DEFAULT nextval('role_id_seq'::regclass);


--
-- Name: url_interceptor id; Type: DEFAULT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY url_interceptor ALTER COLUMN id SET DEFAULT nextval('url_interceptor_id_seq'::regclass);

--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);

----------------------

--
-- Name: revinfo; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE revinfo (
    rev integer NOT NULL,
    revtstmp bigint
);


ALTER TABLE revinfo OWNER TO mdzahidraza;

----------------------

--
-- Name: company company_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY company
    ADD CONSTRAINT company_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: users uk_6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY users
    ADD CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- Name: url_interceptor url_interceptor_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY url_interceptor
    ADD CONSTRAINT url_interceptor_pkey PRIMARY KEY (id);


--
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: idx7516795akd6qg7e0i8e5rv58s; Type: INDEX; Schema: public; Owner: mdzahidraza
--

CREATE INDEX idx7516795akd6qg7e0i8e5rv58s ON users USING btree (name, email, username);


--
-- Name: user_role fka68196081fvovjhkek5m97n3y; Type: FK CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES role(id);


--
-- Name: users fkbwv4uspmyi7xqjwcrgxow361t; Type: FK CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY users
    ADD CONSTRAINT fkbwv4uspmyi7xqjwcrgxow361t FOREIGN KEY (company_id) REFERENCES company(id);


--
-- Name: user_role fkj345gk1bovqvfame88rcx7yyx; Type: FK CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY user_role
    ADD CONSTRAINT fkj345gk1bovqvfame88rcx7yyx FOREIGN KEY (user_id) REFERENCES users(id);







--------------------------
--- Creating tables for JdbcTokenStore
---------------------------


--
-- Name: oauth_client_details; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE oauth_client_details (
    client_id character varying(255),
    resource_ids character varying(255),
    client_secret character varying(255),
    scope character varying(255),
    authorized_grant_types character varying(255),
    web_server_redirect_uri character varying(255),
    authorities character varying(255),
    access_token_validity integer,
    refresh_token_validity integer,
    additional_information character varying(4096),
    autoapprove character varying(255)
);


ALTER TABLE oauth_client_details OWNER TO mdzahidraza;

--
-- Name: oauth_client_details oauth_client_details_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY oauth_client_details
    ADD CONSTRAINT oauth_client_details_pkey PRIMARY KEY (client_id);
 

CREATE TABLE  oauth_client_token (
   token_id character varying(256),
   token bytea,
   authentication_id character varying(256),
   user_name character varying(256),
   client_id character varying(256)
);
 
CREATE TABLE  oauth_access_token (
   token_id character varying(256),
   token bytea,
   authentication_id character varying(256),
   user_name character varying(256),
   client_id character varying(256),
   authentication bytea,
   refresh_token character varying(256)
);
 
CREATE TABLE  oauth_refresh_token (
   token_id character varying(256),
   token bytea,
   authentication bytea
);
 
CREATE TABLE  oauth_code (
   code character varying(256),
   authentication bytea
);
    

--
-- Name: ClientDetails; Type: TABLE; Schema: public; Owner: mdzahidraza
--

-- customized oauth_client_details table
CREATE TABLE ClientDetails (
    appId character varying(255),
    resourceIds character varying(255),
    appSecret character varying(255),
    scope character varying(255),
    grantTypes character varying(255),
    redirectUrl character varying(255),
    authorities character varying(255),
    access_token_validity integer,
    refresh_token_validity integer,
    additional_information character varying(4096)
);


ALTER TABLE ClientDetails OWNER TO mdzahidraza;

--
-- Name: ClientDetails ClientDetails_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY ClientDetails
    ADD CONSTRAINT ClientDetails_pkey PRIMARY KEY (appId);

--
-- PostgreSQL database dump complete
--

