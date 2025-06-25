package org.telosys.tools.db.model;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.telosys.tools.db.junit.utils.AbstractH2Database;
import org.telosys.tools.db.junit.utils.Output;
import org.telosys.tools.db.observer.DatabaseObserverProvider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DatabaseModelManagerTest extends AbstractH2Database {

	@Test
	public void test() throws SQLException {
		Connection connection = getConnection("h2_db_schema1.sql");
		
		DatabaseObserverProvider.setMetadataObserverClass(MyMetadataObserver.class);
		
		DatabaseModelManager manager = new DatabaseModelManager();
		DatabaseTables tables = manager.getDatabaseTables(connection, 
					null, // Catalog 
					"PUBLIC", // Schema (or null for all)
					"%", 
					new String[]{"TABLE"},
					null, // Include pattern
					null); // Exclude pattern
		
        
    	Output.print("Tables found:");
        for ( DatabaseTable table : tables) {
        	Output.print(" . " + table.getTableName());
        }
        assertEquals(3, tables.size());
        assertNotNull( tables.getTableByName("USERS"));
        assertNotNull( tables.getTableByName("ORDERS"));
        
		closeConnection(connection);
	}

}
