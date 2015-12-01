#! /usr/bin/ruby
# encoding: utf-8

query = "select table_name from  information_schema.columns"+
        " where table_schema = 'gasel_v2' and column_name = 'id'"
$tables = `psql -U postgres gasel -t -c "#{query}"`.strip.split.sort

$tables.each do |table|
    puts "SELECT setval('gasel_v2.pk_#{table}', (SELECT (MAX(id)+1)+(20-(MAX(id)+1)%20) FROM gasel_v2.#{table}), true);"
end

