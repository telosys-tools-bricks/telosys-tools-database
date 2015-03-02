/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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

import org.telosys.tools.db.metadata.ColumnMetaData;

public class DatabaseColumn {

	private ColumnMetaData columnMetaData ;
	
	private boolean inPrimaryKey = false ;
	
	private int     primaryKeySequence = 0 ;
	
	private int     usedInForeignKey = 0 ; // 1 : in one FK, 2 : in two FK, etc...
	
	private boolean autoIncremented = false ; 

	public DatabaseColumn(ColumnMetaData columnMetaData, boolean isPkPart, short pkSequence, int usedInForeignKey) 
	{
		super();
		this.columnMetaData = columnMetaData;
		this.inPrimaryKey = isPkPart ;
		if ( isPkPart )
		{
			this.primaryKeySequence = pkSequence ;
		}
		this.usedInForeignKey = usedInForeignKey ;
	}

	public String getColumnName() {
		return columnMetaData.getColumnName();
	}

	public String getComment() {
		return columnMetaData.getComment();
	}

	/**
	 * Returns the original database type name ( the native type ) <br>
	 * @return
	 */
	public String getDbTypeName() {
		return columnMetaData.getDbTypeName();
	}

	/**
	 * Returns the JDBC type code <br>
	 * See "java.sql.Types"
	 * @return
	 */
	public int getJdbcTypeCode() {
		return columnMetaData.getJdbcTypeCode();
	}

	/**
	 * Returns "true" if the column is "NOT NULL", else "false"
	 * @return
	 */
	public String getNotNullAsString() {
		return columnMetaData.getNotNullAsString();
	}

	public int getSize() {
		return columnMetaData.getSize();
	}

	public boolean isNotNull() {
		return columnMetaData.isNotNull();
	}
	
	/**
	 * Returns the column default value
	 * @return
	 */
	public String getDefaultValue() {
		return columnMetaData.getDefaultValue();
	}
	
	/**
	 * Returns the index of the column in table (starting at 1)
	 * @return
	 */
	public int getOrdinalPosition() {
		return columnMetaData.getOrdinalPosition();
	}
	
	/**
	 * Returns true if this column is in the Primary Key
	 * @return
	 */
	public boolean isInPrimaryKey() {
		return this.inPrimaryKey ;
	}

	/**
	 * Returns the sequence number within primary key 
	 * @return
	 */
	public int getPrimaryKeySequence() {
		return this.primaryKeySequence ;
	}
	
	/**
	 * Returns a int indicated if the column is used in zero, one or more foreign keys <br> 
	 * @return 0 : not used in FK, 1 : used in 1 FK, 2 : used in 2 FK, etc
	 */
	public int getUsedInForeignKey() {
		return this.usedInForeignKey ;
	}
	
	protected void setAutoIncremented(boolean v) {
		this.autoIncremented = v ;
	}
	
	/**
	 * Returns true if the column is autoincremented 
	 * @return
	 */
	public boolean isAutoIncremented() {
		return this.autoIncremented ;
	}
	
}
