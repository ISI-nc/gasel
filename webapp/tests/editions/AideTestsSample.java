package editions;

import static java.util.Calendar.AUGUST;

import java.util.Calendar;

import nc.ccas.gasel.model.aides.Aide;
import nc.ccas.gasel.model.aides.AspectAides;
import nc.ccas.gasel.model.aides.Bon;
import nc.ccas.gasel.model.budget.Imputation;
import nc.ccas.gasel.model.budget.NatureAide;
import nc.ccas.gasel.model.core.Dossier;
import nc.ccas.gasel.model.core.Personne;
import nc.ccas.gasel.model.mairie.FournisseurMairie;
import nc.ccas.gasel.modelUtils.DateUtils;
import test.DataSample;

public class AideTestsSample {

	private static final DataSample sample = new DataSample();

	public static final Bon bon1 = sample.create(Bon.class, 1);
	public static final Bon bon2 = sample.create(Bon.class, 2);

	public static final Aide aide = sample.create(Aide.class, 1);

	public static final AspectAides aspectAides = sample
			.create(AspectAides.class);

	public static final Dossier dossier = sample.create(Dossier.class, 1);

	public static final NatureAide natureAide = sample.create( //
			NatureAide.class, 1);

	public static final Imputation imputation = sample.create( //
			Imputation.class, Imputation.ALIMENTATION);

	public static final Personne personne = sample.create(Personne.class, 1);

	public static final FournisseurMairie fournisseur1 = sample
			.create(FournisseurMairie.class);
	public static final FournisseurMairie fournisseur2 = sample
			.create(FournisseurMairie.class);
	public static final FournisseurMairie fournisseur3 = sample
			.create(FournisseurMairie.class);

	static {
		// Bon 1
		bon1.setAide(aide);
		bon1.setNumero("00000001");
		bon1.setMontant(1000);
		bon1.setPersonne(personne);
		bon1.setDebut(DateUtils.debutMois(2008, AUGUST));
		bon1.setFin(DateUtils.finMois(2008, AUGUST));

		// Bon 2
		sample.copy(bon1, bon2);
		bon2.setNumero("00000002");

		// Aide
		aide.setDossier(aspectAides);
		aide.setDebut(DateUtils.debutMois(2008, Calendar.JULY));
		aide.setFin(DateUtils.finMois(2008, Calendar.OCTOBER));
		aide.setQuantiteMensuelle(2);
		aide.setMontantUnitaire(1000);
		aide.setNature(natureAide);

		// AspectAides
		aspectAides.setDossier(dossier);

		// NatureAide
		natureAide.setLibelle("Nature de l'aide");
		natureAide.setImputation(imputation);
		natureAide.addToFournisseurs(fournisseur1);
		natureAide.addToFournisseurs(fournisseur2);
		natureAide.addToFournisseurs(fournisseur3);

		// Imputation
		imputation.setLibelle("Alimentation");

		// Personne
		personne.setNom("Nom personne");
		personne.setPrenom("Prénom personne");

		// Fournisseurs
		fournisseur1.setLibelle("Fournisseur 1");
		fournisseur2.setLibelle("Fournisseur 2");
		fournisseur3.setLibelle("Fournisseur 3");
	}

}
