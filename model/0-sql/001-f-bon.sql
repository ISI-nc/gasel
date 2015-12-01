
--
--
--

CREATE OR REPLACE FUNCTION bon_montant_usage(bon_id integer) RETURNS integer
    AS $_$SELECT montant_utilise FROM usage_bon WHERE bon_id=$1$_$
    LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION bon_montant_utilise(bon_id integer) RETURNS integer
    AS $_$SELECT
    (CASE
        WHEN etat_id=5 -- Créé
        THEN 0
        WHEN etat_id=4 -- Édité
		THEN montant
        WHEN etat_id=3 -- Annulé
        THEN 0
        WHEN etat_id=2 -- Part. utilisé
        THEN bon_montant_usage(id)
        WHEN etat_id=1 -- Utilisé
        THEN montant
    END) AS montant

  FROM bon
 WHERE id = $1$_$
    LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION bon_montant_engage(bon_id integer) RETURNS integer
    AS $_$SELECT
    (CASE
        WHEN etat_id=5 -- Créé
		THEN montant
        WHEN etat_id=4 -- Édité
        THEN montant
        WHEN etat_id=3 -- Annulé
        THEN 0
        WHEN etat_id=2 -- Part. utilisé
        THEN bon_montant_usage(id)
        WHEN etat_id=1 -- Utilisé
        THEN montant
    END) AS montant
  FROM bon
 WHERE id = $1$_$
    LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION bon_montant_inutilise(bon_id integer) RETURNS integer
    AS $_$SELECT
    (CASE
        WHEN etat_id=5 -- Créé
        THEN 0
        WHEN etat_id=4 -- Édité
        THEN 0
        WHEN etat_id=3 -- Annulé
        THEN montant
        WHEN etat_id=2 -- Part. utilisé
        THEN montant - bon_montant_usage(id)
        WHEN etat_id=1 -- Utilisé
        THEN 0
    END) AS montant

  FROM bon
 WHERE id = $1$_$
    LANGUAGE sql;
