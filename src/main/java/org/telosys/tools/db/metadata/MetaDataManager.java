/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.telosys.tools.db.metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.telosys.tools.commons.observer.TaskObserver2;
import org.telosys.tools.db.observer.DatabaseObserverProvider;

/**
 * This class allows to retrieve MetaData information
 * 
 * @author Laurent GUERIN
 *
 */
public class MetaDataManager {
	
	private final TaskObserver2<Integer,String> observer ;
	
    /**
     * Constructor
     */
    public MetaDataManager() {
		super();
		this.observer = DatabaseObserverProvider.getNewMetadataObserverInstance() ;
	}

    /**
     * Notify the observer if any
     * @param level
     * @param message
     */
    private void notify(int level, String message) {
    	if ( observer != null ) {
        	observer.notify(level, message);
    	}
    }
    
	/**
     * Get database information
     * @param con
     * @return
     * @throws SQLException
     */
    public DbInfo getDatabaseInfo(Connection con) throws SQLException {
    	notify(1, "Getting database information from connection");
        return getDatabaseInfo( con.getMetaData() );
    }
    
    /**
     * Get database information
     * @param dbmd
     * @return
     * @throws SQLException
     */
    public DbInfo getDatabaseInfo(DatabaseMetaData dbmd)  throws SQLException {
    	notify(1, "Getting database information from metadata");
        return new DbInfo(dbmd);
    }

	/**
	 * Get the catalogs
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public List<String> getCatalogs(Connection con) throws SQLException {
    	notify(1, "Getting catalogs from connection");
		return getCatalogs( con.getMetaData() );
	}

	/**
	 * Get the catalogs
	 * @param dbmd
	 * @return
	 * @throws SQLException
	 */
	public List<String> getCatalogs(DatabaseMetaData dbmd) throws SQLException {
    	notify(1, "Getting catalogs from metadata");
	    ResultSet rs = dbmd.getCatalogs();
	    
		LinkedList<String> list = new LinkedList<>();
		
		int count = 0;
		while ( rs.next() ) {
			count++;
			String catalog = MetaDataBuilder.buildCatalogMetaData(rs);
	    	notify(2, "Registering catalog #" + count + " : " + catalog );
			list.addLast(catalog);
		}
		rs.close();
		
		return list ;
	}

	/**
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public List<SchemaMetaData> getSchemas(Connection con) throws SQLException {
    	notify(1, "Getting schemas from connection");
		return getSchemas( con.getMetaData() );
	}

	/**
	 * @param dbmd
	 * @return
	 * @throws SQLException
	 */
	public List<SchemaMetaData> getSchemas(DatabaseMetaData dbmd) throws SQLException {
    	notify(1, "Getting schemas from metadata");
	    ResultSet rs = dbmd.getSchemas();
	    
		LinkedList<SchemaMetaData> list = new LinkedList<>();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		
		//--- For each SCHEMA ...
		int count = 0;
		while ( rs.next() ) {
			count++;
			SchemaMetaData schemaMetaData = MetaDataBuilder.buildSchemaMetaData(rs, columnCount);
	    	notify(2, "Registering schema #" + count + " : " + schemaMetaData.getSchemaName() );
			list.addLast(schemaMetaData);
		}
		rs.close();
		
		return list ;
	}

	//--------------------------------------------------------------------------------------------
	/**
	 * Returns a list of tables meta-data according with the given parameters 
	 * @param con
	 * @param catalog
	 * @param schema
	 * @param tableNamePattern
	 * @param tableTypes
	 * @param tableNameInclude
	 * @param tableNameExclude
	 * @return
	 * @throws SQLException
	 */
	public List<TableMetaData> getTables(Connection con, String catalog, String schema, 
			String tableNamePattern, String[] tableTypes,
			String tableNameInclude, String tableNameExclude ) throws SQLException
	{
    	notify(1, "Getting tables from connection");
		//--- Get the database Meta-Data
		DatabaseMetaData dbmd = con.getMetaData();		
		
		return getTables( dbmd, catalog, schema, tableNamePattern, tableTypes, tableNameInclude, tableNameExclude );
	}

	//--------------------------------------------------------------------------------------------
	/**
	 * Retrieves a description of the tables available in the given catalog. 
	 * Only table descriptions matching the catalog, schema, table name and type criteria are returned. 
	 * They are ordered by TABLE_TYPE, TABLE_SCHEM and TABLE_NAME. 
	 * 
	 * @param dbmd
	 *    the database metadata provider to use
	 *    
	 * @param catalog
	 *    a catalog name; must match the catalog name as it is stored in the database; 
	 *    "" retrieves those without a catalog; 
	 *    null means that the catalog name should not be used to narrow the search
	 * 
	 * @param schema
	 *    a schema name pattern; must match the schema name as it is stored in the database; 
	 *    "" retrieves those without a schema; 
	 *    null means that the schema name should not be used to narrow the search
	 *    
	 * @param tableNamePattern
	 *    a table name pattern; must match the table name as it is stored in the database
	 * 
	 * @param tableTypes
	 *    a list of table types to include; null returns all types 
	 * 
	 * @param tableNameInclude 
	 *    Pattern of tables names to include
	 * 
	 * @param tableNameExclude
	 *    Pattern of tables names to exclude
	 * 
	 * @return
	 * 
	 * @throws SQLException
	 */
	public List<TableMetaData> getTables(DatabaseMetaData dbmd, String catalog, String schema, 
			String tableNamePattern, String[] tableTypes,
			String tableNameInclude, String tableNameExclude ) throws SQLException
	{
    	notify(1, "Getting tables from metadata");
    	notify(2, ". catalog = '" + catalog + "'");
    	notify(2, ". schema  = '" + schema  + "'");
    	notify(2, ". metadata pattern = '" + tableNamePattern + "'" );
    	notify(2, ". metadata types   = " + Arrays.toString(tableTypes) );
    	notify(2, ". include pattern  = '" + tableNameInclude + "'" );
    	notify(2, ". exclude pattern  = '" + tableNameExclude + "'" );
		
	    /*
	     * JDBC JavaDoc for "getTables":
	     * 'catalog' : a catalog name
	     *     must match the catalog name as it is stored in the database
	     *     "" retrieves those without a catalog
	     *     null means that the catalog name should not be used to narrow the search
	     *     
	     * 'schemaPattern' : a schema name pattern
	     *     must match the schema name as it is stored in the database
	     *     "" retrieves those without a schema
	     *     null means that the schema name should not be used to narrow the search 
	     */
		
	    String schemaToUse = "!".equals(schema) ? null : schema ;
	    String catalogToUse = "!".equals(catalog) ? null : catalog ;
	    	
		//--- Get tables list
		ResultSet rs = dbmd.getTables(catalogToUse, schemaToUse, tableNamePattern, tableTypes);

		//--- Regexp of table names to include
		Pattern patternTableNameInclude = null;
		if (tableNameInclude != null && ! "".equals(tableNameInclude.trim())) {
			patternTableNameInclude = Pattern.compile(tableNameInclude);
		}
		
		//--- Regexp of table names to exclude
		Pattern patternTableNameExclude = null;
		if (tableNameExclude != null && ! "".equals(tableNameExclude.trim())) {
			patternTableNameExclude = Pattern.compile(tableNameExclude);
		}

		LinkedList<TableMetaData> tables = new LinkedList<>();
		
		//--- For each table ...
		int count = 0;
		while ( rs.next() ) {
			count++;
	    	notify(2, "Processing table #" + count );
			TableMetaData tableMetaData = MetaDataBuilder.buildTableMetaData(rs);
			
			String tableName = tableMetaData.getTableName() ;

			boolean isExclude = isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
			if( ! isExclude ) {
				tables.addLast(tableMetaData);
		    	notify(2, "Table #" + count + " : " + tableName + " registered" );
			}
			else {
		    	notify(2, "Table #" + count + " : " + tableName + " excluded" );
			}
		}
		rs.close();
		
		return tables ;
	}
	
	/**
	 * Cehck if the table is excluded or included.
	 * @param tableName Table name
	 * @param patternTableNameInclude Regexp of the table name to include
	 * @param patternTableNameExclude Regexp of the table name to exclude
	 * @return is excluded
	 */
	protected boolean isExcludedTable(String tableName, Pattern patternTableNameInclude, Pattern patternTableNameExclude) {
		//--- Test if the table name matches with the regexp of excluded tables names
		boolean isExcluded;
		if( patternTableNameExclude == null ) {
			//--- Regexp is not defined : the table is not excluded
			isExcluded = false;
		} else {
			isExcluded = patternTableNameExclude.matcher(tableName).matches();
		}

		//--- Test if the table name matches with the regexp of included tables names, only in the case where the table name is not excluded
		if( ! isExcluded ) {
			if( patternTableNameInclude != null ) {
				boolean isIncluded = patternTableNameInclude.matcher(tableName).matches();
				isExcluded = ! isIncluded;
			}
		}
		return isExcluded;
	}
	
	
	//--------------------------------------------------------------------------------------------
	/**
	 * Retrieves a description of table columns available in the specified catalog.
	 * Only column descriptions matching the catalog, schema, table and column name criteria are returned. 
	 * They are ordered by TABLE_SCHEM, TABLE_NAME, and ORDINAL_POSITION.
	 *  
	 * @param dbmd
	 *    the database metadata provider to use
	 *    
	 * @param catalog 
	 *    a catalog name; must match the catalog name as it is stored in the database; 
	 *    "" retrieves those without a catalog; 
	 *    null means that the catalog name should not be used to narrow the search
	 * 
	 * @param schema 
	 *    a schema name pattern; must match the schema name as it is stored in the database; 
	 *    "" retrieves those without a schema; 
	 *    null means that the schema name should not be used to narrow the search
	 * 
	 * @param tableName
	 *    a table name pattern; must match the table name as it is stored in the database
	 * 
	 * @return
	 * 
	 * @throws SQLException
	 */
	public List<ColumnMetaData> getColumns(DatabaseMetaData dbmd, String catalog, String schema, String tableName) 
		throws SQLException
	{
    	notify(1, "Getting columns from metadata for table '" + tableName + "'");
		
		LinkedList<ColumnMetaData> list = new LinkedList<>();

		//--- Get the columns of the table ...
		ResultSet rs = dbmd.getColumns(catalog, schema, tableName, "%");

		// --- For each column of the table ...
		while ( rs.next() ) {
			ColumnMetaData columnMetaData =  MetaDataBuilder.buildColumnMetaData(rs);			
			list.addLast(columnMetaData);
			notify(2, "Column " + columnMetaData.getColumnName() );
		}

		rs.close();
		
		return list ;
	}

	//--------------------------------------------------------------------------------------------
	/**
	 * Only column descriptions matching the catalog, schema, table and column name criteria are returned. 
	 * They are ordered by TABLE_SCHEM, TABLE_NAME, and ORDINAL_POSITION. 
	 * 
	 * @param dbmd
	 *    the database metadata provider to use
	 * 
	 * @param catalog 
	 *    a catalog name; must match the catalog name as it is stored in the database; 
	 *    "" retrieves those without a catalog; 
	 *    null means that the catalog name should not be used to narrow the search
	 *    
	 * @param schema
	 *    a schema name pattern; must match the schema name as it is stored in the database; 
	 *    "" retrieves those without a schema; 
	 *    null means that the schema name should not be used to narrow the search
	 *    
	 * @param tableName
	 * 
	 * @return
	 * 
	 * @throws SQLException
	 */
	public List<PrimaryKeyColumnMetaData> getPKColumns(DatabaseMetaData dbmd, String catalog, String schema, String tableName) 
		throws SQLException
	{
    	notify(1, "Getting PK columns from metadata for table '" + tableName + "'");

		LinkedList<PrimaryKeyColumnMetaData> list = new LinkedList<>();
	
		ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, tableName);
		while ( rs.next() ) {
			PrimaryKeyColumnMetaData pkColumnMetaData =  MetaDataBuilder.buildPKColumnMetaData(rs);
			list.addLast(pkColumnMetaData);
			notify(2, "PK column " + pkColumnMetaData.getColumnName() );
		}
		rs.close();
		
		return list ;
	}

	/**
	 * Returns the Foreign Key columns
	 * @param dbmd
	 * @param catalog
	 * @param schema
	 * @param tableName
	 * @return
	 * @throws SQLException
	 */
	public List<ForeignKeyColumnMetaData> getFKColumns(DatabaseMetaData dbmd, String catalog, String schema, String tableName) 
		throws SQLException
	{
    	notify(1, "Getting FK columns from metadata for table '" + tableName + "'");

		LinkedList<ForeignKeyColumnMetaData> list = new LinkedList<>();
	
		ResultSet rs = dbmd.getImportedKeys(catalog, schema, tableName);
		while ( rs.next() ) {
			ForeignKeyColumnMetaData fkColumnMetaData =  MetaDataBuilder.buildFKColumnMetaData(rs);
			list.addLast(fkColumnMetaData);
			notify(2, "FK column " + fkColumnMetaData.getFkColumnName() );
		}
		rs.close();
		
		//--- Sort by Foreign Key name 
		Collections.sort(list);

		return list ;
	}
	
	/**
	 * Returns a list of the autoincremented colomns for the given table
	 * @param conn
	 * @param schemaName the table schema, not used if null
	 * @param tableName 
	 * @return
	 * @throws SQLException
	 */
	public List<String> getAutoIncrementedColumns(Connection conn, String schemaName, String tableName) throws SQLException
	{
    	notify(1, "Getting auto-incremented columns for table '" + tableName + "'");

		LinkedList<String> result = new LinkedList<>();
		
		Statement stmt = conn.createStatement();
		
		ResultSet rs = executeSqlSelect(stmt, schemaName, tableName);
		if ( rs != null ) {
			ResultSetMetaData rsmd = rs.getMetaData();
			int n = rsmd.getColumnCount();
			
			// for each column ( from 1 to N )
			for ( int i = 1 ; i <= n ; i++) {
				if ( rsmd.isAutoIncrement(i) ) { // if auto-incremented column
					String colName = rsmd.getColumnName(i);
			    	notify(2, "Auto-incremented column found : '" + colName + "'");
					result.addLast(colName);
				}
			}
			
			rs.close();
		}
		
		stmt.close();

		return result ;
	}

	private ResultSet executeSqlSelect(Statement stmt, String schemaName, String tableName) {
		for ( int step = 1 ; step <=3 ; step++ ) {
			String sqlRequest = buildSqlRequest( schemaName, tableName, step);
			notify(3, "Trying to execute SQL #" + step + ": " + sqlRequest );
			try {
				ResultSet rs = stmt.executeQuery(sqlRequest);
				notify(3, "SQL result : OK");
				return rs ;
			} catch (Exception e) { // SQLException and more
				// Cannot execute 
				notify(3, "SQL result : ERROR : " + e.getMessage());
				// Continue : next step
			} 
		}
		return null ; // Not supposed to happen 
	}
	
	/**
	 * Build a different kind of request depending on the given 'step' <br>
	 * - Step #1 : SELECT * FROM   myschema.mytable   WHERE 1 = 0 <br>
	 * - Step #2 : SELECT * FROM "myschema"."mytable" WHERE 1 = 0 <br>
	 * - Step #3 : SELECT * FROM 'myschema'.'mytable' WHERE 1 = 0 <br>
	 * @param schemaName
	 * @param tableName
	 * @param step
	 * @return
	 */
	/*
	 * . ORACLE ok with : "myschema"."mytable" and myschema.mytable  
	 */
	private String buildSqlRequest(String schemaName, String tableName, int step) {
		String fullTableName = null ;
		if ( step == 1 ) {
			fullTableName = buildFullTableName(schemaName, tableName, null);
		}
		else if ( step == 2 ) {
			fullTableName = buildFullTableName(schemaName, tableName, "\"" );
		}
		else if ( step == 3 ) {
			fullTableName = buildFullTableName(schemaName, tableName, "'" );
		}
		else {
			throw new RuntimeException("buildSqlRequest() : invalid step value");
		}
		return "SELECT * FROM " + fullTableName + " WHERE 1 = 0" ;
	}
	
	private String buildFullTableName(String schemaName, String tableName, String quote) {
		if ( quote != null ) {
			// With quote, e.g. "myschema"."mytable"
			if ( schemaName != null ) {
				return quote + schemaName.trim() + quote + "." + quote + tableName.trim() + quote;
			}
			else {
				return quote + tableName.trim() + quote ;
			}
		}
		else {
			// Without quote, e.g. myschema.mytable
			if ( schemaName != null ) {
				return schemaName.trim() + "." + tableName.trim() ;
			}
			else {
				return tableName.trim()  ;
			}
		}
	}
}
