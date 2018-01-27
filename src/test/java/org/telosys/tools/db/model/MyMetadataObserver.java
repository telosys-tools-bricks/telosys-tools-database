package org.telosys.tools.db.model;

import org.telosys.tools.commons.observer.TaskObserver2;

public class MyMetadataObserver implements TaskObserver2<Integer, String> {

	public MyMetadataObserver() {
	}
	
	@Override
	public void notify(Integer level, String msg) {
		System.out.println("Metadata notification #" + level + " : " + msg );			
	}

}
