
-- 
-- -------------------------------------------- Montants engagés par imputation
--

CREATE OR REPLACE FUNCTION aides_montant_engage_imp(imp_id integer,debut date, fin date, bons_jusqua date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_montant_engage(a.id, $2, $3, $4))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
WHERE na.imputation_id = $1
  AND a.debut <= $3
  AND (a.fin >= $2 OR a.fin IS NULL)$_$
  LANGUAGE sql;

--
-- + fréquence et public
--

CREATE OR REPLACE FUNCTION aides_montant_engage_imp_freq_public(imp_id integer, freq_id integer, pub_id integer, periode_debut date, periode_fin date, bons_jusqua date) RETURNS integer
    AS $_$SELECT max(0,SUM(aide_montant_engage(a.id,$4,$5,$6))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
WHERE a.statut_id = $2
  AND a.public_id = $3
  AND na.imputation_id = $1
  AND a.debut <= $5
  AND (a.fin >= $4 OR a.fin IS NULL)$_$
    LANGUAGE sql;

--
-- - imputation (car 1 nature => 1 imputation), + nature et public
--

CREATE OR REPLACE FUNCTION aides_montant_engage_nature_public(nature_id integer, pub_id integer, periode_debut date, periode_fin date, bons_jusqua date) RETURNS integer
    AS $_$SELECT max(0,SUM(aide_montant_engage(a.id,$3,$4,$5))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
WHERE na.id = $1
  AND a.public_id = $2
  AND a.debut <= $4
  AND (a.fin >= $3 OR a.fin IS NULL)$_$
    LANGUAGE sql;

--
-- + secteur
--

CREATE OR REPLACE FUNCTION aides_montant_engage_imp_secteur(imp_id integer, sa_id integer, debut date, fin date, bons_jusqua date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_montant_engage(a.id, $3, $4, $5))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
WHERE na.parent_id = $2
  AND na.imputation_id = $1
  AND a.debut <= $4
  AND (a.fin >= $3 OR a.fin IS NULL)$_$
  LANGUAGE sql;

--
-- + secteur et public
--

CREATE OR REPLACE FUNCTION aides_montant_engage_imp_secteur_public(imp_id integer, secteur_id integer, pub_id integer, periode_debut date, periode_fin date, bons_jusqua date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_montant_engage(a.id,$4,$5,$6))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
  JOIN secteur_aide sa ON (na.parent_id = sa.id)
WHERE sa.id = $2
  AND a.public_id = $3
  AND na.imputation_id = $1
  AND a.debut <= $5
  AND (a.fin >= $4 OR a.fin IS NULL)$_$
  LANGUAGE sql;

--
-- + public
--

CREATE OR REPLACE FUNCTION aides_montant_engage_imp_public(imp_id integer, pub_id integer, periode_debut date, periode_fin date, bons_jusqua date) RETURNS integer
    AS $_$SELECT max(0,SUM(aide_montant_engage(a.id,$3,$4,$5))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
WHERE a.public_id = $2
  AND na.imputation_id = $1
  AND a.debut <= $4
  AND (a.fin >= $3 OR a.fin IS NULL)$_$
    LANGUAGE sql;

--
-- -------------------------------------------- Autres montants, par imputation
--


--
-- --------------------------------------------------------- Montant utilisé --
--
--

CREATE OR REPLACE FUNCTION aides_montant_utilise_imp(imp_id integer, debut date, fin date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_bons_montant_utilise(a.id, $2, $3))::integer)
 FROM aide a
 JOIN nature_aide na ON (a.nature_id = na.id)
WHERE na.imputation_id = $1
  AND a.debut <= $3
  AND (a.fin >= $2 OR a.fin IS NULL)$_$
  LANGUAGE sql;

--
-- + secteur et public
--

CREATE OR REPLACE FUNCTION aides_montant_utilise_imp_secteur_public(imp_id integer, secteur_id integer, pub_id integer, periode_debut date, periode_fin date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_bons_montant_utilise(a.id,$4,$5))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
  JOIN secteur_aide sa ON (na.parent_id = sa.id)
WHERE sa.id = $2
  AND a.public_id = $3
  AND na.imputation_id = $1
  AND a.debut <= $5
  AND (a.fin >= $4 OR a.fin IS NULL)$_$
  LANGUAGE sql;

--
-- + public
--

CREATE OR REPLACE FUNCTION aides_montant_utilise_imp_public(imp_id integer, tp_id integer, debut date, fin date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_bons_montant_utilise(a.id, $3, $4))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
WHERE a.public_id = $2
  AND na.imputation_id = $1
  AND a.debut <= $4
  AND (a.fin >= $3 OR a.fin IS NULL)$_$
  LANGUAGE sql;

--
-- + secteur
--

CREATE OR REPLACE FUNCTION aides_montant_utilise_imp_secteur(imp_id integer, sa_id integer, debut date, fin date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_bons_montant_utilise(a.id, $3, $4))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
WHERE na.parent_id = $2
  AND na.imputation_id = $1
  AND a.debut <= $4
  AND (a.fin >= $3 OR a.fin IS NULL)$_$
  LANGUAGE sql;

--
-- ------------------------------------------------------- Montant inutilisé --
--

CREATE OR REPLACE FUNCTION aides_montant_inutilise_imp(imp_id integer, debut date, fin date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_bons_montant_inutilise(a.id, $2, $3))::integer)
 FROM aide a
 JOIN nature_aide na ON (a.nature_id = na.id)
WHERE na.imputation_id = $1
  AND a.debut <= $3
  AND (a.fin >= $2 OR a.fin IS NULL)$_$
  LANGUAGE sql;


--
-- + secteur
--

CREATE OR REPLACE FUNCTION aides_montant_inutilise_imp_secteur(imp_id integer, sa_id integer, debut date, fin date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_bons_montant_inutilise(a.id, $3, $4))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
WHERE na.parent_id = $2
  AND na.imputation_id = $1
  AND a.debut <= $4
  AND (a.fin >= $3 OR a.fin IS NULL)$_$
  LANGUAGE sql;

--
-- + public
--

CREATE OR REPLACE FUNCTION aides_montant_inutilise_imp_public(imp_id integer, tp_id integer, debut date, fin date)
  RETURNS integer AS
$_$SELECT max(0,SUM(aide_bons_montant_inutilise(a.id, $3, $4))::integer)
  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
WHERE a.public_id = $2
  AND na.imputation_id = $1
  AND a.debut <= $4
  AND (a.fin >= $3 OR a.fin IS NULL)$_$
  LANGUAGE sql;
