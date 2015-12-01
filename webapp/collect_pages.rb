
package = "nc.ccas.gasel"
clazz = "Pages"

# ---
require 'rexml/document'
# ---

WD = File.expand_path(File.dirname(__FILE__))

Dir.chdir "#{WD}/context/WEB-INF/"

pages = Dir.glob("**/*.page").sort.collect{|fn| fn[0..-6]}
descriptions = {}
pages.each do |p|
  puts p
  filename = "#{p}.page"
  
  doc = REXML::Document.new(File.new(filename)).root;
  
  skip = false
  doc.elements.each("meta") do |elt|
    if elt.attributes["key"] == "security" and elt.attributes["value"] == "skip"
      skip = true
      break
    end
  end
  next if skip
  
  elt = doc.elements["description"]
  if elt
    desc = elt.text.strip.gsub(/\s+/, " ");
  else
    $stdout.write "`--> description: "
    desc = $stdin.gets.strip.gsub(/\s+/, " ")
    next if desc.empty?
    
    add = (desc == "skip" \
          ? "<meta key=\"security\" value=\"skip\"/>" \
          : "<description>#{desc}</description>")
    
    contents = File.read(filename)
    contents.gsub!(/<page-specification.*>.*\n/){ "#{$&}\t#{add}\n"};
    File.open(filename, "wb"){|f| f.write contents}
    
    next if desc == "skip"
  end
  descriptions[p] = desc
  puts "`-=> #{descriptions[p]}"
end

File.open("#{WD}/src/core/#{package.gsub ".", "/"}/#{clazz}.java", "wb") do |f|
  
f.write %<package #{package};

import java.util.Map;
import java.util.TreeMap;

public class #{clazz} {

\tpublic static Map<String, String> PAGES = new TreeMap<String, String>();
\tstatic {
>

  descriptions.each do |page, description|
    f.puts "\t\tPAGES.put(\"#{page}\", \"#{description}\");"
  end
  
  f.puts "\t}"
  f.puts
  f.puts "}"
end