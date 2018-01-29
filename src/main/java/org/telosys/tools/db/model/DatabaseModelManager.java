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
package org.telosys.tools.db.model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import org.telosys.tools.commons.observer.TaskObserver2;
import org.telosys.tools.db.metadata.ColumnMetaData;
import org.telosys.tools.db.metadata.ForeignKeyColumnMetaData;
import org.telosys.tools.db.metadata.MetaDataManager;
import org.telosys.tools.db.metadata.PrimaryKeyColumnMetaData;
import org.telosys.tools.db.metadata.TableMetaData;
import org.telosys.tools.db.observer.DatabaseObserverProvider;

public class DatabaseModelManager {
	
	private final TaskObserver2<Integer,String> observer ;

	/**
	 * Constructor 
	 */
	public DatabaseModelManager() {
		super();
		this.observer = DatabaseObserverProvider.getNewModelObserverInstance() ;
	}
	
	/**
	 * Constructor with observer
	 * @param observer
	 */
	public DatabaseModelManager(TaskObserver2<Integer,String> observer) {
		super();
		this.observer = observer;
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

	public DatabaseTables getDatabaseTables(Connection con, String catalog, String schema, 
			String tableNamePattern, String[] tableTypes,
			String tableNameInclude, String tableNameExclude ) throws SQLException
	{
		DatabaseTables databaseTables = new DatabaseTables();
		
		MetaDataManager mgr = new MetaDataManager();
		
		//--- Get the database Meta-Data
		notify(1, "Getting database metadata");
		DatabaseMetaData dbmd = con.getMetaData();		

		//--- Initialize the tables ( table, columns, PK, FK ) 
		notify(1, "Getting tables metadata");
		List<TableMetaData> tablesMetaData = mgr.getTables(dbmd, catalog, schema, tableNamePattern, tableTypes, tableNameInclude, tableNameExclude);	
		int count = tablesMetaData.size() ;
		notify(1, count + " table(s) found");
		
		//--- For each table get columns, primary key and foreign keys
		int i = 0 ;
		for ( TableMetaData tableMetaData : tablesMetaData ) {
			i++ ;
			String tableName = tableMetaData.getTableName();
			notify(1, "Processing table '" + tableName + "' (" + i + " / " + count + ")" );
			
			//--- Table columns
			notify(2, "'" + tableName + "' : getting columns metadata" );
			List<ColumnMetaData> columnsMetaData = mgr.getColumns(dbmd, tableMetaData.getCatalogName(), tableMetaData.getSchemaName(), tableName );

			//--- Table primary key columns
			notify(2, "'" + tableName + "' : getting PK metadata");
			List<PrimaryKeyColumnMetaData> pkColumnsMetaData = mgr.getPKColumns(dbmd, tableMetaData.getCatalogName(), tableMetaData.getSchemaName(), tableName );

			//--- Table foreign keys columns
			notify(2, "'" + tableName + "' : getting FK metadata for table");
			List<ForeignKeyColumnMetaData> fkColumnsMetaData = mgr.getFKColumns(dbmd, tableMetaData.getCatalogName(), tableMetaData.getSchemaName(), tableName );

			//--- Build the table model
			DatabaseTable databaseTable = new DatabaseTable(tableMetaData, columnsMetaData, pkColumnsMetaData, fkColumnsMetaData);
			
			//--- Set auto-incremented columns if any
			notify(2, "'" + tableName + "' : finding auto-incremented columns");
			findAutoIncrementedColums(mgr, con, databaseTable);
			
			databaseTables.addTable(databaseTable);
		}
		notify(1, "End of metadata processing : " + count + " table(s) processed.");
		
		//--- Initialize the stored procedures
		// in the future ...
			
		return databaseTables ;
	}
	
	private void findAutoIncrementedColums( MetaDataManager mgr, Connection con, DatabaseTable databaseTable ) throws SQLException
	{
		List<String> autoIncrColumns = null ;
		
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
