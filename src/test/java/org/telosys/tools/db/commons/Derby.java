package org.telosys.tools.db.commons;

import java.sql.Connection;
import java.sql.SQLException;

import org.telosys.tools.commons.TelosysToolsException;
import org.telosys.tools.commons.dbcfg.DatabaseConfiguration;
import org.telosys.tools.commons.jdbc.ConnectionManager;

public class Derby {

	private Derby() {
	}
	
	public static Connection getConnection() throws TelosysToolsException, SQLException {

		ConnectionManager cm = new ConnectionManager();

		System.out.println("Getting connection for 'Derby' ...");
		Connection conn = cm.getConnection( getDatabaseConfiguration() );
		System.out.println("Connection OK.");
		return conn;
	}

	public static DatabaseConfiguration getDatabaseConfiguration() {

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
