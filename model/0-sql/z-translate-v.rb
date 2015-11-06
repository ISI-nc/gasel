#! /usr/bin/ruby

require 'z-common'

Dir.chdir File.dirname(__FILE__)

Dir.glob("[0-9]*-v-*.sql").sort.each do |fn|
	puts "* #{fn}..."
	
	sql = File.read(fn)
	sql.gsub!(/"(\w+)"/){$1}
	sql.gsub! 'OR REPLACE ', ''
	sql = Sql.translate_select_body(sql)
	
	File.open("db2/#{fn}", "w") do |out|
		out.write sql
	end
end
