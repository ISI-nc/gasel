#! /usr/bin/ruby

WEB_INF = "#{File.dirname(__FILE__)}/context/WEB-INF"
Dir.chdir(WEB_INF)

Dir.glob("**/*.html") do |file_name|
	content = File.read(file_name)
	# Extraction des jwcid
	matches = {}
	content.gsub(/jwcid="([^@"$]+)(@[^"]+)?"/) do
		matches[$1] ||= 0
		matches[$1] += 1
	end
	matches.each{|k,v| matches.delete(k) if v < 2}
	next if matches.empty?
	puts "#{file_name}: #{matches.inspect}"
end

