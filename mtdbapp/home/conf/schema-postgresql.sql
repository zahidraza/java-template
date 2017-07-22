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

--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: mdzahidraza
--

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE hibernate_sequence OWNER TO mdzahidraza;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: product; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE product (
    id bigint NOT NULL,
    enabled boolean NOT NULL,
    modified_at timestamp without time zone,
    description character varying(255),
    name character varying(255)
);


ALTER TABLE product OWNER TO mdzahidraza;

--
-- Name: product_history; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE product_history (
    id bigint NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    description character varying(255),
    name character varying(255)
);


ALTER TABLE product_history OWNER TO mdzahidraza;

--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: mdzahidraza
--

CREATE SEQUENCE product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE product_id_seq OWNER TO mdzahidraza;

--
-- Name: product_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: mdzahidraza
--

ALTER SEQUENCE product_id_seq OWNED BY product.id;


--
-- Name: rev_info; Type: TABLE; Schema: public; Owner: mdzahidraza
--

CREATE TABLE rev_info (
    id integer NOT NULL,
    "timestamp" bigint NOT NULL,
    username character varying(255)
);


ALTER TABLE rev_info OWNER TO mdzahidraza;

--
-- Name: product id; Type: DEFAULT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY product ALTER COLUMN id SET DEFAULT nextval('product_id_seq'::regclass);


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: mdzahidraza
--

SELECT pg_catalog.setval('hibernate_sequence', 1, true);


--
-- Name: product_id_seq; Type: SEQUENCE SET; Schema: public; Owner: mdzahidraza
--

SELECT pg_catalog.setval('product_id_seq', 1, false);


--
-- Data for Name: rev_info; Type: TABLE DATA; Schema: public; Owner: mdzahidraza
--

COPY rev_info (id, "timestamp", username) FROM stdin;
\.


--
-- Name: product_history product_history_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY product_history
    ADD CONSTRAINT product_history_pkey PRIMARY KEY (id, rev);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: rev_info rev_info_pkey; Type: CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY rev_info
    ADD CONSTRAINT rev_info_pkey PRIMARY KEY (id);


--
-- Name: product_history fko4110kyfea25fcyea8oucddss; Type: FK CONSTRAINT; Schema: public; Owner: mdzahidraza
--

ALTER TABLE ONLY product_history
    ADD CONSTRAINT fko4110kyfea25fcyea8oucddss FOREIGN KEY (rev) REFERENCES rev_info(id);


--
-- PostgreSQL database dump complete
--

