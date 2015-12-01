package nc.ccas.gasel.stats.repr;

import nc.ccas.gasel.stats.Representation;

public class RangeRepr extends Representation<Number> {

	private final Integer[] splitPoints;

	public RangeRepr(Integer... splitPoints) {
		super(Number.class);
		this.splitPoints = splitPoints;
	}

	@Override
	public String representationImpl(Number obj) {
		long value = Math.round(obj.doubleValue());
		for (int i = splitPoints.length - 1; i >= 0; i--) {
			if (value >= splitPoints[i]) {
				if (i == splitPoints.length - 1)
					return splitPoints[i] + "+";
				int p1 = splitPoints[i];
				int p2 = (splitPoints[i + 1] - 1);
				return p1 == p2 ? String.valueOf(p1) : ("" + p1 + ".." + p2);
			}
		}
		return "- de " + splitPoints[0];
	}

}