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

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.db.metadata.ColumnMetaData;
import org.telosys.tools.db.metadata.ForeignKeyColumnMetaData;
import org.telosys.tools.db.metadata.PrimaryKeyColumnMetaData;
import org.telosys.tools.db.metadata.TableMetaData;

public class DatabaseTable {

	private final TableMetaData tableMetaData ;

	private boolean       hasPrimaryKey   = false ;

	private String        primaryKeyName  = null ;
	
	private final LinkedList<DatabaseColumn>     columns     = new LinkedList<DatabaseColumn>();
	
	private final LinkedList<DatabaseForeignKey> foreignKeys = new LinkedList<DatabaseForeignKey>();
	
	
	public DatabaseTable(TableMetaData tableMetaData, 
			List<ColumnMetaData> columnsMetaData, 
			List<PrimaryKeyColumnMetaData> pkColumnsMetaData, 
			List<ForeignKeyColumnMetaData> fkColumnsMetaData ) 
	{
		super();
		
		//--- Table MetaData informations
		this.tableMetaData = tableMetaData ;
		
		//--- Table Primary Key 
		if ( pkColumnsMetaData != null )
		{
//			Iterator iter = pkColumnsMetaData.iterator() ;
//			while ( iter.hasNext() )
//			{
//				PrimaryKeyColumnMetaData pkCol = (PrimaryKeyColumnMetaData) iter.next();
			for( PrimaryKeyColumnMetaData pkCol : pkColumnsMetaData ) {
				if ( pkCol != null )
				{
					hasPrimaryKey = true ;
					primaryKeyName = pkCol.getPkName();
				}
			}
		}
		
		//--- Table Columns 
		if ( columnsMetaData != null )
		{
//			Iterator iter = columnsMetaData.iterator() ;
//			while ( iter.hasNext() )
//			{
//				ColumnMetaData colMetaData = (ColumnMetaData) iter.next();
			for( ColumnMetaData colMetaData : columnsMetaData ) {
				if ( colMetaData != null )
				{
					//--- Is it in the Primary Key ?
					boolean flagPK     = false ;
					short   pkSequence = 0 ;
					PrimaryKeyColumnMetaData pkCol = getPrimaryKeyColumnMetaData(colMetaData.getColumnName(), pkColumnsMetaData);
					if ( pkCol != null )
					{
						flagPK = true ;
						pkSequence = pkCol.getPkSequence();
					}

					//--- Is it in one or more Foreign Key ?
					int usedInForeignKey = usedInForeignKeys(colMetaData.getColumnName(), fkColumnsMetaData);
					
					DatabaseColumn databaseColumn = new DatabaseColumn(colMetaData, flagPK, pkSequence, usedInForeignKey);
					
					columns.addLast(databaseColumn);
				}
			}
		}
		
		//--- Table Foreign Keys ( NB : the Foreign Keys MetaData are supposed to be sorted by name )
		if ( fkColumnsMetaData != null )
		{
			String fkname = "";
//			Iterator iter = fkColumnsMetaData.iterator() ;
//			while ( iter.hasNext() )
//			{
//				ForeignKeyColumnMetaData fkCol = (ForeignKeyColumnMetaData) iter.next();
			for( ForeignKeyColumnMetaData fkCol : fkColumnsMetaData ) {
				if ( fkCol != null )
				{
					if ( ! fkCol.getFkName().equals(fkname) ) 
					{
						// Not the same Foreign Key name => create a new FK instance
						DatabaseForeignKey fk = new DatabaseForeignKey(fkCol.getFkName(), fkColumnsMetaData);
						foreignKeys.addLast(fk);
						
						fkname = fkCol.getFkName();
					}
				}
			}
		}
		
	}

	/**
	 * Returns the PrimaryKeyColumnMetaData instance for the given column name 
	 * if the the column is a part of the Primary Key (else returns nulls).
	 * 
	 * @param colName
	 * @param pkColumnsMetaData
	 * @return
	 */
	private PrimaryKeyColumnMetaData getPrimaryKeyColumnMetaData(String colName, List<PrimaryKeyColumnMetaData> pkColumnsMetaData)
	{
		if ( pkColumnsMetaData != null )
		{
//			Iterator iter = pkColumnsMetaData.iterator() ;
//			while ( iter.hasNext() )
//			{
//				PrimaryKeyColumnMetaData pkCol = (PrimaryKeyColumnMetaData) iter.next();
			for ( PrimaryKeyColumnMetaData pkCol : pkColumnsMetaData ) {
				if ( pkCol != null )
				{
					String pkColName = pkCol.getColumnName();
					if ( pkColName != null )
					{
						if ( pkColName.equalsIgnoreCase(colName) )
						{
							// Yes, this column is a part of the PK
							return pkCol ;
						}
					}
				}
			}
		}
		return null ;
	}
	
	private int usedInForeignKeys(String colName, List<ForeignKeyColumnMetaData> fkColumnsMetaData)
	{
		int n = 0 ;
		if ( fkColumnsMetaData != null )
		{
//			Iterator iter = fkColumnsMetaData.iterator() ;
//			while ( iter.hasNext() )
//			{
//				ForeignKeyColumnMetaData fkCol = (ForeignKeyColumnMetaData) iter.next();
			for ( ForeignKeyColumnMetaData fkCol : fkColumnsMetaData ) {
				if ( fkCol != null )
				{
					String name = fkCol.getFkColumnName();
					if ( name != null )
					{
						if ( name.equalsIgnoreCase(colName) )
						{
							// Yes, this column is used in a Foreign Key
							n++ ;
						}
					}
				}
			}
		}
		return n ;
	}

	/**
	 * Returns the catalog name for this table if any (may be null) 
	 * @return
	 */
	public String getCatalogName() {
		return tableMetaData.getCatalogName();
	}

	/**
	 * Returns the comment/remarks for this table. 
	 * @return
	 */
	public String getComment() {
		return tableMetaData.getComment();
	}

	/**
	 * Returns the schema name for this table if any (may be null) 
	 * @return
	 */
	public String getSchemaName() {
		return tableMetaData.getSchemaName();
	}

	/**
	 * Returns the name of this table. 
	 * @return
	 */
	public String getTableName() {
		return tableMetaData.getTableName();
	}

	/**
	 * Returns the meta-data table type.  <br>
	 * Typical types are : <br>
	 * "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM" <br>
	 * 
	 * @return
	 */
	public String getTableType() {
		return tableMetaData.getTableType();
	}
	
	/**
	 * Returns true if this table has a Primary Key
	 * @return
	 */
	public boolean hasPrimaryKey() {
		return this.hasPrimaryKey;
	}

	/**
	 * Returns the name of the Primary Key for this table if any, else returns null.
	 * @return
	 */
	public String getPrimaryKeyName() {
		return this.primaryKeyName;
	}
	
	public List<DatabaseColumn> getColumns() {
		return columns;
	}
	
	public List<DatabaseForeignKey> getForeignKeys() {
		return foreignKeys;
	}
	
	public DatabaseColumn getColumnByName(String columnName)
	{
		if ( null == columnName ) throw new IllegalArgumentException("Column name is null");
//		Iterator iter = columns.iterator();
//		while ( iter.hasNext() )
//		{
//			DatabaseColumn databaseColumn = (DatabaseColumn) iter.next();
		for ( DatabaseColumn databaseColumn : this.columns ) {
			if ( columnName.equals( databaseColumn.getColumnName() ) )
			{
				return databaseColumn ;
			}
		}
		return null ;
	}
	
	public DatabaseForeignKey getForeignKeyByName(String fkName)
	{
		if ( null == fkName ) throw new IllegalArgumentException("Foreign Key name is null");
//		Iterator iter = foreignKeys.iterator();
//		while ( iter.hasNext() )
//		{
//			DatabaseForeignKey foreignKey = (DatabaseForeignKey) iter.next();
		for ( DatabaseForeignKey foreignKey : this.foreignKeys ) {
			if ( fkName.equals( foreignKey.getForeignKeyName() ) )
			{
				return foreignKey ;
			}
		}
		return null ;
	}
	
}
