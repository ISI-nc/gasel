package nc.ccas.gasel.xls;

import nc.ccas.gasel.xls.XlsUtils.Style;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class CellStyles {

	public static final int HIGHLIGHT = 1;

	public static final int DOTTED = 2;

	public static final int DATE = 4;
	
	public static final int LAST = 8;

	private static final int STYLE_COUNT = 1 << 4;

	private static final short HIGHLIGHT_COLOR = HSSFColor.LIGHT_TURQUOISE.index;

	//private static final short DOTTED_BORDER_STYLE = (short) (HSSFCellStyle.BORDER_DOTTED);
	private static final short DOTTED_BORDER_STYLE = (short) (HSSFCellStyle.BORDER_NONE);

	private final HSSFWorkbook wb;

	public final HSSFCellStyle title;

	private final HSSFCellStyle[] styles = new HSSFCellStyle[STYLE_COUNT];

	public CellStyles(HSSFWorkbook wb) {
		this.wb = wb;
		title = baseStyle(wb).background(HSSFColor.LIGHT_CORNFLOWER_BLUE.index).style;
		for (int i = 0; i < STYLE_COUNT; i++)
			styles[i] = buildStyle(i);
	}

	public HSSFCellStyle cell(boolean highlight, boolean dottedTop, boolean date, boolean last) {
		int key = (highlight ? HIGHLIGHT : 0) //
				| (dottedTop ? DOTTED : 0) //
				| (date ? DATE : 0) //
				| (last ? LAST : 0);
		return styles[key];
	}

	private HSSFCellStyle buildStyle(int key) {
		XlsUtils.Style style = baseStyle(wb);
		if ((key & HIGHLIGHT) > 0)
			style.background(HIGHLIGHT_COLOR);
		if ((key & DATE) > 0)
			style.date();
		if ((key & DOTTED) > 0)
			style.borderTop(DOTTED_BORDER_STYLE);
		if ((key & LAST) == 0)
			style.borderBottom(HSSFCellStyle.BORDER_NONE);
		return style.style;
	}

	private Style baseStyle(HSSFWorkbook wb) {
		return new XlsUtils.Style(wb).border();
	}

}
