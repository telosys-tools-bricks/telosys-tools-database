package org.telosys.tools.db.model;

import org.telosys.tools.commons.observer.TaskObserver2;
import org.telosys.tools.db.junit.utils.Output;

public class MyMetadataObserver implements TaskObserver2<Integer, String> {

	@Override
	public void notify(Integer level, String msg) {
		Output.print("Metadata notification #" + level + " : " + msg );			
	}

}
