package nc.ccas.gasel.reports;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java_gaps.NumberUtils;

import org.apache.hivemind.util.PropertyAdaptor;
import org.apache.hivemind.util.PropertyUtils;

public class ReportUtils {

	public static <T> void cumule(Cumulable<T> cible, List<? extends T> source) {
		for (T obj : source) {
			cible.cumule(obj);
		}
	}

	public static double somme(List<?> objects, String property) {
		double somme = 0;
		if (objects.isEmpty()) {
			return somme;
		}
		PropertyAdaptor adap = PropertyUtils.getPropertyAdaptor(objects.get(0),
				property);
		for (Object o : objects) {
			Number value = (Number) adap.read(o);
			if (value == null)
				continue;
			somme += value.doubleValue();
		}
		return somme;
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<T, Object> cumule(List<? extends Map<?, ?>> lignes) {
		Map<T, Object> cumul = new HashMap<T, Object>();
		for (Map<?, ?> ligne : lignes) {
			for (Entry<?, ?> entry : ligne.entrySet()) {
				if (!(entry.getValue() instanceof Number))
					continue;
				// ruby : cumul[entry.key] = (cumul[entry.key] || 0) + value
				Number value = (Number) entry.getValue();
				Number current = (Number) cumul.get(entry.getKey());
				cumul.put((T) entry.getKey(), NumberUtils.add(value, current));
			}
		}
		return cumul;
	}

	public static Integer asInteger(Object object) {
		Number n = (Number) object;
		return n == null ? null : n.intValue();
	}

}
