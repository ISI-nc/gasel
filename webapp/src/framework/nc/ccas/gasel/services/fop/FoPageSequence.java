package nc.ccas.gasel.services.fop;

import org.apache.tapestry.IMarkupWriter;

public class FoPageSequence extends FoAbstractElement<FoPageSequence> {

	public FoPageSequence(IMarkupWriter writer, String xmlns) {
		super(writer, xmlns);
	}

	public FoPageSequence(IMarkupWriter writer) {
		super(writer);
	}

	@Override
	protected String tag() {
		return "page-sequence";
	}

	@Override
	protected FoPageSequence fluentResult() {
		return this;
	}

	public FoFlow body(Object... attributes) {
		return flow("xsl-region-body", attributes);
	}

	public FoStaticContent header(Object... attributes) {
		return staticContent("xsl-region-before", attributes);
	}

	public FoStaticContent footer(Object... attributes) {
		return staticContent("xsl-region-after", attributes);
	}

	public FoStaticContent startSide(Object... attributes) {
		return staticContent("xsl-region-start", attributes);
	}

	public FoStaticContent endSide(Object... attributes) {
		return staticContent("xsl-region-end", attributes);
	}

	public FoFlow flow(String flowName, Object... attributes) {
		attributes = prependAttributes(attributes, "flow-name", flowName);
		return child(FoFlow.class, attributes);
	}

	public FoStaticContent staticContent(String flowName, Object... attributes) {
		attributes = prependAttributes(attributes, "flow-name", flowName);
		return child(FoStaticContent.class, attributes);
	}

	public FoArea area(String flowName, Object... attributes) {
		attributes = prependAttributes(attributes, "flow-name", flowName);
		return child(FoArea.class, attributes);
	}

}
