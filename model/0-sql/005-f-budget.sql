
--
--
--


CREATE OR REPLACE FUNCTION budget_imp_annuel_montant(imp_id integer, annee integer)
  RETURNS integer AS
$_$SELECT bia.montant::integer
  FROM budget_imp_annuel bia
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)
WHERE bia.imputation_id = $1
  AND ba.annee = $2$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION budget_imp_mensuel_montant(imp_id integer, periode date)
  RETURNS integer AS
$_$SELECT bim.montant::integer
  FROM budget_imp_mensuel bim
  JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)
WHERE bim.mois = DATE_PART('month', $2)-1
  AND ba.annee = DATE_PART('year', $2)
  AND bia.imputation_id = $1$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION virements_bsa_depuis_bia(bia_id integer)
  RETURNS integer AS
$_$SELECT SUM(v.montant)::integer
          FROM virement_budget v
        WHERE v.budget_type = 3
          AND v.budget_id IN (SELECT bsa.id
                                FROM budget_secteur_aide bsa
                                JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
                               WHERE bim.parent_id = $1)$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION virement_budget_type_public(imputation_id integer, annee integer)
  RETURNS integer AS
$_$SELECT SUM(v.montant)::integer
          FROM virement_budget v
        WHERE (v.budget_type = 4
	  AND v.budget_id IN (SELECT btp.id
                                FROM budget_type_public btp
                                JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)
                                JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
                                JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
                                JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2))$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION virement_budget_secteur_aide(imputation_id integer, annee integer)
  RETURNS integer AS
$_$SELECT SUM(v.montant)::integer
          FROM virement_budget v
        WHERE (v.budget_type = 4
	  AND v.budget_id IN (SELECT btp.id
                                FROM budget_type_public btp
                                JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)
                                JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
                                JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
                                JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2)) OR
          (v.budget_type = 3
	  AND v.budget_id IN (SELECT bsa.id
                                FROM budget_secteur_aide bsa
                                JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
                                JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
                                JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2))$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION virement_budget_imp_mensuel(imputation_id integer, annee integer, mois integer)
  RETURNS integer AS
$_$SELECT SUM(v.montant)::integer
          FROM virement_budget v
        WHERE (v.budget_type = 4
	  AND v.budget_id IN (SELECT btp.id
                                FROM budget_type_public btp
                                JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)
                                JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
                                JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
                                JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2 AND bim.mois = $3)) OR
          (v.budget_type = 3
	  AND v.budget_id IN (SELECT bsa.id
                                FROM budget_secteur_aide bsa
                                JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
                                JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
                                JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2 AND bim.mois = $3)) OR
          (v.budget_type = 2
	  AND v.budget_id IN (SELECT bim.id
                                FROM budget_imp_mensuel bim
                                JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id) 
                                JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2 AND bim.mois = $3))$_$
  LANGUAGE sql;

--
--
--

CREATE OR REPLACE FUNCTION virement_budget_imp_annuel(imputation_id integer, annee integer)
  RETURNS integer AS
$_$SELECT SUM(v.montant)::integer
          FROM virement_budget v
        WHERE (v.budget_type = 4
	  AND v.budget_id IN (SELECT btp.id
                                FROM budget_type_public btp
                                JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)
                                JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
                                JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
                                JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2)) OR
          (v.budget_type = 3
	  AND v.budget_id IN (SELECT bsa.id
                                FROM budget_secteur_aide bsa
                                JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
                                JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
                                JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2)) OR
          (v.budget_type = 2
	  AND v.budget_id IN (SELECT bim.id
                                FROM budget_imp_mensuel bim
                                JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id) 
                                JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2)) OR
          (v.budget_type = 1
	  AND v.budget_id IN (SELECT bia.id
                                FROM budget_imp_annuel bia
                               JOIN budget_annuel ba ON (bia.parent_id = ba.id)
                               WHERE bia.imputation_id = $1 AND ba.annee = $2))$_$
  LANGUAGE sql;
  
--
--
--

CREATE OR REPLACE FUNCTION bsa_montant(annee integer, imp_id integer, sa_id integer)
  RETURNS integer AS
$_$SELECT SUM(bsa.montant::integer)::integer
  FROM budget_secteur_aide bsa
  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
  JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)
WHERE bsa.secteur_aide_id = $3
  AND bia.imputation_id = $2
  AND ba.annee = $1$_$
  LANGUAGE sql;
