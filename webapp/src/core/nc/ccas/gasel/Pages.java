package nc.ccas.gasel;

import java.util.Map;
import java.util.TreeMap;

public class Pages {

	public static Map<String, String> PAGES = new TreeMap<String, String>();
	static {
		PAGES.put("habitat/EditerSIE", "S.I.E.");
		PAGES.put("paph/EditerDossierPaPh", "Dossier PA/PH");
		PAGES.put("dossiers/Recherche", "Recherche de dossier");
		PAGES.put("pi/EditerParcelle", "Parcelle");
		PAGES.put("aides/GestionAide", "Page de gestion d'une aide");
		PAGES.put("compta/GestionBons", "Gestion des bons");
		PAGES.put("habilitations/Groupes", "Liste des groupes");
		PAGES.put("compta/Factures", "Liste des factures");
		PAGES.put("budget/annuel/NbDossierAides", "Rapport Nombre de dossiers aidés");
		PAGES.put("pi/ListeCollectivites", "Liste des collectivités");
		PAGES.put("pi/EditerDemandeJF", "Demande JF");
		PAGES.put("pe/stats/SuiviParents", "Suivi des parents");
		PAGES.put("parametres/pe/FormationEdit", "Formation");
		PAGES.put("parametres/pe/AutorisationParentaleEdit", "Autorisation parentale");
		PAGES.put("habilitations/EditerCompte", "Compte");
		PAGES.put("habitat/EditDemandeAffectation", "Demande d'affectation");
		PAGES.put("arretes/AjoutAides", "Ajout d'aides à un arrêté");
		PAGES.put("voirie/CommuneSearch", "Recherche de commune.");
		PAGES.put("pe/ListeDossierAM", "Liste des dossiers AM d'un dossier général");
		PAGES.put("parametres/aides/NatureAideEdit", "Nature d'aide");
		PAGES.put("paph/stats/Statistiques", "Statistiques");
		PAGES.put("dossiers/PersonneSearch", "Recherche de personne");
		PAGES.put("arretes/Liste", "Liste des arrêtés");
		PAGES.put("compta/BonsInutilises", "Liste des bons");
		PAGES.put("dossiers/stats/PersonnesPaio", "Statistiques sur les personnes en PAIO");
		PAGES.put("pi/EditerPaiement", "Paiement");
		PAGES.put("parametres/ListeEdit", "Elément d'une liste");
		PAGES.put("habitat/EditDemandeAideLogement", "Demande d'aide au logement");
		PAGES.put("habitat/stats/Rapport150Familles", "Rapport 150 familles");
		PAGES.put("habitat/EditerDossierHabitat", "Dossier habitat");
		PAGES.put("paph/EditerAvisCommission", "Avis de commission");
		PAGES.put("pi/EditerJardinFamilial", "Jardin familial");
		PAGES.put("pi/EditerArreteJF", "Arrêté JF");
		PAGES.put("pe/stats/Gardes", "Statistiques des gardes");
		PAGES.put("pe/EditerDossierPE", "Dossier PE");
		PAGES.put("budget/Liste", "Liste des budgets annuels");
		PAGES.put("parametres/pi/EditerTypeParcelle", "Type de parcelle");
		PAGES.put("budget/annuel/HATypePublic", "Rapport Hors alimentaire par type de public");
		PAGES.put("paph/DemanderTaxi", "Demande de taxi");
		PAGES.put("dossiers/Edition", "Dossier général");
		PAGES.put("habitat/EditerObjectif", "Objectif");
		PAGES.put("pi/ListeJardinsFamiliaux", "Liste de jardins familiaux");
		PAGES.put("parametres/Listes", "Listes");
		PAGES.put("habitat/SiteRHISearch", "Recherche de site RHI");
		PAGES.put("paph/ListeDossiersPaPh", "Dossiers PA/PH d'un dossier général");
		PAGES.put("budget/mensuel/EditBudgetImpMensuel", "Budget d'imputation mensuel");
		PAGES.put("aides/EditionBons", "Page d'édition des bons");
		PAGES.put("parametres/pe/ModeGardeEdit", "Mode de garde");
		PAGES.put("parametres/Constantes", "Constantes");
		PAGES.put("arretes/Creation", "Arrêté");
		PAGES.put("budget/mensuel/AlimOccImm", "Rapport Alimentation occasionnelle et immédiate (mensuel)");
		PAGES.put("aides/RechercheFournisseur", "Recherche de fournisseur");
		PAGES.put("pi/DocumentsPI", "Documents");
		PAGES.put("parametres/pi/EditerEtatParcelle", "Etat d'une parcelle");
		PAGES.put("budget/annuel/AlimPlurimensuel", "Rapport Alimentation plurimensuelle");
		PAGES.put("aides/ListeAidesDossier", "Liste des aides d'un dossier");
		PAGES.put("dossiers/PersonneEdit", "Personne");
		PAGES.put("budget/annuel/HANombre", "Rapport Hors alimentaire en nombre");
		PAGES.put("habitat/EditActionSociale", "Action sociale");
		PAGES.put("habilitations/Comptes", "Liste des comptes");
		PAGES.put("budget/annuel/SuiviBudgetAnnuel", "Rapport Suivi de budget annuel");
		PAGES.put("dossiers/Suppression", "Suppression de dossiers");
		PAGES.put("budget/annuel/MouvementsCredit", "Rapport Mouvements de crédit");
		PAGES.put("habilitations/GroupeEdit", "Groupe d'utilisateurs");
		PAGES.put("budget/annuel/CompteFoyers", "Rapport Foyers aidés");
		PAGES.put("compta/EditionBons", "Édition des bons (comptabilité)");
		PAGES.put("pi/TableauDeBord", "Tableau de bord");
		PAGES.put("pi/EditerCollectivite", "Collectivité");
		PAGES.put("pe/EditerDossierAM", "Dossier AM");
		PAGES.put("parametres/pi/EditerTypeArreteJF", "Type arrêté JF");
		PAGES.put("budget/annuel/SuiviAnnuelArticleAlimentation", "Rapport Suivi annuel des article d'alimentation");
		PAGES.put("budget/mensuel/BonsAnnules", "Liste des bons annulés");
		PAGES.put("Accueil", "Page d'accueil");
		PAGES.put("budget/mensuel/EditBudgetSecteurAide", "Budget par secteur d'aide");
		PAGES.put("habitat/EditerRHI", "Site RHI");
		PAGES.put("budget/Mouvement", "Mouvements de crédit");
		PAGES.put("voirie/VoieSearch", "Recherche de voie.");
		PAGES.put("pi/GestionDispositif", "Gestion du dispositif");
		PAGES.put("pi/EditerAttribution", "Attribution");
		PAGES.put("pe/stats/Frequentation", "Statistiques de fréquentation");
		PAGES.put("habitat/EditerProblematique", "Problématique");
		PAGES.put("compta/RetoursBons", "Facture");
		PAGES.put("budget/mensuel/AlimPlurimensuel", "Rapport Alimentation plurimensuelle (mensuel)");
		PAGES.put("paph/EditerDeplacement", "Déplacement");
		PAGES.put("pe/ListeDossierPE", "Liste des dossiers PE d'un dossier général");
		PAGES.put("parametres/aides/TypeAideEdit", "Type d'aide");
		PAGES.put("paph/EditerObjectif", "Objectif");
		PAGES.put("compta/Consultation", "Consultation (comptabilité)");
		PAGES.put("budget/annuel/EditBudgetImpAnnuel", "Budget d'imputation annuel");
		PAGES.put("budget/Creation", "Budget annuel");
		PAGES.put("paph/EditerAccompagnement", "Accompagnement");
		PAGES.put("budget/mensuel/ListeNominative", "Liste nominative");
		PAGES.put("budget/mensuel/Alimentation", "Rapport mensuel sur une imputation");
		PAGES.put("habilitations/Composants", "Définition des accès");
		PAGES.put("habitat/EditerActionCollective", "Action collective");
		PAGES.put("budget/mensuel/SuiviBudgetMensuel", "Rapport Suivi de budget mensuel");
		PAGES.put("dossiers/stats/PersonnesSuivies", "Statistiques sur les personnes suivies");
		PAGES.put("pi/EditerAspectPI", "Aspect PI");
		PAGES.put("paph/EditerReponse", "Réponse");
		PAGES.put("budget/annuel/HAValeur", "Rapport Hors alimentaire en valeur");
		PAGES.put("budget/mensuel/AlimTous", "Rapport Interventions en aide d'alimentation");
		PAGES.put("budget/annuel/AlimOccImm", "Rapport Alimentation occasionnelle et immédiate");
		PAGES.put("budget/mensuel/EditBudgetTypePublic", "Budget par type de public");
	}

}
