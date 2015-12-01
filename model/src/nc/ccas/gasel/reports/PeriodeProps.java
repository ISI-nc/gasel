package nc.ccas.gasel.reports;

import java.util.Date;

import org.apache.tapestry.annotations.InitialValue;
import org.apache.tapestry.annotations.Persist;

public interface PeriodeProps {

	// Début

	@Persist("workflow")
	@InitialValue("ognl:defaultPeriodeDebut")
	public Date getPeriodeDebut();

	public void setPeriodeDebut(Date date);

	public Date getDefaultPeriodeDebut();

	// Fin

	@Persist("workflow")
	@InitialValue("ognl:defaultPeriodeFin")
	public Date getPeriodeFin();

	public void setPeriodeFin(Date date);

	public Date getDefaultPeriodeFin();

}
