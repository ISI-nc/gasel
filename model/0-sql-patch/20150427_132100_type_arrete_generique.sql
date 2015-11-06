ALTER TABLE type_arrete
  ADD COLUMN titre character varying(255);
ALTER TABLE type_arrete
  ADD COLUMN article1 text;
ALTER TABLE type_arrete
  ADD COLUMN infos_imputation text;
ALTER TABLE type_arrete
  ADD COLUMN avec_factures boolean;
