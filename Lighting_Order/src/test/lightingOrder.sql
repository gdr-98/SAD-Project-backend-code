--
-- PostgreSQL database dump
--

-- Dumped from database version 13.4
-- Dumped by pg_dump version 13.4

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: Restaurant; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA "Restaurant";


ALTER SCHEMA "Restaurant" OWNER TO postgres;

--
-- Name: adminpack; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS adminpack WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION adminpack; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION adminpack IS 'administrative functions for PostgreSQL';


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: Dipendente; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."Dipendente" (
    name character varying(100) NOT NULL,
    surname character varying(100) NOT NULL,
    id character varying(16) NOT NULL,
    date date
);


ALTER TABLE "Restaurant"."Dipendente" OWNER TO postgres;

--
-- Name: Dipendente_Ruolo; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."Dipendente_Ruolo" (
    dipendente_id character varying(16) NOT NULL,
    ruolo_name character varying(50) NOT NULL
);


ALTER TABLE "Restaurant"."Dipendente_Ruolo" OWNER TO postgres;

--
-- Name: Merce; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."Merce" (
    id character varying(10) NOT NULL,
    quantity integer NOT NULL,
    name character varying(100) NOT NULL,
    price double precision NOT NULL,
    description character varying(255),
    countable boolean,
    "quantityLowerBound" integer,
    "inStock" boolean,
    "priceAsAdditive" double precision
);


ALTER TABLE "Restaurant"."Merce" OWNER TO postgres;

--
-- Name: Merce_Prodotto; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."Merce_Prodotto" (
    merce_id character varying(10) NOT NULL,
    prodotto_name character varying NOT NULL
);


ALTER TABLE "Restaurant"."Merce_Prodotto" OWNER TO postgres;

--
-- Name: Merce_ProdottoOrdinato; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."Merce_ProdottoOrdinato" (
    type boolean NOT NULL,
    prodotto_ordinato_ordine_id integer NOT NULL,
    prodotto_ordinato_linea_ordine integer NOT NULL,
    merce_id character varying(10) NOT NULL
);


ALTER TABLE "Restaurant"."Merce_ProdottoOrdinato" OWNER TO postgres;

--
-- Name: Ordine; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."Ordine" (
    id integer NOT NULL,
    date date NOT NULL,
    dipendente_id character varying(16),
    tavolo_id character varying(10),
    state character varying(20),
    tavolo_room integer,
    item_completed integer
);


ALTER TABLE "Restaurant"."Ordine" OWNER TO postgres;

--
-- Name: Prodotto; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."Prodotto" (
    price double precision,
    name character varying(100) NOT NULL,
    description character varying(255) NOT NULL,
    area character varying(50) NOT NULL,
    "inStock" boolean
);


ALTER TABLE "Restaurant"."Prodotto" OWNER TO postgres;

--
-- Name: ProdottoOrdinato; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."ProdottoOrdinato" (
    priority integer,
    notes character varying(255),
    ordine_id integer NOT NULL,
    prodotto_name character varying(100) NOT NULL,
    linea_ordine integer NOT NULL,
    state character varying(20)
);


ALTER TABLE "Restaurant"."ProdottoOrdinato" OWNER TO postgres;

--
-- Name: Ruolo; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."Ruolo" (
    name character varying(50) NOT NULL,
    description character varying(255)
);


ALTER TABLE "Restaurant"."Ruolo" OWNER TO postgres;

--
-- Name: Tavolo; Type: TABLE; Schema: Restaurant; Owner: postgres
--

CREATE TABLE "Restaurant"."Tavolo" (
    id character varying(10) NOT NULL,
    client_number integer NOT NULL,
    room integer NOT NULL,
    state character varying(20)
);


ALTER TABLE "Restaurant"."Tavolo" OWNER TO postgres;

--
-- Data for Name: Dipendente; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."Dipendente" (name, surname, id, date) FROM stdin;
Giuseppe	Di Cecio	1	2021-09-22
Giuseppe	De Rosa	2	2021-09-22
Nicola	D Ambra	3	2021-09-22
Antonio	Emmanuele	4	2021-09-22
\.


--
-- Data for Name: Dipendente_Ruolo; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."Dipendente_Ruolo" (dipendente_id, ruolo_name) FROM stdin;
1	Cameriere
1	Accoglienza
2	Cucina
3	Forno
4	Bar
4	Cassa
\.


--
-- Data for Name: Merce; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."Merce" (id, quantity, name, price, description, countable, "quantityLowerBound", "inStock", "priceAsAdditive") FROM stdin;
B001	50	Acqua Natia	0.25	Acqua liscia Natia	t	5	t	\N
B002	50	Acqua Ferrarelle	0.25	Acqua frizzante Ferrarelle	t	5	t	\N
F001	10	Salsa	4.99	Scatola di salsa per pizza	f	2	t	\N
F002	250	Impasto	0.1	Pagnotta singola per pizza	t	20	t	\N
F003	10	Fior di latte	5.99	Quantità di Fior di latte in kilogrammi	f	2	t	\N
C001	5	Patate	4.99	Busta di patatine fritte	f	1	t	1.5
G001	10	Origano	0.2	Busta di origano per scopo generale	f	1	t	0.5
G002	1	Aglio	0.49	Quantità di aglio in kilogrammi	f	1	f	0.5
G003	5	Funghi	7.99	Busta di funghi per uso generale	f	1	t	0.5
G004	2	Olive	3.99	Pacco di olive per uso generale	f	1	f	1
\.


--
-- Data for Name: Merce_Prodotto; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."Merce_Prodotto" (merce_id, prodotto_name) FROM stdin;
B001	Acqua Natia
B002	Acqua Ferrarelle
F001	Margherita
F001	Margherita Speciale
F001	Napoletana
F002	Margherita
F002	Margherita Speciale
F002	Napoletana
F003	Margherita
F003	Margherita Speciale
G001	Napoletana
G002	Napoletana
G003	Margherita Speciale
G004	Margherita Speciale
C001	Patate Fritte
\.


--
-- Data for Name: Merce_ProdottoOrdinato; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."Merce_ProdottoOrdinato" (type, prodotto_ordinato_ordine_id, prodotto_ordinato_linea_ordine, merce_id) FROM stdin;
\.


--
-- Data for Name: Ordine; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."Ordine" (id, date, dipendente_id, tavolo_id, state, tavolo_room, item_completed) FROM stdin;
0	2021-09-23	1	1	WaitingForWorking	1	0
1	2021-09-23	1	1	WaitingForWorking	1	0
\.


--
-- Data for Name: Prodotto; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."Prodotto" (price, name, description, area, "inStock") FROM stdin;
1.5	Acqua Natia	Acqua Naturale	Bar	t
1.5	Acqua Ferrarelle	Acqua Frizzante	Bar	t
2.5	Patate Fritte	Porzione di patatine fritte. (Prodotto Surgelato)	Cucina	t
5	Margherita	Fior di Latte, Salsa, Olio e Basilico	Forno	t
4	Napoletana	Salsa, Origano, Aglio, Olio e Basilico	Forno	t
7	Margherita Speciale	Fior di latte, Salsa, Funghi, Olive, Olio e Basilico	Forno	t
\.


--
-- Data for Name: ProdottoOrdinato; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."ProdottoOrdinato" (priority, notes, ordine_id, prodotto_name, linea_ordine, state) FROM stdin;
1	\N	1	Patate Fritte	1	WaitingForWorking
2	\N	1	Margherita	2	WaitingForWorking
\.


--
-- Data for Name: Ruolo; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."Ruolo" (name, description) FROM stdin;
Cameriere	\N
Cassa	\N
Accoglienza	\N
Cucina	\N
Bar	\N
Forno	\N
\.


--
-- Data for Name: Tavolo; Type: TABLE DATA; Schema: Restaurant; Owner: postgres
--

COPY "Restaurant"."Tavolo" (id, client_number, room, state) FROM stdin;
3	4	1	free
4	6	1	free
2	3	1	free
1	9	2	free
1	8	1	reserved
\.


--
-- Name: Dipendente_Ruolo Dipendente_Ruolo_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Dipendente_Ruolo"
    ADD CONSTRAINT "Dipendente_Ruolo_pkey" PRIMARY KEY (dipendente_id, ruolo_name);


--
-- Name: Dipendente Dipendente_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Dipendente"
    ADD CONSTRAINT "Dipendente_pkey" PRIMARY KEY (id);


--
-- Name: Merce_ProdottoOrdinato Merce_ProdottoOrdinato_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Merce_ProdottoOrdinato"
    ADD CONSTRAINT "Merce_ProdottoOrdinato_pkey" PRIMARY KEY (prodotto_ordinato_ordine_id, prodotto_ordinato_linea_ordine, merce_id);


--
-- Name: Merce_Prodotto Merce_Prodotto_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Merce_Prodotto"
    ADD CONSTRAINT "Merce_Prodotto_pkey" PRIMARY KEY (merce_id, prodotto_name);


--
-- Name: Merce Merce_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Merce"
    ADD CONSTRAINT "Merce_pkey" PRIMARY KEY (id);


--
-- Name: Ordine Ordine_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Ordine"
    ADD CONSTRAINT "Ordine_pkey" PRIMARY KEY (id);


--
-- Name: ProdottoOrdinato ProdottoOrdinato_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."ProdottoOrdinato"
    ADD CONSTRAINT "ProdottoOrdinato_pkey" PRIMARY KEY (ordine_id, linea_ordine);


--
-- Name: Prodotto Prodotto_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Prodotto"
    ADD CONSTRAINT "Prodotto_pkey" PRIMARY KEY (name);


--
-- Name: Ruolo Ruolo_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Ruolo"
    ADD CONSTRAINT "Ruolo_pkey" PRIMARY KEY (name);


--
-- Name: Tavolo Tavolo_pkey; Type: CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Tavolo"
    ADD CONSTRAINT "Tavolo_pkey" PRIMARY KEY (id, room);


--
-- Name: fki_Merce_Prodotto_Merce_fkey; Type: INDEX; Schema: Restaurant; Owner: postgres
--

CREATE INDEX "fki_Merce_Prodotto_Merce_fkey" ON "Restaurant"."Merce_Prodotto" USING btree (merce_id);


--
-- Name: fki_Merce_Prodotto_Prodotto_fkey; Type: INDEX; Schema: Restaurant; Owner: postgres
--

CREATE INDEX "fki_Merce_Prodotto_Prodotto_fkey" ON "Restaurant"."Merce_Prodotto" USING btree (prodotto_name);


--
-- Name: fki_ProdottoOrdinato_prodotto_name_fkey; Type: INDEX; Schema: Restaurant; Owner: postgres
--

CREATE INDEX "fki_ProdottoOrdinato_prodotto_name_fkey" ON "Restaurant"."ProdottoOrdinato" USING btree (prodotto_name);


--
-- Name: fki_Prodotto_fkey; Type: INDEX; Schema: Restaurant; Owner: postgres
--

CREATE INDEX "fki_Prodotto_fkey" ON "Restaurant"."Prodotto" USING btree (area);


--
-- Name: fki_id; Type: INDEX; Schema: Restaurant; Owner: postgres
--

CREATE INDEX fki_id ON "Restaurant"."Merce_ProdottoOrdinato" USING btree (prodotto_ordinato_linea_ordine, prodotto_ordinato_ordine_id);


--
-- Name: fki_ordinato; Type: INDEX; Schema: Restaurant; Owner: postgres
--

CREATE INDEX fki_ordinato ON "Restaurant"."ProdottoOrdinato" USING btree (ordine_id);


--
-- Name: fki_tavolo_esterno; Type: INDEX; Schema: Restaurant; Owner: postgres
--

CREATE INDEX fki_tavolo_esterno ON "Restaurant"."Ordine" USING btree (tavolo_id);


--
-- Name: Dipendente_Ruolo Dipendente_Ruolo_dipendente_id_fkey; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Dipendente_Ruolo"
    ADD CONSTRAINT "Dipendente_Ruolo_dipendente_id_fkey" FOREIGN KEY (dipendente_id) REFERENCES "Restaurant"."Dipendente"(id);


--
-- Name: Dipendente_Ruolo Dipendente_Ruolo_ruolo_name_fkey; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Dipendente_Ruolo"
    ADD CONSTRAINT "Dipendente_Ruolo_ruolo_name_fkey" FOREIGN KEY (ruolo_name) REFERENCES "Restaurant"."Ruolo"(name);


--
-- Name: Merce_Prodotto Merce_Prodotto_Merce_fkey; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Merce_Prodotto"
    ADD CONSTRAINT "Merce_Prodotto_Merce_fkey" FOREIGN KEY (merce_id) REFERENCES "Restaurant"."Merce"(id) NOT VALID;


--
-- Name: Merce_Prodotto Merce_Prodotto_Prodotto_fkey; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Merce_Prodotto"
    ADD CONSTRAINT "Merce_Prodotto_Prodotto_fkey" FOREIGN KEY (prodotto_name) REFERENCES "Restaurant"."Prodotto"(name) NOT VALID;


--
-- Name: Ordine Ordine_dipendente_id_fkey; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Ordine"
    ADD CONSTRAINT "Ordine_dipendente_id_fkey" FOREIGN KEY (dipendente_id) REFERENCES "Restaurant"."Dipendente"(id);


--
-- Name: ProdottoOrdinato ProdottoOrdinato_ordine_id_fkey; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."ProdottoOrdinato"
    ADD CONSTRAINT "ProdottoOrdinato_ordine_id_fkey" FOREIGN KEY (ordine_id) REFERENCES "Restaurant"."Ordine"(id) ON DELETE CASCADE NOT VALID;


--
-- Name: ProdottoOrdinato ProdottoOrdinato_prodotto_name_fkey; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."ProdottoOrdinato"
    ADD CONSTRAINT "ProdottoOrdinato_prodotto_name_fkey" FOREIGN KEY (prodotto_name) REFERENCES "Restaurant"."Prodotto"(name) NOT VALID;


--
-- Name: Prodotto Prodotto_fkey; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Prodotto"
    ADD CONSTRAINT "Prodotto_fkey" FOREIGN KEY (area) REFERENCES "Restaurant"."Ruolo"(name) NOT VALID;


--
-- Name: Merce_ProdottoOrdinato fkeyProdottoOrdinato; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."Merce_ProdottoOrdinato"
    ADD CONSTRAINT "fkeyProdottoOrdinato" FOREIGN KEY (prodotto_ordinato_linea_ordine, prodotto_ordinato_ordine_id) REFERENCES "Restaurant"."ProdottoOrdinato"(linea_ordine, ordine_id) ON DELETE CASCADE NOT VALID;


--
-- Name: ProdottoOrdinato ordinato; Type: FK CONSTRAINT; Schema: Restaurant; Owner: postgres
--

ALTER TABLE ONLY "Restaurant"."ProdottoOrdinato"
    ADD CONSTRAINT ordinato FOREIGN KEY (ordine_id) REFERENCES "Restaurant"."Ordine"(id) ON DELETE CASCADE NOT VALID;


--
-- PostgreSQL database dump complete
--

