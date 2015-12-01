package nc.ccas.gasel.services.fop;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IMarkupWriter;

public class FoPage extends FoContributor {

	// -- generally overidden --

	protected Object[] defaultSequenceAttributes() {
		return new Object[] {};
	}

	protected void contributePageMaster(FoElement pageMaster) {
		pageMaster.empty("region-body");
		pageMaster.empty("region-before");
		pageMaster.empty("region-after");
		pageMaster.empty("region-start");
		pageMaster.empty("region-end");
	}

	// -- end --

	private Object[] sequenceAttributes;

	public FoPage(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
		sequenceAttributes = defaultSequenceAttributes();
	}

	public FoPage(IMarkupWriter writer) {
		super(writer);
		sequenceAttributes = defaultSequenceAttributes();
	}

	public FoPage begin() {
		begin("root", "xmlns:fo", "http://www.w3.org/1999/XSL/Format");

		// Page definition
		FoElement masterSet = new FoElement("layout-master-set", writer)
				.begin();

		FoElement pageMaster = masterSet.child("simple-page-master",
				"master-name", "page");
		contributePageMaster(pageMaster);
		pageMaster.end();

		masterSet.end();

		return this;
	}

	public void end() {
		super.end("root"); // </fo:root>
	}

	public FoPage sequenceAttributes(Object... sequenceAttributes) {
		Defense.notNull(sequenceAttributes, "sequenceAttributes");
		this.sequenceAttributes = sequenceAttributes;
		return this;
	}

	public FoPageSequence sequence(Object... attributes) {
		if (attributes == null)
			attributes = sequenceAttributes;

		attributes = prependAttributes(attributes, "master-reference", "page");

		return new FoPageSequence(writer, xmlns).begin(attributes);
	}

}
