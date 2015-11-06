import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.modelUtils.CayenneUtils;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.cayenne.access.DataContext;

public class Temp {

	public static void main(String[] args) {
		DataContext oc = CayenneUtils.createDataContext();
		DataContext.bindThreadDataContext(oc);

		Dossier d19950225 = CommonQueries.findById(Dossier.class, 19950225);
		Dossier d20110004 = CommonQueries.findById(Dossier.class, 20110004);

		AspectAides aa19950225 = d19950225.getAspect(AspectAides.class);
		AspectAides aa20110004 = d20110004.getAspect(AspectAides.class);

		System.out.println("-- before --");
		System.out.println("dst counts: " + counts(aa19950225));
		System.out.println("src counts: " + counts(aa20110004));

		aa19950225.basculerAidesDepuis(aa20110004);

		System.out.println("-- after  --");
		System.out.println("dst counts: " + counts(aa19950225));
		System.out.println("src counts: " + counts(aa20110004));
		
		// oc.commitChanges();
	}

	private static String counts(AspectAides aspect) {
		return aspect.getAides().size() + "\t"
				+ aspect.getAidesRefusees().size();
	}

}
