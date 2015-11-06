
-------------------------------------------------------------------------------
-- Fonctions utilitaires
--

CREATE OR REPLACE FUNCTION fin_mois_prec(d date)
  RETURNS date AS
$_$SELECT (date(DATE_PART('year',$1)||'-'||DATE_PART('month',$1)||'-01')
          - '1 DAY'::interval)::date$_$
  LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION debut_mois(d date)
  RETURNS date AS
$_$SELECT date(DATE_PART('year',$1)||'-'||DATE_PART('month',$1)||'-01')::date$_$
  LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION fin_mois(d date)
  RETURNS date AS
$_$SELECT (date(DATE_PART('year',$1)||'-'||DATE_PART('month',$1)||'-01')
          + '1 MONTH'::interval - '1 DAY'::interval)::date$_$
  LANGUAGE 'sql';

CREATE OR REPLACE FUNCTION debut_annee(d date)
  RETURNS date AS
$_$SELECT date(DATE_PART('year',$1)||'-01-01')::date$_$
  LANGUAGE 'sql';

-------------------------------------------------------------------------------
-- Fonctions de calcul des reports
--

--
-- report budget secteur aide
--

CREATE OR REPLACE FUNCTION report_budget_secteur_aide(imp integer, m date, secteur_id integer, split date)
  RETURNS integer AS
$_$
SELECT (SUM(bsa.montant)
       - aides_montant_engage_imp_secteur($1,
                  $3, debut_annee($2), fin_mois_prec($2), $4)
       )::integer
  FROM budget_secteur_aide bsa
  JOIN budget_imp_mensuel bim ON (bsa.parent_id=bim.id)
  JOIN budget_imp_annuel bia ON (bim.parent_id=bia.id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)
 WHERE bsa.secteur_aide_id = $3
   AND bim.mois < (DATE_PART('month', $2)-1)
   AND bia.imputation_id = $1
   AND ba.annee = DATE_PART('year',$2)
$_$
  LANGUAGE 'sql';

--
-- report budget type public
--

CREATE OR REPLACE FUNCTION report_budget_type_public(imp integer, m date, pub_id integer, split date)
  RETURNS integer AS
$_$
SELECT (SUM(btp.montant)
       - aides_montant_engage_imp_public($1,
                  $3, debut_annee($2), fin_mois_prec($2), $4)
       )::integer
  FROM budget_type_public btp
  JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)
  JOIN budget_imp_mensuel bim ON (bsa.parent_id=bim.id)
  JOIN budget_imp_annuel bia ON (bim.parent_id=bia.id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)
 WHERE btp.type_public_id = $3
   AND bim.mois < (DATE_PART('month', $2)-1)
   AND bia.imputation_id = $1
   AND ba.annee = DATE_PART('year',$2)
$_$
  LANGUAGE 'sql';

-------------------------------------------------------------------------------
-- Fonctions de calcul des virements
--

--
-- Virements secteur aide
--

CREATE OR REPLACE FUNCTION virement_budget_secteur_aide(imputation_id integer, m date, secteur_id integer)
  RETURNS integer AS
$_$
SELECT SUM(v.montant)::integer
  FROM virement_budget v
  JOIN budget_secteur_aide bsa ON (v.budget_id = bsa.id AND v.budget_type = 3)
  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
  JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)
 WHERE bsa.secteur_aide_id = $3
   AND bim.mois = DATE_PART('month', $2)
   AND bia.imputation_id = $1
   AND ba.annee = DATE_PART('year', $2)
$_$
  LANGUAGE sql;

--
-- Virements type public
--

CREATE OR REPLACE FUNCTION virement_budget_type_public(imputation_id integer, m date, secteur_id integer)
  RETURNS integer AS
$_$
SELECT SUM(v.montant)::integer
  FROM virement_budget v
  JOIN budget_type_public btp ON (v.budget_id = btp.id AND v.budget_type = 4)
  JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)
  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
  JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)
 WHERE btp.type_public_id = $3
   AND bim.mois = DATE_PART('month', $2)
   AND bia.imputation_id = $1
   AND ba.annee = DATE_PART('year', $2)
$_$
  LANGUAGE sql;
