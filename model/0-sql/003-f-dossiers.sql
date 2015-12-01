
--
--
--


CREATE OR REPLACE FUNCTION count_dossier_personnes_ressource(dossier_id integer)
  RETURNS integer AS
$_$SELECT COUNT(*)::integer
  FROM dossier_personnes_ressource
 WHERE dossier_id = $1$_$
  LANGUAGE sql;
