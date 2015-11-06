ALTER TABLE adresse ADD FOREIGN KEY (code_voie) REFERENCES mairie__sivoie (cdvoie)
;

ALTER TABLE adresse ADD FOREIGN KEY (code_ville) REFERENCES mairie__sicomm (codcom)
;

ALTER TABLE personne ADD FOREIGN KEY (couverture_sociale_id) REFERENCES couverture_sociale (id)
;

ALTER TABLE personne ADD FOREIGN KEY (lien_chef_famille_id) REFERENCES lien_parente (id)
;

ALTER TABLE personne ADD FOREIGN KEY (mutuelle_id) REFERENCES mutuelle (id)
;

ALTER TABLE personne ADD FOREIGN KEY (ne_code_ville) REFERENCES mairie__sicomm (codcom)
;

ALTER TABLE personne ADD FOREIGN KEY (ne_ville_etrangere_codpay, ne_ville_etrangere_scodpa) REFERENCES mairie__siviet (codpay, scodpa)
;

ALTER TABLE personne ADD FOREIGN KEY (situation_familiale_id) REFERENCES situation_familiale (id)
;

ALTER TABLE personne ADD FOREIGN KEY (situation_professionelle_id) REFERENCES situation_professionelle (id)
;

ALTER TABLE personne ADD FOREIGN KEY (statut_id) REFERENCES statut_personne (id)
;

ALTER TABLE activite_personne ADD FOREIGN KEY (contrat_id) REFERENCES type_contrat (id)
;

ALTER TABLE activite_personne ADD FOREIGN KEY (personne_id) REFERENCES personne (id)
;

ALTER TABLE activite_personne ADD FOREIGN KEY (type_id) REFERENCES type_activite (id)
;

ALTER TABLE acces_page ADD FOREIGN KEY (groupe_id) REFERENCES groupe (id)
;

ALTER TABLE acces_page ADD FOREIGN KEY (page_id) REFERENCES app_page (id)
;

ALTER TABLE ressource ADD FOREIGN KEY (personne_id) REFERENCES personne (id)
;

ALTER TABLE ressource ADD FOREIGN KEY (type_id) REFERENCES type_ressource (id)
;

ALTER TABLE dossier ADD FOREIGN KEY (adresse_habitation_id) REFERENCES adresse (id)
;

ALTER TABLE dossier ADD FOREIGN KEY (adresse_postale_id) REFERENCES adresse (id)
;

ALTER TABLE dossier ADD FOREIGN KEY (chef_famille_id) REFERENCES personne (id)
;

ALTER TABLE dossier ADD FOREIGN KEY (conjoint_id) REFERENCES personne (id)
;

ALTER TABLE dossier ADD FOREIGN KEY (modif_utilisateur_id) REFERENCES utilisateur (id)
;

ALTER TABLE dossier ADD FOREIGN KEY (referent_id) REFERENCES utilisateur (id)
;

ALTER TABLE dossier ADD FOREIGN KEY (signalement_id) REFERENCES origine_signalement (id)
;

ALTER TABLE dossier ADD FOREIGN KEY (type_habitat_id) REFERENCES type_habitat (id)
;

ALTER TABLE charge ADD FOREIGN KEY (personne_id) REFERENCES personne (id)
;

ALTER TABLE charge ADD FOREIGN KEY (type_id) REFERENCES type_charge (id)
;

