package org.telosys.tools.db.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;
import org.telosys.tools.commons.jdbc.ConnectionManager;
import org.telosys.tools.commons.logger.ConsoleLogger;

public class TestMetadataWithDerby {
	
	public static void main(String[] args) throws SQLException, TelosysToolsException{
		System.out.println("Test metadata / Derby ...");
		for ( int i = 1 ; i <= 10 ; i++ ) {
			System.out.println("\n--- # " + i );
			printDatabaseInfo();
		}
		
		for ( int i = 1 ; i <= 2 ; i++ ) {
			System.out.println("\n--- # " + i );
			ConnectionManager cm = createConnectionManager();
			System.out.println("Getting connection for 'Derby in memory' ...");
			Connection conn = cm.getConnection( getDerbyConfig() );
			System.out.println("Connection OK.");
			conn.close();
		}
		
	}
	
	public static ConnectionManager createConnectionManager() throws TelosysToolsException {
		System.out.println("Creating ConnectionManager ...");
		String libraries [] = { "/aaa/aaa", "/bbb/bbb" };
		return new ConnectionManager( libraries, new ConsoleLogger() );
	}
	
	public static void printDatabaseInfo() throws TelosysToolsException, SQLException {
		Connection conn = getDerbyConnection();
		DatabaseMetaData dbmd = conn.getMetaData();
		System.out.println("DatabaseMetaData obtained : " ) ;
		System.out.println(" product name    : " + dbmd.getDatabaseProductName() ) ;
		System.out.println(" product version : " + dbmd.getDatabaseProductVersion() ) ;
		System.out.println(" driver name     : " + dbmd.getDriverName() ) ;
		System.out.println(" driver version  : " + dbmd.getDriverVersion() ) ;
		conn.close();
	}
	
	public static Connection getDerbyConnection() throws TelosysToolsException, SQLException {

		ConnectionManager cm = new ConnectionManager( new ConsoleLogger() );

//		DatabaseConfiguration dbcfg = new DatabaseConfiguration();
//		dbcfg.setDatabaseId(0);
//		dbcfg.setDatabaseName("Derby-Bookstore");
//		dbcfg.setDriverClass("org.apache.derby.jdbc.ClientDriver");
//		dbcfg.setJdbcUrl("jdbc:derby://localhost:1527/bookstore");
//		dbcfg.setUser("root");
//		dbcfg.setPassword("admin");
//		
		System.out.println("Getting connection for 'Derby in memory' ...");
		Connection conn = cm.getConnection( getDerbyConfig() );
		System.out.println("Connection OK.");
		return conn;
	}

	public static DatabaseConfiguration getDerbyConfig() {

		DatabaseConfiguration dbcfg = new DatabaseConfiguration();
		dbcfg.setDatabaseId(0);
		dbcfg.setDatabaseName("Derby-Bookstore");
		dbcfg.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		dbcfg.setJdbcUrl("jdbc:derby://localhost:1527/bookstore");
		dbcfg.setUser("root");
		dbcfg.setPassword("admin");
		
		return dbcfg;
	}

}
