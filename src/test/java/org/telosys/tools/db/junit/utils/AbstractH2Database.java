package org.telosys.tools.db.junit.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractH2Database {

	/**
	 * Get a connection for a "in-memory" H2 database initialized with the given SQL file name located in "src/test/resources"
	 * @param sqlFileName
	 * @return
	 * @throws SQLException
	 */
	protected Connection getConnection(String sqlFileName) throws SQLException  {
        String url = "jdbc:h2:mem:testdb;INIT=RUNSCRIPT FROM 'classpath:" + sqlFileName + "';DB_CLOSE_DELAY=-1";
        return DriverManager.getConnection(url, "sa", "");
	}
	
	protected void closeConnection(Connection connection) throws SQLException  {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }	
    }
	
	protected List<String> getTableNames(DatabaseMetaData metaData, String schemaNamePattern) throws SQLException  {
		List<String> tableNames = new ArrayList<>();
        ResultSet rs = metaData.getTables( 
        		null, // catalogName
        		schemaNamePattern,
        		"%",  // tableNamePattern
        		new String[]{"TABLE"} ) ; // types
        while (rs.next()) {
            String tableName  = rs.getString("TABLE_NAME");
            String schemaName = rs.getString("TABLE_SCHEM");
            tableNames.add(schemaName + "." + tableName);
        }
        return tableNames;
    }
	
}
