
--
-- Nombre de bons dans un etat donné sur une période donnée.
--

CREATE OR REPLACE FUNCTION count_aide_bons_etat(aide_id integer, etat integer, periode_debut date, periode_fin date)
  RETURNS integer AS
$_$SELECT GREATEST(0, COUNT(*))::integer
  FROM bon
 WHERE aide_id = $1
   AND debut <= $4
   AND (fin IS NULL OR fin >= $3)
   AND etat_id = $2$_$
  LANGUAGE 'sql';

--
-- Nombre de bons n'étant pas dans un etat donné sur une période donnée.
--

CREATE OR REPLACE FUNCTION count_aide_bons_non_etat(aide_id integer, etat integer, periode_debut date, periode_fin date)
  RETURNS integer AS
$_$SELECT GREATEST(0, COUNT(*))::integer
  FROM bon
 WHERE aide_id = $1
   AND debut <= $4
   AND (fin IS NULL OR fin >= $3)
   AND etat_id <> $2$_$
  LANGUAGE 'sql';

--
-- Nombre de bons à éditer sur un mois donné.
--

CREATE OR REPLACE FUNCTION count_aide_bons_a_editer(aide_id integer, mois_debut date, mois_fin date)
  RETURNS integer AS $_$
SELECT a.quantite_mensuelle
          - count_aide_bons_non_etat(a.id, 5, $2,$3) -- bons autres que "créé"
  FROM aide a
 WHERE a.id = $1$_$
  LANGUAGE 'sql';
