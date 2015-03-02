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

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.db.metadata.ForeignKeyColumnMetaData;

public class DatabaseForeignKey 
{

	private final String foreignKeyName ;
	
	private LinkedList<DatabaseForeignKeyColumn> foreignKeyColumns = new LinkedList<DatabaseForeignKeyColumn>();

	public DatabaseForeignKey(String foreignKeyName, List<ForeignKeyColumnMetaData> fkColumnsMetaData ) 
	{
		super();
		
		//--- The name of the Foreign Key
		this.foreignKeyName = foreignKeyName;
		
		//--- The columns of the Foreign Key
		if ( fkColumnsMetaData != null )
		{
//			Iterator iter = fkColumnsMetaData.iterator() ;
//			while ( iter.hasNext() )
//			{
//				ForeignKeyColumnMetaData fkCol = (ForeignKeyColumnMetaData) iter.next();
			for ( ForeignKeyColumnMetaData fkCol : fkColumnsMetaData ) {
				if ( fkCol != null )
				{
					String name = fkCol.getFkName();
					if ( name != null )
					{
						if ( name.equalsIgnoreCase(foreignKeyName) )
						{
							DatabaseForeignKeyColumn dbFK = new DatabaseForeignKeyColumn(fkCol);
							foreignKeyColumns.addLast(dbFK);
						}
					}
				}
			}
		}
		
	}

	public String getForeignKeyName() {
		return foreignKeyName;
	}

	public List<DatabaseForeignKeyColumn> getForeignKeyColumns() {
		return foreignKeyColumns;
	}

}
