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
package org.telosys.tools.db.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.telosys.tools.commons.StandardTool;
import org.telosys.tools.commons.TelosysToolsLogger;
import org.telosys.tools.db.metadata.ColumnMetaData;
import org.telosys.tools.db.metadata.ForeignKeyColumnMetaData;
import org.telosys.tools.db.metadata.MetaDataManager;
import org.telosys.tools.db.metadata.PrimaryKeyColumnMetaData;
import org.telosys.tools.db.metadata.TableMetaData;

public class DatabaseModelManager extends StandardTool
{

	public DatabaseModelManager(TelosysToolsLogger logger) {
		super(logger);
	}

	public DatabaseTables getDatabaseTables(Connection con, String catalog, String schema, 
			String tableNamePattern, String[] tableTypes,
			String tableNameInclude, String tableNameExclude ) throws SQLException
	{
		DatabaseTables databaseTables = new DatabaseTables();
		
		MetaDataManager mgr = new MetaDataManager( this.getLogger() );
		
		//--- Get the database Meta-Data
		DatabaseMetaData dbmd = con.getMetaData();		

		//--- Initialize the tables ( table, columns, PK, FK ) 
		List<TableMetaData> tablesMetaData = mgr.getTables(dbmd, catalog, schema, tableNamePattern, tableTypes, tableNameInclude, tableNameExclude);	
		
		//--- For each table get columns, primary key and foreign keys
		for ( TableMetaData tableMetaData : tablesMetaData ) {
			//--- Table columns
			List<ColumnMetaData> columnsMetaData = mgr.getColumns(dbmd, tableMetaData.getCatalogName(), tableMetaData.getSchemaName(), tableMetaData.getTableName() );

			//--- Table primary key columns
			List<PrimaryKeyColumnMetaData> pkColumnsMetaData = mgr.getPKColumns(dbmd, tableMetaData.getCatalogName(), tableMetaData.getSchemaName(), tableMetaData.getTableName() );

			//--- Table foreign keys columns
			List<ForeignKeyColumnMetaData> fkColumnsMetaData = mgr.getFKColumns(dbmd, tableMetaData.getCatalogName(), tableMetaData.getSchemaName(), tableMetaData.getTableName() );

			//--- Build the table model
			DatabaseTable databaseTable = new DatabaseTable(tableMetaData,columnsMetaData,pkColumnsMetaData,fkColumnsMetaData);
			
			//--- Set auto-incremented columns if any
			findAutoIncrementedColums(mgr, con, databaseTable);
			
			databaseTables.addTable(databaseTable);
		}
		
		//--- Initialize the stored procedures
		// in the future ...
			
		return databaseTables ;
	}
	
	private void findAutoIncrementedColums( MetaDataManager mgr, Connection con, DatabaseTable databaseTable ) throws SQLException
	{
		List<String> autoIncrColumns = null ;
		
//		try {
//			autoIncrColumns = mgr.getAutoIncrementedColumns(con, databaseTable.getSchemaName(), databaseTable.getTableName() );
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			// ERROR : cannot get autoincremented columns
//		}
		autoIncrColumns = mgr.getAutoIncrementedColumns(con, databaseTable.getSchemaName(), databaseTable.getTableName() );
		
		if ( autoIncrColumns != null ) {
			if ( ! autoIncrColumns.isEmpty() ) {
				for ( String columnName : autoIncrColumns ) {
					DatabaseColumn c = databaseTable.getColumnByName(columnName);
					if ( c != null ) {
						c.setAutoIncremented(true);
					}
				}
			}
		}
	}
}
