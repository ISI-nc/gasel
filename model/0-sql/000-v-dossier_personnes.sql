CREATE OR REPLACE VIEW dossier_personnes AS 
(( SELECT d.id AS dossier_id, p.id AS personne_id
   FROM dossier d
   JOIN personne p ON d.chef_famille_id = p.id

UNION

 SELECT d.id AS dossier_id, p.id AS personne_id
   FROM dossier d
   JOIN personne p ON d.conjoint_id = p.id)

UNION 

 SELECT d.id AS dossier_id, p.id AS personne_id
   FROM dossier d
   JOIN dossier_personnes_acharge_personne_dossiers_acharge j ON d.id = j.dossier_id
   JOIN personne p ON j.personne_id = p.id)

UNION 

 SELECT d.id AS dossier_id, p.id AS personne_id
   FROM dossier d
   JOIN dossier_personnes_non_acharge_personne_dossiers_non_acharge j ON d.id = j.dossier_id
   JOIN personne p ON j.personne_id = p.id;
