package org.telosys.tools.db.junit.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class H2DatabaseTest extends AbstractH2Database {
	
	private static final String PUBLIC = "PUBLIC";
	
	@Test
	public void test() throws SQLException {
		Connection connection = getConnection("h2_db_schema1.sql");
        DatabaseMetaData metaData = connection.getMetaData();

        //List<String> tables = getTableNames(metaData, PUBLIC);
        //String schemaNamePattern = PUBLIC; // only this schema
        //String schemaNamePattern = null;  // all schemas
        List<String> tables;
        
    	Output.print("--- All schemas:" );
        tables = getTableNames(metaData, null);  // all schemas
        for ( String tableName : tables) {
        	Output.print(" . " + tableName);
            //getIndexInfo(metaData, null, PUBLIC, tableName);
        }
        assertEquals("Should have 2 tables", 18, tables.size());
        
    	Output.print("--- Schemas " + PUBLIC );
        tables = getTableNames(metaData, PUBLIC);  // only this schema
        for ( String tableName : tables) {
        	Output.print(" . " + tableName);
            //getIndexInfo(metaData, null, PUBLIC, tableName);
        }
        assertEquals("Should have 2 tables", 3, tables.size());
        
		closeConnection(connection);
	}
	
	private void getIndexInfo(DatabaseMetaData metaData, String catalogName, String schemaName, String tableName) throws SQLException {
    	Output.print("Trying to get unique index for table " + tableName);
		// Get INDEX info to find UNIQUE
        ResultSet rs = metaData.getIndexInfo(catalogName, schemaName, tableName, 
        		true,  // when true, return only indices for UNIQUE values; when false, return indices regardless of whether unique or not
        		true); // when true, result is allowed to reflect approximate or out of data values
        		
        while (rs.next()) {
            String indexName = rs.getString("INDEX_NAME");
            String columnName = rs.getString("COLUMN_NAME");
            boolean nonUnique = rs.getBoolean("NON_UNIQUE");
            if (!nonUnique) {
            	Output.print("Index (UNIQUE): " + indexName + ", Column: " + columnName);
            }
        }
	}

}
