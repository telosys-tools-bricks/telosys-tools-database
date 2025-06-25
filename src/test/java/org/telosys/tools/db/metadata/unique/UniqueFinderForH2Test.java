package org.telosys.tools.db.metadata.unique;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.Test;
import org.telosys.tools.db.junit.utils.AbstractH2Database;
import org.telosys.tools.db.junit.utils.Output;
import org.telosys.tools.db.model.DatabaseModelManager;
import org.telosys.tools.db.model.DatabaseTable;
import org.telosys.tools.db.model.DatabaseTables;

import static org.junit.Assert.*;

public class UniqueFinderForH2Test extends AbstractH2Database {

	@Test
	public void test() throws SQLException {
		UniqueFinderForH2 finder =  new UniqueFinderForH2();
		Connection connection = getConnection("h2_db_schema1.sql");
		
		DatabaseModelManager manager = new DatabaseModelManager();
		DatabaseTables tables = manager.getDatabaseTables(connection, 
					null, // Catalog 
					"PUBLIC", // Schema
					"%", 
					new String[]{"TABLE"},
					null, // Include pattern
					null); // Exclude pattern
        
    	Output.print("Tables found:");
        for ( DatabaseTable table : tables) {
        	Output.print(" . " + table.getTableName());
    		finder.findUniques(connection, table.getTableName());
        }
	}

}
