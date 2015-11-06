
-- --------------------------------------------------------------------
--
--

CREATE OR REPLACE VIEW aide_resume_montants AS

SELECT a.id, bim.id AS bim_id, a.public_id AS public_id, na.parent_id AS secteur_id, annee, mois,
	(annee * 12 + mois + 1) AS annee_mois,
 	a.montant AS montant_aide,

 	CAST(SUM(coalesce(b.montant, 0)) AS INTEGER)
 	AS montant_bons,

 	CAST(SUM(coalesce(ub.montant_utilise, 0)) AS INTEGER)
 	AS montant_bons_utilise,

 	CAST(SUM(coalesce(b.montant - ub.montant_utilise, 0)) AS INTEGER)
 	AS montant_bons_inutilise_brut,

 	CAST(SUM(coalesce(bann.montant, 0)) AS INTEGER)
 	AS montant_bons_annule,

 	CAST(SUM(coalesce(bedit.montant, 0)) AS INTEGER)
 	AS montant_bons_edite,

 	CAST(SUM(coalesce(b.montant, 0) - coalesce(ub.montant_utilise, 0) + coalesce(bann.montant, 0)) AS INTEGER)
 	AS montant_bons_inutilise

  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
  JOIN budget_imp_annuel bia ON (na.imputation_id = bia.imputation_id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)
  JOIN budget_imp_mensuel bim ON (bim.parent_id = bia.id)
  
  LEFT JOIN bon b ON (b.aide_id = a.id
                           AND extract(year from b.debut) = annee
                           AND extract(month from b.debut) = mois + 1)

  LEFT JOIN usage_bon ub ON (b.etat_id IN (1,2) -- util. ou part. util.
                                  AND ub.bon_id = b.id)

  LEFT JOIN bon bann ON (b.etat_id = 3 -- annule
                              AND bann.id = b.id)

  LEFT JOIN bon bedit ON (b.etat_id IN (4,5) -- cree ou edite
                               AND bedit.id = b.id)
  
 WHERE annee * 12 + mois + 1 BETWEEN year_month(a.debut)
                                 AND year_month(a.fin)

 GROUP BY a.id, bim.id, a.public_id, na.parent_id, annee, mois, a.montant
;
CREATE INDEX aide__debut    ON aide(debut);
CREATE INDEX aide__debut_ym ON aide(year_month(debut));
CREATE INDEX aide__debut_y  ON aide(date_part('year'::text, debut));
CREATE INDEX aide__debut_m  ON aide(date_part('month'::text, debut));
CREATE INDEX aide__fin      ON aide(fin);
CREATE INDEX aide__fin_ym   ON aide(year_month(fin));
CREATE INDEX aide__fin_y    ON aide(date_part('year'::text, fin));
CREATE INDEX aide__fin_m    ON aide(date_part('month'::text, fin));

CREATE INDEX bon__debut    ON bon(debut);
CREATE INDEX bon__debut_ym ON bon(year_month(debut));
CREATE INDEX bon__debut_y  ON bon(date_part('year'::text, debut));
CREATE INDEX bon__debut_m  ON bon(date_part('month'::text, debut));
CREATE INDEX bon__fin      ON bon(fin);
CREATE INDEX bon__fin_ym   ON bon(year_month(fin));
CREATE INDEX bon__fin_y    ON bon(date_part('year'::text, fin));
CREATE INDEX bon__fin_m    ON bon(date_part('month'::text, fin));

CREATE INDEX usage_bon__bon_montant_utilise ON usage_bon (bon_id, montant_utilise);

CREATE INDEX aide__debutym_finym_public_nature_montant ON aide (year_month(debut), year_month(fin), public_id, nature_id, montant);

--
--
--

CREATE VIEW aide_montants AS

SELECT id, bim_id, public_id, secteur_id, annee, mois, annee_mois, montant_aide,
	montant_bons, montant_bons_utilise, montant_bons_annule,
	montant_bons_inutilise,

	CASE
	WHEN (year_month(CURRENT_DATE) >= annee_mois)
	THEN montant_bons_utilise + montant_bons_edite
	WHEN (year_month(CURRENT_DATE) < annee_mois)
	THEN 0
	END AS montant_utilise,

	CASE
	WHEN (year_month(CURRENT_DATE) >= annee_mois) THEN montant_bons
	WHEN (year_month(CURRENT_DATE) < annee_mois) THEN montant_aide
	END AS montant_engage,

	CASE
	WHEN (year_month(CURRENT_DATE) >= annee_mois) THEN montant_bons_inutilise
	WHEN (year_month(CURRENT_DATE) < annee_mois) THEN montant_aide
	END AS montant_inutilise

  FROM aide_resume_montants
;

-- --------------------------------------------------------------------
--
--

CREATE VIEW bim_montants AS

SELECT bim.id,
	SUM(montant_bons) AS montant_bons,
	SUM(montant_engage) AS montant_engage,
	SUM(montant_bons_inutilise) AS montant_inutilise,
	SUM(montant_utilise) AS montant_utilise

  FROM budget_imp_mensuel bim
  LEFT JOIN aide_montants am ON (am.bim_id = bim.id)

 GROUP BY bim.id
;

--
--
--

CREATE VIEW bim_virements AS

SELECT bim.id, SUM(v.montant) AS virements
  FROM budget_imp_mensuel bim
  JOIN virement_budget v ON (v.budget_type = 2 -- BIM
                                  AND v.budget_id = bim.id)
 GROUP BY bim.id
;

-- --------------------------------------------------------------------
--
--

CREATE VIEW bsa_montants AS

SELECT bsa.id,
	SUM(montant_bons) AS montant_bons,
	SUM(montant_engage) AS montant_engage,
	SUM(montant_bons_inutilise) AS montant_inutilise,
	SUM(montant_utilise) AS montant_utilise

  FROM budget_secteur_aide bsa
  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
  LEFT JOIN aide_montants am ON (am.bim_id = bim.id
                                      AND am.secteur_id = bsa.secteur_aide_id)
  
 GROUP BY bsa.id
;

--
--
--

CREATE VIEW bsa_virements AS

SELECT bsa.id, SUM(v.montant) AS virements
  FROM budget_secteur_aide bsa
  JOIN virement_budget v ON (v.budget_type = 3 -- BSA
                                   AND v.budget_id = bsa.id)
 GROUP BY bsa.id
;

--
--
--

CREATE VIEW bsa_report AS

SELECT bsa.id,
       SUM(bsa2.montant)
       + CASE WHEN SUM(bv.virements) IS NULL THEN 0
              ELSE SUM(bv.virements) END
       - CASE WHEN SUM(bm.montant_utilise) IS NULL THEN 0
              ELSE SUM(bm.montant_utilise) END
       AS report

  FROM budget_secteur_aide bsa
  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
  JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)

  JOIN budget_secteur_aide bsa2 ON (bsa2.secteur_aide_id = bsa.secteur_aide_id) -- meme secteur
  JOIN budget_imp_mensuel bim2 ON (bsa2.parent_id = bim2.id
                                        AND bim2.parent_id = bim.parent_id -- meme annee
                                        AND bim2.mois < bim.mois) -- mois inférieur

  JOIN bsa_montants bm ON (bm.id = bsa2.id)
  LEFT JOIN bsa_virements bv ON (bv.id = bsa2.id)

GROUP BY bsa.id
;

--
--
--

CREATE VIEW bsa_resume AS

SELECT bsa.id, bsa.montant, bv.virements, bm.montant_inutilise, bm.montant_bons,
       bm.montant_engage
  FROM budget_secteur_aide bsa
  LEFT JOIN bsa_virements bv ON (bv.id = bsa.id)
  LEFT JOIN bsa_montants bm ON (bm.id = bsa.id)
;

-- --------------------------------------------------------------------
--
--

CREATE VIEW btp_montants AS

SELECT btp.id,
	SUM(montant_bons) AS montant_bons,
	SUM(montant_engage) AS montant_engage,
	SUM(montant_bons_inutilise) AS montant_inutilise,
	SUM(montant_utilise) AS montant_utilise

  FROM budget_type_public btp
  JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)
  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
  LEFT JOIN aide_montants am ON (am.bim_id = bim.id
                                      AND am.secteur_id = bsa.secteur_aide_id
                                      AND am.public_id = btp.type_public_id)
  
 GROUP BY btp.id
;

--
--
--

CREATE VIEW btp_virements AS

SELECT btp.id, SUM(v.montant) AS virements
  FROM budget_type_public btp
  JOIN virement_budget v ON (v.budget_type = 4 -- BTP
                                  AND v.budget_id = btp.id)
 GROUP BY btp.id
;

--
--
--

CREATE VIEW btp_report AS

SELECT btp.id,
       SUM(btp2.montant)
       + null_is_zero(CAST(SUM(bv.virements) AS INTEGER))
       - null_is_zero(CAST(SUM(bm.montant_utilise) AS INTEGER))
       AS report

  FROM budget_type_public btp
  JOIN budget_secteur_aide bsa ON (btp.parent_id = bsa.id)
  JOIN budget_imp_mensuel bim ON (bsa.parent_id = bim.id)
  JOIN budget_imp_annuel bia ON (bim.parent_id = bia.id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id)

  JOIN budget_type_public btp2 ON (btp2.type_public_id = btp.type_public_id) -- meme public
  JOIN budget_secteur_aide bsa2 ON (btp2.parent_id = bsa2.id
                                         AND bsa2.secteur_aide_id = bsa.secteur_aide_id) -- meme secteur
  JOIN budget_imp_mensuel bim2 ON (bsa2.parent_id = bim2.id
                                        AND bim2.parent_id = bim.parent_id -- meme annee
                                        AND bim2.mois < bim.mois) -- mois inférieur

  JOIN btp_montants bm ON (bm.id = btp2.id)
  LEFT JOIN btp_virements bv ON (bv.id = btp2.id)

GROUP BY btp.id
;

--
--
--

CREATE VIEW btp_resume AS

SELECT btp.id, btp.montant, bv.virements, bm.montant_inutilise, bm.montant_bons, bm.montant_engage
  FROM budget_type_public btp
  LEFT JOIN btp_virements bv ON (bv.id = btp.id)
  LEFT JOIN btp_montants bm ON (bm.id = btp.id)
;
