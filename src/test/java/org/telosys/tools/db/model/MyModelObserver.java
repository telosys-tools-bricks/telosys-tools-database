package org.telosys.tools.db.model;

import org.telosys.tools.commons.observer.TaskObserver2;
import org.telosys.tools.db.junit.utils.Output;

public class MyModelObserver implements TaskObserver2<Integer, String> {

	@Override
	public void notify(Integer level, String msg) {
		if ( level == 1 ) {
			Output.print("---");			
		}
		Output.print("Model notification    #" + level + " : " + msg );			
	}

}
