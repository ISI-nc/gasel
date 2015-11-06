package nc.ccas.gasel.pages.dossiers;

import static java.util.Collections.singletonMap;
import static nc.ccas.gasel.modelUtils.DateUtils.today;
import static org.apache.cayenne.exp.Expression.fromString;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nc.ccas.gasel.BasePage;
import nc.ccas.gasel.Formats;
import nc.ccas.gasel.ValueFactory;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.modelUtils.DateUtils;

import org.apache.cayenne.access.DataContext;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry.annotations.Persist;
import org.apache.tapestry.event.PageEvent;

public abstract class Suppression extends BasePage {

	private static final int NB_ANNEES_EXPIRATION = 4;

	private static final Comparator<Dossier> YEAR_COMP = new Comparator<Dossier>() {

		public int compare(Dossier o1, Dossier o2) {
			return year(o1).compareTo(year(o2));
		}

		ThreadLocal<GregorianCalendar> CAL = new ThreadLocal<GregorianCalendar>() {
			protected GregorianCalendar initialValue() {
				return new GregorianCalendar();
			}
		};

		private Integer year(Dossier o) {
			Date ouverture = o.getDateOuverture();

			GregorianCalendar cal = CAL.get();
			cal.setTime(ouverture);
			return cal.get(Calendar.YEAR);
		}
	};

	public static Date getLimite() {
		return DateUtils.addYears(today(), -NB_ANNEES_EXPIRATION);
	}

	public List<Dossier> getDossiersASupprimer() {
		return getRequestCache().get(new ValueFactory<List<Dossier>>() {
			@SuppressWarnings("unchecked")
			public List<Dossier> buildValue() {
				Expression expr = fromString("modifDate < $limite")
						.expWithParameters(singletonMap("limite", getLimite()));
				SelectQuery query = new SelectQuery(Dossier.class, expr);

					query.addPrefetch("chefFamille");
				query.addPrefetch("adresseHabitation");
				query.addPrefetch("adresseHabitation.rue");
				query.addPrefetch("adresseHabitation.ville");

				List<Dossier> dossiers = getObjectContext().performQuery(query);
				return sort(dossiers) //
						.by(YEAR_COMP) //
						.by("chefFamille.nom") //
						.by("chefFamille.prenom") //
						.results();
			}
		}, "dossiers");
	}

	public NumberFormat getFormatNumero() {
		return Formats.NUMERO_DOSSIER;
	}

	/*
	 * Gestion de la sélection
	 */

	@Persist("workflow")
	public abstract Set<Dossier> getSelection();

	public abstract void setSelection(Set<Dossier> selection);

	public void supprimer() {
		DataContext context = getObjectContext();
		for (Dossier dossier : getSelection()) {
			dossier = localObject(dossier); // pourquoi??
			dossier.prepareForDeletion();
			context.deleteObject(dossier);
		}
		context.commitChanges();
		redirect();
	}

	@Override
	public void pageBeginRender(PageEvent event) {
		super.pageBeginRender(event);
		if (getSelection() == null) {
			setSelection(new HashSet<Dossier>());
		}
	}

}
