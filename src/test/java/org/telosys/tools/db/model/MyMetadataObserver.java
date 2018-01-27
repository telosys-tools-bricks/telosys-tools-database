package org.telosys.tools.db.model;

import org.telosys.tools.commons.observer.TaskObserver2;

public class MyObserver implements TaskObserver2<Integer, String> {

	public MyObserver() {
	}
	
	@Override
	public void notify(Integer level, String msg) {
		System.out.println("Observer notification : #" + level + " - " + msg );			
	}

}
