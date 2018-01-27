package org.telosys.tools.db.model;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.db.commons.Derby;
import org.telosys.tools.db.observer.DatabaseObserverProvider;

public class DatabaseModelManagerDerbyIT {
	
	@Test
	public void test1() throws SQLException, TelosysToolsException {
		
		DatabaseObserverProvider.setMetadataObserverClass(MyMetadataObserver.class);
		DatabaseObserverProvider.setModelObserverClass(MyModelObserver.class);
		
		DatabaseModelManager dbmgr = new DatabaseModelManager();
		
		Connection con = Derby.getConnection();

		String[] tableTypes = { "TABLE" } ;
		String catalog = null ;
		String schema = "ROOT" ;
		DatabaseTables dbTables = dbmgr.getDatabaseTables(con, catalog, schema, "%", tableTypes, null, null);
		
		con.close();

		System.out.println("Tables found : size = " + dbTables.size() );
		for ( DatabaseTable t : dbTables ) {
			System.out.println(" . " + t.getTableName() );
		}
	}

}
