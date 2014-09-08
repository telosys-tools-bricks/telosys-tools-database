/**
 *  Copyright (C) 2008-2013  Telosys project org. ( http://www.telosys.org/ )
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.telosys.tools.commons.StandardTool;
import org.telosys.tools.commons.TelosysToolsLogger;

public class MetaDataManager extends StandardTool
{
	public MetaDataManager(TelosysToolsLogger logger) {
		super(logger);
	}

	//--------------------------------------------------------------------------------------------
	public List<String> getCatalogs(Connection con ) throws SQLException
	{
		log("getCatalogs(Connection) ..."  );
		//--- Get the database Meta-Data
		DatabaseMetaData dbmd = con.getMetaData();		
		
		return getCatalogs( dbmd );
	}

	//--------------------------------------------------------------------------------------------
	public List<String> getCatalogs(DatabaseMetaData dbmd) throws SQLException
	{
		log("getCatalogs(DatabaseMetaData) ..."  );
	    ResultSet rs = dbmd.getCatalogs();
	    
		LinkedList<String> list = new LinkedList<String>();
		
		int iCount = 0;
		while ( rs.next() ) 
		{
			iCount++;
			log("getCatalogs : try to build catalog #" + iCount +" ..." );			
			String catalog = MetaDataBuilder.buildCatalogMetaData(rs);
			log("getCatalogs : catalog #" + iCount +  " built ( name = " + catalog + " )");

			list.addLast(catalog);
		}
		rs.close();
		
		return list ;
	}

	//--------------------------------------------------------------------------------------------
	public List<SchemaMetaData> getSchemas(Connection con ) throws SQLException
	{
		log("getSchemas(Connection) ..."  );
		//--- Get the database Meta-Data
		DatabaseMetaData dbmd = con.getMetaData();		
		
		return getSchemas( dbmd );
	}

	//--------------------------------------------------------------------------------------------
	public List<SchemaMetaData> getSchemas(DatabaseMetaData dbmd) throws SQLException
	{
		log("getSchemas(DatabaseMetaData) ..."  );
	    ResultSet rs = dbmd.getSchemas();
	    
		LinkedList<SchemaMetaData> list = new LinkedList<SchemaMetaData>();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		log("getSchemas : column count = " + columnCount );
		
		//--- For each SCHEMA ...
		int iCount = 0;
		while ( rs.next() ) 
		{
			iCount++;
			log("getSchemas : try to build schema #" + iCount +" ..." );			
			SchemaMetaData schemaMetaData = MetaDataBuilder.buildSchemaMetaData(rs, columnCount);
			log("getSchemas : schema #" + iCount +  " built ( name = " + schemaMetaData.getSchemaName() + " )");

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
		log("getTables(..., '" + catalog + "', '" + schema + "', '" + tableNamePattern + "', " + tableTypes + ")");
		
	    /*
	     * JDBC JavaDoc for "getTables":
	     * catalog - a catalog name; 
	     *     must match the catalog name as it is stored in the database; 
	     *     "" retrieves those without a catalog; 
	     *     null means that the catalog name should not be used to narrow the search
	     *     
	     * schemaPattern - a schema name pattern; 
	     *     must match the schema name as it is stored in the database; 
	     *     "" retrieves those without a schema; 
	     *     null means that the schema name should not be used to narrow the search 
	     */
		
	    String schemaToUse = "!".equals(schema) ? null : schema ;
	    String catalogToUse = "!".equals(catalog) ? null : catalog ;
	    	
		//--- Get tables list
		ResultSet rs = dbmd.getTables(catalogToUse, schemaToUse, tableNamePattern, tableTypes);

		log("getTables : result set ready" );
		
		//--- Regexp of table names to include
		Pattern patternTableNameInclude = null;
		if(tableNameInclude != null && ! "".equals(tableNameInclude.trim())) {
			patternTableNameInclude = Pattern.compile(tableNameInclude);
		}
		
		//--- Regexp of table names to exclude
		Pattern patternTableNameExclude = null;
		if(tableNameExclude != null && ! "".equals(tableNameExclude.trim())) {
			patternTableNameExclude = Pattern.compile(tableNameExclude);
		}

		LinkedList<TableMetaData> tables = new LinkedList<TableMetaData>();
		
		//--- For each table ...
		int iTablesCount = 0;
		while ( rs.next() ) 
		{
			iTablesCount++;
			log("getTables : try to build table #" + iTablesCount +"..." );
			
			TableMetaData tableMetaData = MetaDataBuilder.buildTableMetaData(rs);
			log("getTables : table #" + iTablesCount +  " built ( name = " + tableMetaData.getTableName() + " )");

			String tableName = tableMetaData.getTableName();
			
			boolean isExclude = isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
						
//			//--- Primary Key columns for the current table
//			List pkColumns = getPKColumns(dbmd, tableMetaData.getCatalogName(), tableMetaData.getSchemaName(), tableMetaData.getTableName() );
//
//			//--- Foreign Keys columns for the current table
//			List fkColumns = getFKColumns(dbmd, tableMetaData.getCatalogName(), tableMetaData.getSchemaName(), tableMetaData.getTableName() );
//			
//			//--- All columns of the current table
//			List columns = getColumns(dbmd, tableMetaData.getCatalogName(), tableMetaData.getSchemaName(), tableMetaData.getTableName() );
			
			if( ! isExclude ) {
				tables.addLast(tableMetaData);
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
		boolean isExclude;
		if( patternTableNameExclude == null ) {
			//--- Regexp is not defined : the table is not excluded
			isExclude = false;
		} else {
			isExclude = patternTableNameExclude.matcher(tableName).matches();
		}

		//--- Test if the table name matches with the regexp of included tables names, only in the case where the table name is not excluded
		if( ! isExclude ) {
			if( patternTableNameInclude != null ) {
				boolean isInclude = patternTableNameInclude.matcher(tableName).matches();
				isExclude = ! isInclude;
			}
		}
		return isExclude;
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
		log("getColumns(..., " + catalog + ", " + schema + ", " + tableName + ")");
		
		LinkedList<ColumnMetaData> list = new LinkedList<ColumnMetaData>();

		//--- Get the columns of the table ...
		ResultSet rs = dbmd.getColumns(catalog, schema, tableName, "%");

		// --- For each column of the table ...
		while ( rs.next() ) 
		{
			ColumnMetaData columnMetaData =  MetaDataBuilder.buildColumnMetaData(rs);
			

//			// --- If this column is in the Table Primary Key
//			if (listPK.contains(dbColName.toUpperCase())) {
//				//column.setAttribute(DatabaseRepository.COLUMN_PRIMARY_KEY_ATTRIBUTE, "true");
//				column.setPrimaryKey(true);
//			}
//
//			// --- If this column is a member of a Foreign Key
//			setFkAttribute(dbColName, column, listFK);
			
			list.addLast(columnMetaData);
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
		log("getPKColumns(..., " + catalog + ", " + schema + ", " + tableName + ")");

		LinkedList<PrimaryKeyColumnMetaData> list = new LinkedList<PrimaryKeyColumnMetaData>();
	
		ResultSet rs = dbmd.getPrimaryKeys(catalog, schema, tableName);
		while ( rs.next() ) 
		{
			PrimaryKeyColumnMetaData pkColumnMetaData =  MetaDataBuilder.buildPKColumnMetaData(rs);
			list.addLast(pkColumnMetaData);
		}
		rs.close();
		
		return list ;
	}

	//--------------------------------------------------------------------------------------------
	/**
	 * @param dbmd
	 * 
	 * @param catalog
	 * 
	 * @param schema
	 * 
	 * @param tableName
	 * 
	 * @return
	 * 
	 * @throws SQLException
	 */
	public List<ForeignKeyColumnMetaData> getFKColumns(DatabaseMetaData dbmd, String catalog, String schema, String tableName) 
		throws SQLException
	{
		log("getFKColumns(..., " + catalog + ", " + schema + ", " + tableName + ")");

		LinkedList<ForeignKeyColumnMetaData> list = new LinkedList<ForeignKeyColumnMetaData>();
	
		ResultSet rs = dbmd.getImportedKeys(catalog, schema, tableName);
		while ( rs.next() ) 
		{
			ForeignKeyColumnMetaData fkColumnMetaData =  MetaDataBuilder.buildFKColumnMetaData(rs);
			list.addLast(fkColumnMetaData);
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
		log("getAutoIncrementedColumns(..., " + schemaName + ", " + tableName + ")");

		LinkedList<String> result = new LinkedList<String>();
		
		Statement stmt = conn.createStatement();
		
		String fullName = tableName.trim() ;
		if ( schemaName != null )
		{
			fullName = schemaName.trim() + "." + tableName.trim() ;
		}
		
		ResultSet rs = stmt.executeQuery("SELECT * FROM " + fullName + " WHERE 1 = 0");
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int n = rsmd.getColumnCount();
		
		// for each column 
		for ( int i = 1 ; i < n ; i++)
		{
			if ( rsmd.isAutoIncrement(i) ) 
			{
				String colName = rsmd.getColumnName(i);
				result.addLast(colName);
			}
		}
		return result ;
	}

}
