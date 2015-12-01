
COLOR = "#bbd7fe"

require '../../../png2gif'

Dir.glob("*.png") do |fn|
	png2gif fn, COLOR
end

