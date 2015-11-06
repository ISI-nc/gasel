BEGIN;
-- ----- BEGIN -----

--
-- aide_resume_montants
--

DELETE FROM aide_resume_montants;

INSERT INTO aide_resume_montants (
SELECT
	a.id AS id,
	annee,
	mois,
    annee * 12 + mois + 1 AS annee_mois,
	bim.id AS bim_id,
	a.public_id AS public_id,
	na.parent_id AS secteur_id,
 	a.montant AS montant_aide,

 	CAST(SUM(CASE WHEN b.montant IS NULL THEN 0 ELSE b.montant END) AS INTEGER)
 	AS montant_bons,

 	CAST(SUM(CASE WHEN ub.montant_utilise IS NULL THEN 0 ELSE ub.montant_utilise END) AS INTEGER)
 	AS montant_bons_utilise,

 	CAST(SUM(CASE WHEN (b.montant - ub.montant_utilise) IS NULL THEN 0
 	              ELSE (b.montant - ub.montant_utilise) END)
 	     AS INTEGER)
 	AS montant_bons_inutilise_brut,

 	CAST(SUM(CASE WHEN bann.montant IS NULL THEN 0 ELSE bann.montant END) AS INTEGER)
 	AS montant_bons_annule,

 	CAST(SUM(CASE WHEN bedit.montant IS NULL THEN 0 ELSE bedit.montant END) AS INTEGER)
 	AS montant_bons_edite,

 	CAST( (
 	  CASE WHEN SUM(b.montant - ub.montant_utilise) IS NULL THEN 0
           ELSE SUM(b.montant - ub.montant_utilise) END
    + CASE WHEN SUM(bann.montant) IS NULL THEN 0
           ELSE SUM(bann.montant) END
 	) AS INTEGER) AS montant_bons_inutilise

  FROM aide a
  JOIN nature_aide na ON (a.nature_id = na.id)
  JOIN budget_imp_annuel bia ON (na.imputation_id = bia.imputation_id)
  JOIN budget_annuel ba ON (bia.parent_id = ba.id
                                 AND ba.annee BETWEEN year(a.debut) AND year(a.fin))
  JOIN budget_imp_mensuel bim ON (bim.parent_id = bia.id
                                       AND ba.annee * 12 + bim.mois + 1 BETWEEN year(a.debut)*12+month(a.debut)
                                                                            AND year(a.fin)*12+month(a.fin))
  
  LEFT JOIN bon b ON (b.aide_id = a.id
                           AND year(b.debut) = annee
                           AND month(b.debut) = mois + 1)

  LEFT JOIN usage_bon ub ON (b.etat_id IN (1,2) -- util. ou part. util.
                                  AND ub.bon_id = b.id)

  LEFT JOIN bon bann ON (b.etat_id = 3 -- annule
                              AND bann.id = b.id)

  LEFT JOIN bon bedit ON (b.etat_id IN (4,5) -- cree ou edite
                               AND bedit.id = b.id)

 GROUP BY a.id, bim.id, a.public_id, na.parent_id, annee, mois, a.montant
)
;
--
-- aide_montants
--

DELETE FROM aide_montants;

INSERT INTO aide_montants (

SELECT	id,

	annee,

	mois,

	annee_mois,

	bim_id,

	public_id,

	secteur_id,

	montant_aide,

	montant_bons,

	montant_bons_utilise,

	montant_bons_inutilise_brut,

	montant_bons_annule,

	montant_bons_edite,

	montant_bons_inutilise,

	0,0,0

  FROM	aide_resume_montants
);

UPDATE aide_montants SET
       montant_utilise   = montant_bons_utilise + montant_bons_edite,
       montant_engage    = montant_bons,
       montant_inutilise = montant_bons_inutilise
       
 WHERE annee_mois <= year(CURRENT DATE) * 12 + month(CURRENT_DATE);

UPDATE aide_montants SET
       montant_utilise   = 0,
       montant_engage    = montant_aide,
       montant_inutilise = montant_aide
       
 WHERE annee_mois > year(CURRENT DATE) * 12 + month(CURRENT_DATE);

--
-- 
--

DELETE FROM bsa_report;

INSERT INTO bsa_report (
SELECT bsa.id,
       SUM(bsa2.montant)
       + null_is_zero(CAST(SUM(bv.virements) AS INTEGER))
       - null_is_zero(CAST(SUM(bm.montant_utilise) AS INTEGER))
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
);

--
-- 
--

DELETE FROM btp_report;

INSERT INTO btp_report (
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
);

-- ----- END -----
COMMIT;