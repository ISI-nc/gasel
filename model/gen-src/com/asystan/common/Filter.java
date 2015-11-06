package com.asystan.common;

public interface Filter<T> {
	
	public boolean accept(T value);

}
