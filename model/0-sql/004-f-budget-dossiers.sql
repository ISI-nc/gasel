
--
-- ---------------------------------------- Par imputation, fréquence et public
--

--
--
--

CREATE OR REPLACE FUNCTION dossiers_aide_cree_imp_freq_public(imp_id integer, freq_id integer, pub_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT COUNT(*)::integer
  FROM (

SELECT DISTINCT d.id
  FROM dossier d
  JOIN aide a ON (a.dossier_dossier_id = d.id)
  JOIN nature_aide na ON (a.nature_id = na.id)

WHERE a.statut_id = $2
  AND a.public_id = $3
  AND na.imputation_id = $1
  AND a.debut BETWEEN $4 AND $5

) AS ids$_$
    LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION dossiers_aide_suppr_imp_freq_public(imp_id integer, freq_id integer, pub_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT COUNT(*)::integer
  FROM (

SELECT DISTINCT d.id
  FROM dossier d
  JOIN aide a ON (a.dossier_dossier_id = d.id)
  JOIN nature_aide na ON (a.nature_id = na.id)

WHERE a.statut_id = $2
  AND a.public_id = $3
  AND na.imputation_id = $1
  AND a.fin BETWEEN $4 AND $5

) AS ids$_$
    LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION dossiers_imp_freq_public(imp_id integer, freq_id integer, pub_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT COUNT(*)::integer
  FROM (

SELECT DISTINCT d.id
  FROM dossier d
  JOIN aide a ON (a.dossier_dossier_id = d.id)
  JOIN nature_aide na ON (a.nature_id = na.id)

WHERE a.statut_id = $2
  AND a.public_id = $3
  AND na.imputation_id = $1
  AND a.debut <= $5
  AND (a.fin IS NULL OR a.fin >= $4)

) AS ids$_$
    LANGUAGE sql;

--
-- ----------------------------------------------------------------- Par public
--

--
--
--

CREATE OR REPLACE FUNCTION dossiers_nature_public(nature_id integer, pub_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT COUNT(*)::integer
  FROM (

SELECT DISTINCT d.id
  FROM dossier d
  JOIN aide a ON (a.dossier_dossier_id = d.id)
  JOIN nature_aide na ON (a.nature_id = na.id)

WHERE a.nature_id = $1
  AND a.public_id = $2
  AND a.debut <= $4
  AND (a.fin IS NULL OR a.fin >= $3)

) AS ids$_$
    LANGUAGE sql;

--
-- Imputation et public
--

CREATE OR REPLACE FUNCTION dossiers_imp_public(imp_id integer, pub_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT COUNT(*)::integer
  FROM (

SELECT DISTINCT d.id
  FROM dossier d
  JOIN aide a ON (a.dossier_dossier_id = d.id)
  JOIN nature_aide na ON (a.nature_id = na.id)

WHERE a.public_id = $2
  AND na.imputation_id = $1
  AND a.debut <= $4
  AND (a.fin IS NULL OR a.fin >= $3)

) AS ids$_$
    LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION dossiers_personnes_imp_public(imp_id integer, pub_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT COUNT(*)::integer FROM (
SELECT DISTINCT p.id
  FROM personne p
  JOIN dossier_personnes dp ON (p.id = dp.personne_id)
  JOIN dossier d ON (d.id = dp.dossier_id)
  JOIN aide a ON (a.dossier_dossier_id = d.id)
  JOIN nature_aide na ON (a.nature_id = na.id)

WHERE a.public_id = $2
  AND na.imputation_id = $1
  AND a.debut <= $4
  AND (a.fin IS NULL OR a.fin >= $3)
) AS ids$_$
    LANGUAGE sql;

--
-- Par imputation, fréquence et public
--

CREATE OR REPLACE FUNCTION dossiers_personnes_imp_freq_public(imp_id integer, freq_id integer, pub_id integer, periode_debut date, periode_fin date) RETURNS integer
    AS $_$SELECT COUNT(*)::integer FROM (
SELECT DISTINCT p.id
  FROM personne p
  JOIN dossier_personnes dp ON (p.id = dp.personne_id)
  JOIN dossier d ON (d.id = dp.dossier_id)
  JOIN aide a ON (a.dossier_dossier_id = d.id)
  JOIN nature_aide na ON (a.nature_id = na.id)

WHERE a.statut_id = $2
  AND a.public_id = $3
  AND na.imputation_id = $1
  AND a.debut <= $5
  AND (a.fin IS NULL OR a.fin >= $4)
) AS ids$_$
    LANGUAGE sql;
    
--
-- Par secteur et public
--

CREATE OR REPLACE FUNCTION dossiers_secteur_public(sec_id int4, pub_id integer, periode_debut date, periode_fin date)
  RETURNS int4 AS
$_$SELECT COUNT(*)::integer
  FROM (

SELECT DISTINCT d.id
  FROM dossier d
  JOIN aide a ON (a.dossier_dossier_id = d.id)
  JOIN nature_aide na ON (a.nature_id = na.id)

WHERE na.parent_id = $1
  AND a.public_id = $2
  AND a.debut <= $4
  AND (a.fin IS NULL OR a.fin >= $3)

) AS ids$_$
  LANGUAGE sql;
