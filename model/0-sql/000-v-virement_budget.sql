
--
--
--

CREATE OR REPLACE VIEW virement_budget AS 

SELECT lv.id AS virement_id,
       lv.source_id AS budget_id,
       lv.source_type AS budget_type,
       - lv.montant AS montant

  FROM ligne_virement lv

UNION

SELECT lv.id AS virement_id,
       lv.destination_id AS budget_id,
       lv.destination_type AS budget_type,
       lv.montant
  FROM ligne_virement lv;
