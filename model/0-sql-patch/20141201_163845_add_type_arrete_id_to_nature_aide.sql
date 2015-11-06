ALTER TABLE nature_aide
  ADD COLUMN type_arrete_id integer;
ALTER TABLE nature_aide ADD FOREIGN KEY (type_arrete_id) REFERENCES type_arrete (id);
