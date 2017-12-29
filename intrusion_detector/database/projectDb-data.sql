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
1	Camera-1	(1,2)	t	Camera-1 description
4	Camera-2	(10.0020000000000007,200.255545839999996)	t	Camera-2 description
5	Camera-3	(100.001999999999995,20.2555458399999999)	t	Camera-3 description
6	Camera-4	(90.0019999999999953,25.2555458399999999)	t	Camera-4 description
7	Camera-5	(100.155001999999996,110.255544999999998)	t	Camera-5 description
8	Camera-6	(120.356002000000004,140.895544999999998)	t	Camera-6 description
9	Camera-7	(130.112201999999996,150.005545000000012)	t	Camera-7 description
13	Camera-11	(55.2589630000000014,95.0499999999999972)	t	Camera-11 description
14	Camera-12	(25.2589630000000014,195.050000000000011)	t	Camera-12 description
15	Camera-13	(255.258962999999994,295.050000000000011)	t	Camera-13 description
16	Camera-14	(15.2589629999999996,5.04999999999999982)	t	Camera-14 description
\.


--
-- Name: cameras_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cameras_id_seq', 18, true);


--
-- Data for Name: image; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY image (id, url, last_detected_date, cameras_id, traffic_id) FROM stdin;
7	./server/upload/images/folder1	2017-01-01 12:03:00	1	1
9	./server/upload/images/folder2	2017-01-01 13:03:00	4	2
10	./server/upload/images/folder3	2017-01-01 13:03:00	5	3
11	./server/upload/images/folder4	2017-01-01 13:03:00	6	4
12	./server/upload/images/folder5	2017-01-01 13:03:00	7	5
13	./server/upload/images/folder6	2017-01-01 14:03:00	8	1
14	./server/upload/images/folder7	2017-01-01 15:03:00	9	2
16	./server/upload/images/folder8	2017-01-02 16:03:00	13	3
17	./server/upload/images/folder9	2017-01-03 17:03:00	14	4
\.


--
-- Name: image_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('image_id_seq', 17, true);


--
-- Data for Name: traffic; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY traffic (id, type, first_detected_date) FROM stdin;
1	people	2017-08-01 12:33:55
2	people	2017-08-02 15:33:55
3	people	2017-08-03 15:33:55
4	car	2017-08-03 16:33:55
5	animal	2017-08-03 17:33:55
\.


--
-- Name: traffic_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('traffic_id_seq', 5, true);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY users (id, name, role, email, password) FROM stdin;
1	user1	admin	user1@mail.ru	a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('users_id_seq', 1, true);


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

