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

public class PrimaryKeyColumnMetaData 
{

	private String  catalogName ; // table catalog (may be null) 
	
	private String  schemaName ; // table schema (may be null) 
	
	private String  tableName ; // table name

	private String  columnName ; // column name
	
	private short   pkSequence = 0 ; // sequence number within primary key 
	
	private String  pkName ; // primary key name (may be null) 

	
	//----------------------------------------------------------------------------------
	public PrimaryKeyColumnMetaData(String catalogName, String schemaName, String tableName, 
			String columnName, short pkSequence, String pkName) {
		super();
		this.catalogName = catalogName;
		this.schemaName = schemaName;
		this.tableName = tableName;
		
		this.columnName = columnName;
		this.pkSequence = pkSequence;
		this.pkName = pkName;
	}

	//----------------------------------------------------------------------------------
	public String getCatalogName() {
		return catalogName;
	}

	//----------------------------------------------------------------------------------
	public String getColumnName() {
		return columnName;
	}

	//----------------------------------------------------------------------------------
	public String getPkName() {
		return pkName;
	}

	//----------------------------------------------------------------------------------
	public short getPkSequence() {
		return pkSequence;
	}

	//----------------------------------------------------------------------------------
	public String getSchemaName() {
		return schemaName;
	}

	//----------------------------------------------------------------------------------
	public String getTableName() {
		return tableName;
	}
}
