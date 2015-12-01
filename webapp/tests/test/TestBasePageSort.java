package test;

import junit.framework.TestCase;
import nc.ccas.gasel.BasePagePsm;
import nc.ccas.gasel.BasePageSort;
import nc.ccas.gasel.model.pi.JardinFamilial;
import nc.ccas.gasel.modelUtils.CommonQueries;

import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.util.io.DataSqueezerUtil;


public class TestBasePageSort extends TestCase {

	static {
	}

	public void testBugPI001() throws Exception {
		new BasePageSort<JardinFamilial>(CommonQueries
				.getAll(JardinFamilial.class)) //
				.by(JardinFamilial.NOM_PROPERTY) //
				.byToString() //
				.results();

		BasePagePsm psm = new BasePagePsm(null) {
			@Override
			protected DataSqueezer squeezer() {
				return DataSqueezerUtil.createUnitTestSqueezer();
			}
		};

		psm.all("JardinFamilial", "nom");
	}

}
