
--
--
--

CREATE OR REPLACE FUNCTION adresse_quartier(adresse_id integer)
  RETURNS varchar(20) AS
$_$
SELECT MIN(liquar)::varchar
  FROM adresse a
  JOIN mairie.sivoqt voqt ON (voqt.codcom=a.code_ville AND voqt.cdvoie=a.code_voie)
  JOIN mairie.siqurt qt ON (qt.quarti=voqt.quarti)
WHERE a.id = $1
$_$
  LANGUAGE 'sql' VOLATILE;

--
--
--

CREATE OR REPLACE FUNCTION nb_mois_intersection(debut1 date, fin1 date, debut2 date, fin2 date)
  RETURNS integer AS
$_$
SELECT nb_mois(GREATEST($1,$3), LEAST($2,$4))
$_$
  LANGUAGE 'sql' VOLATILE;

--
--
--

CREATE OR REPLACE FUNCTION ressource_montant(res_id integer, periode_debut date, periode_fin date)
  RETURNS integer AS
$_$
SELECT (r.montant * nb_mois_intersection($2,$3, r.debut,r.fin))::integer
  FROM ressource r
 WHERE r.id = $1
$_$
  LANGUAGE 'sql' VOLATILE;

--
--
--

CREATE OR REPLACE FUNCTION dossier_revenus(dossier_id integer, periode_debut date, periode_fin date)
  RETURNS integer AS
$_$
SELECT (SUM(ressource_montant(r.id, $2,$3))
       / nb_mois($2,$3)
       )::integer
  FROM ressource r
  JOIN personne p ON (r.personne_id = p.id)
  JOIN dossier_personnes dp ON (dp.personne_id = p.id)
 WHERE dp.dossier_id = $1
   AND r.debut <= $3
   AND (r.fin >= $2 OR r.fin IS NULL)
$_$
  LANGUAGE 'sql' VOLATILE;
