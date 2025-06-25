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
package org.telosys.tools.db.metadata.unique;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UniqueFinderForH2 extends AbstractUniqueFinder {

// Wrong 
//	private static final String SQL = 
//            " SELECT COLUMN_LIST " +
//            " FROM INFORMATION_SCHEMA.CONSTRAINTS " +
//            " WHERE TABLE_NAME = UPPER(?) AND CONSTRAINT_TYPE = 'UNIQUE'";
	private static final String SQL = 
			"SELECT " +
			"  ic.TABLE_CATALOG, ic.TABLE_SCHEMA, ic.TABLE_NAME, " +
			"  ic.INDEX_NAME, ic.ORDINAL_POSITION, ic.COLUMN_NAME, i.INDEX_TYPE_NAME " + 
			" FROM " +  
			"  INFORMATION_SCHEMA.INDEX_COLUMNS ic " +
			" JOIN "+ 
			"  INFORMATION_SCHEMA.INDEXES i "+
			"  ON  ic.INDEX_CATALOG = i.INDEX_CATALOG "+ 
			"  AND ic.INDEX_SCHEMA = i.INDEX_SCHEMA " +
			"  AND ic.INDEX_NAME = i.INDEX_NAME " +
			" ORDER BY  " +
			"  ic.TABLE_CATALOG, ic.TABLE_SCHEMA, ic.TABLE_NAME, " +
			"  ic.INDEX_NAME, " +
			"  ic.ORDINAL_POSITION";
	
	public UniqueFinderForH2() {
	}

	
	public void findUniques(Connection conn, String tableName) throws SQLException {
        try ( PreparedStatement ps = conn.prepareStatement(SQL) ) {
            //ps.setString(1, tableName);
            ResultSet rs = ps.executeQuery();
            printResultSet(rs);
        }
	}
	
}
