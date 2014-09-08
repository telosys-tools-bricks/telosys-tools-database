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

import org.telosys.tools.db.metadata.ForeignKeyColumnMetaData;

public class DatabaseForeignKeyColumn {

	private final ForeignKeyColumnMetaData foreignKeyColumnMetaData ;

	public DatabaseForeignKeyColumn(ForeignKeyColumnMetaData foreignKeyColumnMetaData) 
	{
		super();
		this.foreignKeyColumnMetaData = foreignKeyColumnMetaData;
	}

//	public int compareTo(Object obj) {
//		return foreignKeyColumnMetaData.compareTo(obj);
//	}
//
//	public boolean equals(Object arg0) {
//		return foreignKeyColumnMetaData.equals(arg0);
//	}

	public int getDeferrability() {
		return foreignKeyColumnMetaData.getDeferrability();
	}

	public int getDeleteRule() {
		return foreignKeyColumnMetaData.getDeleteRule();
	}

	public String getFkCatalogName() {
		return foreignKeyColumnMetaData.getFkCatalogName();
	}

	public String getFkColumnName() {
		return foreignKeyColumnMetaData.getFkColumnName();
	}

	public String getFkName() {
		return foreignKeyColumnMetaData.getFkName();
	}

	public String getFkSchemaName() {
		return foreignKeyColumnMetaData.getFkSchemaName();
	}

	public int getFkSequence() {
		return foreignKeyColumnMetaData.getFkSequence();
	}

	public String getFkTableName() {
		return foreignKeyColumnMetaData.getFkTableName();
	}

	public String getPkCatalogName() {
		return foreignKeyColumnMetaData.getPkCatalogName();
	}

	public String getPkColumnName() {
		return foreignKeyColumnMetaData.getPkColumnName();
	}

	public String getPkName() {
		return foreignKeyColumnMetaData.getPkName();
	}

	public String getPkSchemaName() {
		return foreignKeyColumnMetaData.getPkSchemaName();
	}

	public String getPkTableName() {
		return foreignKeyColumnMetaData.getPkTableName();
	}

	public int getUpdateRule() {
		return foreignKeyColumnMetaData.getUpdateRule();
	}

	public int hashCode() {
		return foreignKeyColumnMetaData.hashCode();
	}

	public String toString() {
		return foreignKeyColumnMetaData.toString();
	}
	
	
}
