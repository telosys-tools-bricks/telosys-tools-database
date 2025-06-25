package org.telosys.tools.db.junit.utils;

public class Output {

	private static final boolean ENABLED = true ;
	
	private Output() {
	}
	
	public static void print(String msg) {
		if ( ENABLED ) {
			System.out.println(msg);
		}
	}

}
