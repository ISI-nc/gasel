
-------------------------------------------------------------------------------
------------------------------------------------------------------------- Dates
-------------------------------------------------------------------------------

CREATE OR REPLACE FUNCTION "year"(d date)
  RETURNS integer AS
$_$SELECT DATE_PART('year', $1)::integer$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION "month"(d date)
  RETURNS integer AS
$_$SELECT DATE_PART('month', $1)::integer$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION year_month(d date) RETURNS integer
    AS $_$SELECT (DATE_PART('year', $1) * 12 + DATE_PART('month', $1))::integer$_$
    LANGUAGE sql;

--
-- Nombre de mois entre deux dates (0 si fin < debut)
--

CREATE OR REPLACE FUNCTION nb_mois(debut date, fin date) RETURNS integer
	AS $_$SELECT CASE
		WHEN $2 < $1 THEN 0
		ELSE year_month($2)-year_month($1)+1
	END$_$
  LANGUAGE sql;

-------------------------------------------------------------------------------
----------------------------------------------------------------------- Min/max
-------------------------------------------------------------------------------

--
--
--

CREATE OR REPLACE FUNCTION min(i1 integer, i2 integer)
  RETURNS integer AS $_$SELECT LEAST($1,$2)$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION max(i1 integer, i2 integer)
  RETURNS integer AS $_$SELECT GREATEST($1,$2)$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION min(d1 date, d2 date)
  RETURNS date AS $_$SELECT LEAST($1,$2)$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION max(d1 date, d2 date)
  RETURNS date AS $_$SELECT GREATEST($1,$2)$_$
  LANGUAGE sql;

-------------------------------------------------------------------------------
----------------------------------------------------------------------- Misc...
-------------------------------------------------------------------------------

--
--
--

CREATE OR REPLACE FUNCTION null_is_zero(value integer)
  RETURNS integer AS $_$SELECT CASE WHEN ($1 IS NULL) THEN 0 ELSE $1 END$_$
  LANGUAGE sql;
