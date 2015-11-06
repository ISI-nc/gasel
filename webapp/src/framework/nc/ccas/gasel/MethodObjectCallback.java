package nc.ccas.gasel;

public class MethodObjectCallback extends ObjectCallback<BasePage, Object> {
	private static final long serialVersionUID = 1897921964368904379L;

	private final String method;

	public MethodObjectCallback(BasePage page, String method) {
		super(page);
		this.method = method;
	}

	public void performCallback(BasePage page, Object o) {
		try {
			page.getClass().getMethod(method, o.getClass()).invoke(page, o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	};

}
