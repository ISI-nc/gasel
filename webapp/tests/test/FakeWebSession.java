package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.tapestry.describe.DescriptionReceiver;
import org.apache.tapestry.web.WebSession;

public class FakeWebSession implements WebSession {

	private Map<String, Object> values = new TreeMap<>();

	@Override
	public Object getAttribute(String arg0) {
		return values.get(arg0);
	}

	@Override
	public List<?> getAttributeNames() {
		return new ArrayList<String>(values.keySet());
	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		values.put(arg0, arg1);
	}

	@Override
	public void describeTo(DescriptionReceiver arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void invalidate() {
		values.clear();
	}

	@Override
	public boolean isNew() {
		return values.isEmpty();
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub
	}

}
