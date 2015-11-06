
CREATE OR REPLACE VIEW bon_imputation AS 
 SELECT b.id AS bon_id, n.imputation_id
   FROM bon b
   JOIN aide a ON b.aide_id = a.id
   JOIN nature_aide n ON a.nature_id = n.id;