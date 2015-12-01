
module Sql
	
	# Translate de SELECT's from postgresql to DB2.
	def self.translate_select_body(body)
		body = body.dup
		# fonctions differentes
		body.gsub! "GREATEST","max"
		body.gsub! "LEAST", "min"
		body.gsub!(/DATE_PART\('([^']*)',\s*([^\)]*)\)/){|s| "#{$1}(#{$2})"}
		# casts
		body.gsub!(/([a-z_]+)::date/){|s| "date(#{$1})"}
		body.gsub!(/(\w+\([^\(]*\))::date/){|s| "date(#{$1})"}
		body.gsub!(/\(([^\(]*)\)::date/){|s| "date(#{$1})"}
		body.gsub!(/'([^']*)'::interval/){|s| "(#{$1})"}
		body.gsub! /::\w+/, ""
		body
	end

end

