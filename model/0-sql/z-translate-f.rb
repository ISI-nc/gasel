#! /usr/bin/ruby

EXTRACT = {
}

FUNC_MATCH = /CREATE[^;]*;/m

Type_translations = {
	'int4' => 'integer',
	'boolean' => 'integer',
}

require 'z-common'

class SqlFunc
	def initialize(definition)
		@definition = definition
		@name = extract_name
		@params = extract_params
		@return = translate_type(extract_return)
		@body = extract_body.strip
	end

	def to_s
		"#{@name}: #{@params.collect{|x|x[1]}.join(' x ')} -> #{@return}"
	end

	def db2_drop
		"DROP FUNCTION #{@name}(#{@params.collect{|p|p[1]}.join(", ")});"
	end

	def db2_create
		"CREATE FUNCTION #{@name}" \
			"(#{@params.collect{|p|p.join(' ')}.join(', ')})" \
			" RETURNS #{@return}" \
			" LANGUAGE SQL" \
			" RETURN (#{body_db2});"
	end

	attr_reader :name

	private
	def extract_name
		/FUNCTION ([^ \(]+)/.match(@definition)[1].gsub('"','')
	end
	def extract_params
		i = 0
		/\(([^\)]*)\)/.match(@definition)[1] \
			.split(/\s*,\s*/) \
			.collect{|x|x.split(/\s+/)} \
			.collect{|n,t| i+=1; ["p#{i}_#{n}", translate_type(t)]}
	end
	def extract_return
		translate_type(/RETURNS\s+(\w+(\(\d+\))?)/.match(@definition)[1])
	end
	def extract_body
		definition = @definition.gsub('$BODY$','$_$')
		body = /\$_\$(.*)\$_\$/m.match(definition)[1]
		return body if body =~ /FROM/
		body.gsub(/\s*SELECT\s/,"")
	end

	def body_db2
		body = @body.dup
		# remplacement des params
		@params.each_with_index{|p, idx| body.gsub! "$#{idx+1}", p[0]}
		Sql.translate_select_body(body)
	end
	
	def translate_type(type)
		Type_translations[type] || type
	end
	
end

Dir.chdir File.dirname(__FILE__)

# Empty extract files
EXTRACT.each{|file, functions| File.open(file,"w"){}}

# Open global scripts
drop_script = File.open("db2/z-f-drop.sql", "w")
create_script = File.open("db2/z-f-create.sql", "w")
recreate_script = File.open("db2/z-f-recreate.sql", "w")

# Process files
Dir.glob("[0-9]*-f-*.sql").sort.each do |fn|
	puts "* #{fn}..."
	sql = File.read(fn)
	File.open("db2/#{fn}", "w") do |script|
		match = nil
		while match = FUNC_MATCH.match(match ? match.post_match : sql)
			f = SqlFunc.new(match[0])
			puts "  - #{f}"

			header = "\n---\n--- #{f}\n---"

			[script, drop_script, create_script, recreate_script].each do |out|
				out.puts header
			end
			
			[script, create_script].each do |out|
				out.puts "-- #{f.db2_drop}"
			end

			[drop_script, recreate_script].each do |out|
				out.puts f.db2_drop
			end

			[script, create_script, recreate_script].each do |out|
				out.puts f.db2_create
			end

			EXTRACT.each do |file, functions|
				next unless functions.index(f.name)
				File.open(file, "a") do |fd|
					fd.puts header
					td.puts "-- #{f.db2_drop}"
					fd.puts f.db2_create
				end
			end
		end
	end
end

# Close global scripts
[drop_script, create_script, recreate_script].each{|script| script.close}

