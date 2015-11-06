
--
-- Renvoie le dernier paiement d'une attribution à une date donnée.
--

CREATE OR REPLACE FUNCTION attribution_jf_date_dernier_paiement(att_id integer, date_max date)
  RETURNS date AS
$_$SELECT MAX(p.fin)::date
  FROM paiement p
  JOIN attribution_jf a ON (p.attribution_id=a.id)
 WHERE a.id = $1
   AND (p.fin)::date <= $2$_$
  LANGUAGE 'sql' VOLATILE;

--
-- Indique si une attribution est payee sur une période donnée
--

CREATE OR REPLACE FUNCTION attribution_jf_payee(att_id integer, periode_debut date, periode_fin date)
  RETURNS integer AS
$_$
SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
  FROM paiement p
 WHERE p.attribution_id = $1
   AND (p.debut)::date <= $3
   AND (p.fin)::date >= $2
$_$
  LANGUAGE 'sql' VOLATILE;

--
-- Indique si une attribution est active sur une période donnée
--

CREATE OR REPLACE FUNCTION attribution_jf_active(att_id integer, periode_debut date, periode_fin date)
  RETURNS integer AS
$BODY$
SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
  FROM arrete_jf a
 WHERE attribution_id = $1
   AND (debut)::date <= $3
   AND (fin)::date >= $2
$BODY$
  LANGUAGE 'sql' VOLATILE;

--
-- Renvoie le nombre d'attributions actives d'un jardin 
--

CREATE OR REPLACE FUNCTION jardin_attributions_actives(jard_id integer, p_debut date, p_fin date)
  RETURNS integer AS
$BODY$SELECT COUNT(*)::integer
  FROM attribution_jf a
  JOIN parcelle p ON (a.parcelle_id = p.id)

WHERE p.jardin_id = $1
  AND attribution_jf_active(a.id, $2, $3) > 0
$BODY$
  LANGUAGE 'sql' VOLATILE;
  

--
-- Renvoie le nombre d'attributions payées d'un jardin 
--

CREATE OR REPLACE FUNCTION jardin_attributions_payees(jard_id integer, p_debut date, p_fin date)
  RETURNS integer AS
$BODY$SELECT COUNT(*)::integer
  FROM attribution_jf a
  JOIN parcelle p ON (a.parcelle_id = p.id)

WHERE p.jardin_id = $1
  AND attribution_jf_payee(a.id, $2, $3) > 0
  AND attribution_jf_active(a.id, $2, $3) > 0

GROUP BY p.jardin_id$BODY$
  LANGUAGE 'sql' VOLATILE;
