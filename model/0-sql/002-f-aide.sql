
--
-- Nombre de bons entre deux dates.
--

CREATE OR REPLACE FUNCTION count_aide_bons(a_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT COUNT(*)::integer
  FROM bon b
 WHERE b.aide_id = $1
   AND debut <= $3
   AND (fin IS NULL OR fin >= $2)$_$
    LANGUAGE sql;

--
-- Montant d'une date à une autre.
--

CREATE OR REPLACE FUNCTION aide_montant(a_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT
    (a.montant *
     nb_mois(GREATEST($2,a.debut),
					  LEAST($3,a.fin))
	)::integer
  FROM aide a
 WHERE a.id = $1$_$
    LANGUAGE sql;

--
-- ------------------------------------------------------ Montants sur les bons
--

--
-- Montant des bons sur une période :  "utilisé".
--

CREATE OR REPLACE FUNCTION aide_bons_montant_utilise(a_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT SUM(bon_montant_utilise(id))::integer
  FROM bon
 WHERE aide_id = $1
   AND debut <= $3
   AND (fin IS NULL OR fin >= $2)$_$
    LANGUAGE sql;
    
--
-- Montant des bons sur une période : "engagé".
--

CREATE OR REPLACE FUNCTION aide_bons_montant_engage(a_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT SUM(bon_montant_inutilise(id))::integer
  FROM bon
 WHERE aide_id = $1
   AND debut <= $3
   AND (fin IS NULL OR fin >= $2)$_$
    LANGUAGE sql;

--
-- Montant des bons sur une période : "inutilisé".
--

CREATE OR REPLACE FUNCTION aide_bons_montant_inutilise(a_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT SUM(bon_montant_inutilise(id))::integer
  FROM bon
 WHERE aide_id = $1
   AND debut <= $3
   AND (fin IS NULL OR fin >= $2)$_$
    LANGUAGE sql;

--
-- -------------------------------------------- Montants "mixtes" aides et bons
--
-- Ils sont calculés sur les bons jusqu'à la "fin du mois", puis sur le montant
-- de l'aide, considéré comme équivalent à des bons "créés".
--

--
-- Montant "utilisé" sur une période et en prenant en compte la "fin du mois".
--

CREATE OR REPLACE FUNCTION aide_montant_utilise(a_id integer, periode_debut date, periode_fin date, bons_jusqua date) RETURNS integer
    AS $_$SELECT
    CASE
    WHEN $4 < $2 -- periode future
    THEN 0
    WHEN $4 BETWEEN $2 and $3 -- periode en cours
	THEN max(0,aide_bons_montant_utilise($1, $2, $4))
    WHEN $4 > $3 -- periode passée
    THEN max(0,aide_bons_montant_utilise($1, $2, $3))
    END$_$
    LANGUAGE sql;

--
-- Montant "engagé" sur une période et en prenant en compte la "fin du mois".
--

CREATE OR REPLACE FUNCTION aide_montant_engage(a_id integer, periode_debut date, periode_fin date, bons_jusqua date) RETURNS integer
    AS $_$SELECT
    CASE
    WHEN $4 < $2 -- periode future
    THEN max(0,aide_montant($1, $2,$3))
    WHEN $4 BETWEEN $2 and $3 -- periode en cours
	THEN max(0,aide_bons_montant_engage($1, $2, $4)) +
         max(0,aide_montant($1, ($4+'1 DAY'::interval)::date,$3))
    WHEN $4 > $3 -- periode passée
    THEN max(0,aide_bons_montant_engage($1, $2, $3))
    END$_$
    LANGUAGE sql;

--
-- Montant "inutilisé" sur une période et en prenant en compte la "fin du mois".
--

CREATE OR REPLACE FUNCTION aide_montant_inutilise(a_id integer, periode_debut date, periode_fin date, bons_jusqua date) RETURNS integer
    AS $_$SELECT
    CASE
    WHEN $4 < $2 -- periode future
    THEN 0
    WHEN $4 BETWEEN $2 and $3 -- periode en cours
	THEN max(0,aide_bons_montant_inutilise($1, $2, $4))
    WHEN $4 > $3 -- periode passée
    THEN max(0,aide_bons_montant_inutilise($1, $2, $3))
    END$_$
    LANGUAGE sql;

