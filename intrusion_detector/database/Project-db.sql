--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.7
-- Dumped by pg_dump version 9.5.7

SET statement_timeout = 0;
SET lock_timeout = 0;
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
-- Name: obj_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE obj_type AS ENUM (
    'people',
    'animal',
    'car',
    'undefined'
);


ALTER TYPE obj_type OWNER TO postgres;

--
-- Name: users_role; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE users_role AS ENUM (
    'admin',
    'guest'
);


ALTER TYPE users_role OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: cameras; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cameras (
    id integer NOT NULL,
    camera_name character varying(255) NOT NULL,
    location point NOT NULL,
    online boolean NOT NULL,
    description character varying(255) NOT NULL
);


ALTER TABLE cameras OWNER TO postgres;

--
-- Name: cameras_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cameras_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cameras_id_seq OWNER TO postgres;

--
-- Name: cameras_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE cameras_id_seq OWNED BY cameras.id;


--
-- Name: image; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE image (
    id integer NOT NULL,
    url character varying(255) NOT NULL,
    last_detected_date timestamp without time zone NOT NULL,
    cameras_id integer NOT NULL,
    traffic_id integer NOT NULL,
    CONSTRAINT image_camerasid_check CHECK ((cameras_id >= 0)),
    CONSTRAINT image_trafficid_check CHECK ((traffic_id >= 0))
);


ALTER TABLE image OWNER TO postgres;

--
-- Name: image_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE image_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE image_id_seq OWNER TO postgres;

--
-- Name: image_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE image_id_seq OWNED BY image.id;


--
-- Name: traffic; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE traffic (
    id integer NOT NULL,
    type obj_type NOT NULL,
    first_detected_date timestamp without time zone NOT NULL
);


ALTER TABLE traffic OWNER TO postgres;

--
-- Name: traffic_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE traffic_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE traffic_id_seq OWNER TO postgres;

--
-- Name: traffic_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE traffic_id_seq OWNED BY traffic.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE users (
    id integer NOT NULL,
    name character varying(50) NOT NULL,
    role users_role NOT NULL,
    email character varying(255) NOT NULL,
    password uuid NOT NULL,
    CONSTRAINT email_regexp CHECK (((email)::text ~* '^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$'::text))
);


ALTER TABLE users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cameras ALTER COLUMN id SET DEFAULT nextval('cameras_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY image ALTER COLUMN id SET DEFAULT nextval('image_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY traffic ALTER COLUMN id SET DEFAULT nextval('traffic_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Data for Name: cameras; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY cameras (id, camera_name, location, online, description) FROM stdin;
\.


--
-- Name: cameras_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cameras_id_seq', 1, false);


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY image (id, url, last_detected_date, cameras_id, traffic_id) FROM stdin;
\.


--
-- Name: image_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('image_id_seq', 1, false);


--
-- Data for Name: traffic; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY traffic (id, type, first_detected_date) FROM stdin;
\.


--
-- Name: traffic_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('traffic_id_seq', 1, false);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY users (id, name, role, email, password) FROM stdin;
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('users_id_seq', 1, false);


--
-- Name: cameras_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cameras
    ADD CONSTRAINT cameras_pkey PRIMARY KEY (id);


--
-- Name: image_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY image
    ADD CONSTRAINT image_pkey PRIMARY KEY (id);


--
-- Name: traffic_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY traffic
    ADD CONSTRAINT traffic_pkey PRIMARY KEY (id);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: image_camerasid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY image
    ADD CONSTRAINT image_camerasid_fkey FOREIGN KEY (cameras_id) REFERENCES cameras(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: image_trafficid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY image
    ADD CONSTRAINT image_trafficid_fkey FOREIGN KEY (traffic_id) REFERENCES traffic(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

