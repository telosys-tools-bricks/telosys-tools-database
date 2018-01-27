package org.telosys.tools.db.model;

import org.telosys.tools.commons.observer.TaskObserver2;

public class MyModelObserver implements TaskObserver2<Integer, String> {

	public MyModelObserver() {
	}
	
	@Override
	public void notify(Integer level, String msg) {
		if ( level == 1 ) {
			System.out.println("---");			
		}
		System.out.println("Model notification    #" + level + " : " + msg );			
	}

}
