package java_gaps;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;

public abstract class Yield<T> implements Iterable<T>, Iterator<T> {

	protected abstract void run(Object... params) throws InterruptedException;

	private final Object[] params;

	private boolean finished = false;

	private Semaphore sem = new Semaphore(0);

	private Semaphore semProduce = new Semaphore(0);

	private Thread producer = null;

	private T next = null;

	private Exception ex = null;

	public Yield(Object... params) {
		this.params = params;
	}

	private void runImpl() {
		try {
			run(params);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RuntimeException ex) {
			this.ex = ex;
		} finally {
			finished = true;
			semProduce.release();
		}
	}

	public boolean hasNext() {
		waitData();
		if (ex != null) {
			throw new RuntimeException(ex);
		}
		return !finished;
	}

	public T next() {
		waitData();
		if (ex != null) {
			throw new RuntimeException(ex);
		}
		if (finished)
			throw new NoSuchElementException();
		T retval = next;
		next = null;
		sem.release();
		return retval;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	private synchronized void waitData() {
		if (next != null) {
			return;
		}
		init();
		try {
			semProduce.acquire();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void init() {
		if (producer != null)
			return;
		synchronized (this) {
			if (producer != null)
				return;
			producer = new Thread() {
				@Override
				public void run() {
					runImpl();
				}
			};
			producer.start();
		}
	}

	protected void yield(T object) throws InterruptedException {
		next = object;
		semProduce.release();
		sem.acquire();
	}

	public Iterator<T> iterator() {
		return this;
	}

}
