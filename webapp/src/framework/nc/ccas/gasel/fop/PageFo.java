package nc.ccas.gasel.fop;

import nc.ccas.gasel.services.fop.FoElement;
import nc.ccas.gasel.services.fop.FoPage;

import org.apache.tapestry.IMarkupWriter;

public class PageFo extends FoPage {

	public PageFo(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public PageFo(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected Object[] defaultSequenceAttributes() {
		return new Object[] { "font-family", "Helvetica", "font-size", "11pt" };
	}

	@Override
	protected void contributePageMaster(FoElement pageMaster) {
		writer.attribute("page-width", "21cm");
		writer.attribute("page-height", "29.7cm");
		writer.attribute("margin", "1cm");

		pageMaster.emptyChild("region-body", //
				"margin-top", "1cm", //
				"margin-bottom", "1cm");

		pageMaster.emptyChild("region-before", //
				"extent", "1cm");

		pageMaster.emptyChild("region-after", //
				"display-align", "after", //
				"extent", "1cm");
	}

}
