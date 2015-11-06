-- --------------------------------------------------------------------
--
--

DROP TABLE aide_resume_montants;
CREATE TABLE aide_resume_montants (
	id                          INTEGER,
	annee                       INTEGER,
	mois                        INTEGER,
	annee_mois                  INTEGER,
	bim_id                      INTEGER,
	public_id                   INTEGER,
	secteur_id                  INTEGER,
	montant_aide                INTEGER,
	montant_bons                INTEGER,
	montant_bons_utilise        INTEGER,
	montant_bons_inutilise_brut INTEGER,
	montant_bons_annule         INTEGER,
	montant_bons_edite          INTEGER,
	montant_bons_inutilise      INTEGER,
	
	PRIMARY KEY (id, annee, mois)
);

CREATE INDEX aide_resume_montants_id         ON aide_resume_montants (id);
CREATE INDEX aide_resume_montants_bim_id     ON aide_resume_montants (bim_id);
CREATE INDEX aide_resume_montants_sa_id      ON aide_resume_montants (secteur_id);
CREATE INDEX aide_resume_montants_tp_id      ON aide_resume_montants (public_id);
CREATE INDEX aide_resume_montants_annee      ON aide_resume_montants (annee);
CREATE INDEX aide_resume_montants_mois       ON aide_resume_montants (mois);
CREATE INDEX aide_resume_montants_annee_mois ON aide_resume_montants (annee_mois);

-- --------------------------------------------------------------------
--
--

-- DROP VIEW aide_montants;
DROP TABLE aide_montants;
CREATE TABLE aide_montants (
	id                          INTEGER,
	annee                       INTEGER,
	mois                        INTEGER,
	annee_mois                  INTEGER,
	bim_id                      INTEGER,
	public_id                   INTEGER,
	secteur_id                  INTEGER,
	montant_aide                INTEGER,
	montant_bons                INTEGER,
	montant_bons_utilise        INTEGER,
	montant_bons_inutilise_brut INTEGER,
	montant_bons_annule         INTEGER,
	montant_bons_edite          INTEGER,
	montant_bons_inutilise      INTEGER,
	montant_utilise             INTEGER,
	montant_engage              INTEGER,
	montant_inutilise           INTEGER,
	
	PRIMARY KEY (id, annee, mois)
);

CREATE INDEX aide_montants_id         ON aide_montants (id);
CREATE INDEX aide_montants_bim_id     ON aide_montants (bim_id);
CREATE INDEX aide_montants_sa_id      ON aide_montants (secteur_id);
CREATE INDEX aide_montants_tp_id      ON aide_montants (public_id);
CREATE INDEX aide_montants_annee      ON aide_montants (annee);
CREATE INDEX aide_montants_mois       ON aide_montants (mois);
CREATE INDEX aide_montants_annee_mois ON aide_montants (annee_mois);

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

-- --------------------------------------------------------------------
--
--

DROP VIEW bsa_report;
CREATE TABLE bsa_report (
	id INTEGER,
	report INTEGER,
	
	PRIMARY KEY (id)
);

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

DROP VIEW btp_report;
CREATE TABLE btp_report (
	id INTEGER,
	report INTEGER,
	
	PRIMARY KEY (id)
);

--
--
--

CREATE VIEW btp_resume AS

SELECT btp.id, btp.montant, bv.virements, bm.montant_inutilise, bm.montant_bons, bm.montant_engage
  FROM budget_type_public btp
  LEFT JOIN btp_virements bv ON (bv.id = btp.id)
  LEFT JOIN btp_montants bm ON (bm.id = btp.id)
;
