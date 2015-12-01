package nc.ccas.gasel.xls;

import java.util.Map;
import java.util.TreeMap;

public class XlsServiceParam {

	private final String page;

	private final String componentPath;

	public XlsServiceParam(String page, String componentPath) {
		this.page = page;
		this.componentPath = componentPath;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new TreeMap<String, Object>();
		map.put("page", page);
		map.put("componentPath", componentPath);
		return map;
	}

	public String getPage() {
		return page;
	}

	public String getComponentPath() {
		return componentPath;
	}

}
