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

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.telosys.tools.commons.TelosysToolsException;

public class DbInfo {

    private final String url ;
    private final String databaseProductName ;
    private final String databaseProductVersion ;
    private final String driverName ;
    private final String driverVersion;
    private final int    maxConnections ;
    private final String userName ;
    private final int    defaultTransactionIsolation ;
    
    private final String catalogTerm ;
    private final String catalogSeparator ;
    
    private final String schemaTerm ;
    private final String searchStringEscape ;
	
	/**
	 * Constructor
	 * @param telosysToolsCfg
	 * @throws TelosysToolsException
	 */
	protected DbInfo(DatabaseMetaData dbmd ) throws SQLException {
		this.url = dbmd.getURL();
		this.databaseProductName = dbmd.getDatabaseProductName();
		this.databaseProductVersion = dbmd.getDatabaseProductVersion();
		this.driverName = dbmd.getDriverName() ;
		this.driverVersion = dbmd.getDriverVersion() ;
		this.maxConnections = dbmd.getMaxConnections() ;
		this.userName = dbmd.getUserName();
		this.defaultTransactionIsolation = dbmd.getDefaultTransactionIsolation();
		this.catalogTerm = dbmd.getCatalogTerm();
		this.catalogSeparator = dbmd.getCatalogSeparator() ;
		this.schemaTerm = dbmd.getSchemaTerm() ;
		this.searchStringEscape = dbmd.getSearchStringEscape()  ;
	}

	public String getUrl() {
		return url;
	}

	public String getDatabaseProductName() {
		return databaseProductName;
	}

	public String getDatabaseProductVersion() {
		return databaseProductVersion;
	}

	public String getDriverName() {
		return driverName;
	}

	public String getDriverVersion() {
		return driverVersion;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public String getUserName() {
		return userName;
	}

	public int getDefaultTransactionIsolation() {
		return defaultTransactionIsolation;
	}

	public String getCatalogTerm() {
		return catalogTerm;
	}

	public String getCatalogSeparator() {
		return catalogSeparator;
	}

	public String getSchemaTerm() {
		return schemaTerm;
	}

	public String getSearchStringEscape() {
		return searchStringEscape;
	}

}
