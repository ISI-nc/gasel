package nc.ccas.gasel.xls;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class XlsUtils {

	public static class Style {
		public final HSSFCellStyle style;

		public Style(HSSFWorkbook wb) {
			this(wb.createCellStyle());
		}

		public Style(HSSFCellStyle style) {
			this.style = style;
		}

		public Style border() {
			return border(HSSFCellStyle.BORDER_THIN);
		}

		public Style border(short borderStyle) {
			return border(borderStyle, HSSFColor.BLACK.index);
		}

		public Style border(short borderStyle, short color) {
			style.setBorderBottom(borderStyle);
			style.setBorderTop(borderStyle);
			style.setBorderLeft(borderStyle);
			style.setBorderRight(borderStyle);

			style.setBottomBorderColor(color);
			style.setTopBorderColor(color);
			style.setLeftBorderColor(color);
			style.setRightBorderColor(color);

			return this;
		}

		public Style borderTop(short borderStyle) {
			return borderTop(borderStyle, HSSFColor.BLACK.index);
		}

		public Style borderTop(short borderStyle, short color) {
			style.setBorderTop(borderStyle);
			style.setTopBorderColor(color);
			return this;
		}

		public Style borderBottom(short borderStyle) {
			return borderBottom(borderStyle, HSSFColor.BLACK.index);
		}

		public Style borderBottom(short borderStyle, short color) {
			style.setBorderBottom(borderStyle);
			style.setBottomBorderColor(color);
			return this;
		}

		public Style background(short color) {
			style.setFillForegroundColor(color);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			return this;
		}

		public Style background(Class<HSSFColor> color) {
			try {
				return background(color.getField("index").getShort(null));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		public Style date() {
			return date("d/m/yy");
		}

		public Style date(String format) {
			style.setDataFormat(HSSFDataFormat.getBuiltinFormat(format));
			return this;
		}

	}

}
