
require 'RMagick'

def png2gif(src, bg_color)
	dst = src[0...-3] + "gif"
	puts "#{src} -> #{dst}"
	img = Magick::Image.read(src)[0]
	bg = Magick::Image.new(img.columns,img.rows) do
		self.background_color = bg_color
	end
	bg.composite! img, Magick::CenterGravity, Magick::OverCompositeOp
	bg = bg.transparent bg_color
	bg.write(dst)
end

