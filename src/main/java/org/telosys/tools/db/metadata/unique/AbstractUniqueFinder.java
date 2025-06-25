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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUniqueFinder {

	protected AbstractUniqueFinder() {
	}
	
    protected ResultSet runQuery(Connection conn, String sql, String table) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, table);
        return ps.executeQuery();
    }
    
    public void printResultSet(ResultSet rs) throws SQLException {
        ResultSetMetaData rsMetaData = rs.getMetaData();
        int columnCount = rsMetaData.getColumnCount();

        // Print column headers
        for (int i = 1; i <= columnCount; i++) {
            System.out.print(rsMetaData.getColumnLabel(i));
            if (i < columnCount) System.out.print(" | ");
        }
        System.out.println();
        System.out.println("----");

        // Print rows
        while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
                Object value = rs.getObject(i);
                System.out.print(value);
                if (i < columnCount) System.out.print(" | ");
            }
            System.out.println();
        }
    }

//    public abstract void processRow(ResultSet rs);
}
