ALTER TABLE aide ADD COLUMN date_commission date;
ALTER TABLE aide RENAME COLUMN frequence_id TO statut_id;
ALTER TABLE frequence_aide RENAME TO statut_aide;
ALTER SEQUENCE pk_frequence_aide RENAME TO pk_statut_aide;

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

