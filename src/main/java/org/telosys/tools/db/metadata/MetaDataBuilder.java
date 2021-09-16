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
import java.sql.ResultSet;
import java.sql.SQLException;

public class MetaDataBuilder {

	/**
	 * Private constructor
	 */
	private MetaDataBuilder() {
	}

	//--------------------------------------------------------------------------------------------
	// CATALOG 
	//--------------------------------------------------------------------------------------------
	protected static String buildCatalogMetaData( ResultSet rs ) throws SQLException
	{
	    return rs.getString(TABLE_CAT);
	}

	//--------------------------------------------------------------------------------------------
	// SCHEMA 
	//--------------------------------------------------------------------------------------------
	/*  1 */ private static final String TABLE_CATALOG  = "TABLE_CATALOG" ; // table catalog (may be null) 
	/*  2 */ private static final String TABLE_SCHEM    = "TABLE_SCHEM" ;   // table schema  
	// NB : Specific ResultSet for Oracle : only one column with TABLE_SCHEM
	protected static SchemaMetaData buildSchemaMetaData( ResultSet rs, int columnCount ) throws SQLException
	{
	    String catalogName = null ;
		if ( columnCount > 1 ) {
		    catalogName = rs.getString(TABLE_CATALOG);
		}
		else {
			// Specific case for ORACLE (only one column)
		    catalogName = "" ;
		}
	    String schemaName = rs.getString(TABLE_SCHEM);
	    return new SchemaMetaData( catalogName, schemaName );
	}
	
	//--------------------------------------------------------------------------------------------
	// TABLE 
	//--------------------------------------------------------------------------------------------
	// Each "table" description has the following columns : 
	/*  1 */ private static final String TABLE_CAT      = "TABLE_CAT" ; // table catalog (may be null) 
	//  2 */ private static final String TABLE_SCHEM    = "TABLE_SCHEM" ; // table schema (may be null) 
	/*  3 */ private static final String TABLE_NAME     = "TABLE_NAME" ; // table name 
	/*  4 */ private static final String TABLE_TYPE     = "TABLE_TYPE" ; // table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM". 
	/*  5 */ private static final String REMARKS        = "REMARKS" ; // explanatory comment on the table 
	//  6 */ private static final String TYPE_CAT       = "TYPE_CAT" ; // the types catalog (may be null) 
	//  7 */ private static final String TYPE_SCHEM     = "TYPE_SCHEM" ; // the types schema (may be null) 
	/*  8 */ private static final String TYPE_NAME      = "TYPE_NAME" ; // type name (may be null) 
	//  9 */ private static final String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME" ; // name of the designated "identifier" column of a typed table (may be null) 
	// 10 */ private static final String REF_GENERATION = "REF_GENERATION" ; // specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null)
	
//	/*  1 */ private static final int TABLE_CAT      = 1 ; // table catalog (may be null) 
//	/*  2 */ private static final int TABLE_SCHEM    = 2 ; // table schema (may be null) 
//	/*  3 */ private static final int TABLE_NAME     = 3 ; // table name 
//	/*  4 */ private static final int TABLE_TYPE     = 4 ; // table type. Typical types are "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL TEMPORARY", "ALIAS", "SYNONYM". 
//	/*  5 */ private static final int REMARKS        = 5 ; // explanatory comment on the table 
//	//  6 */ private static final String TYPE_CAT       = "TYPE_CAT" ; // the types catalog (may be null) 
//	//  7 */ private static final String TYPE_SCHEM     = "TYPE_SCHEM" ; // the types schema (may be null) 
//	/*  8 */ private static final int TYPE_NAME      = 8 ; // type name (may be null) 
//	//  9 */ private static final String SELF_REFERENCING_COL_NAME = "SELF_REFERENCING_COL_NAME" ; // name of the designated "identifier" column of a typed table (may be null) 
//	// 10 */ private static final String REF_GENERATION = "REF_GENERATION" ; // specifies how values in SELF_REFERENCING_COL_NAME are created. Values are "SYSTEM", "USER", "DERIVED". (may be null) 
	//--------------------------------------------------------------------------------------------
	protected static TableMetaData buildTableMetaData( ResultSet rs ) throws SQLException
	{
	    String catalogName = rs.getString(TABLE_CAT);
	    String schemaName  = rs.getString(TABLE_SCHEM);
	    String tableName   = rs.getString(TABLE_NAME);	    
	    String tableType   = rs.getString(TABLE_TYPE);
	    String comment     = rs.getString(REMARKS);

	    return new TableMetaData(tableName, tableType, catalogName, schemaName, comment);
	}
	
	//--------------------------------------------------------------------------------------------
	// TABLE COLUMN
	//--------------------------------------------------------------------------------------------
	// Each "column" description has the following columns: 
	//  1    private static final String TABLE_CAT      = "TABLE_CAT" ; // table catalog (may be null) 
	//  2    private static final String TABLE_SCHEM    = "TABLE_SCHEM" ; // table schema (may be null) 
	//  3    private static final String TABLE_NAME     = "TABLE_NAME" ; // table name 
	/*  4 */ private static final String COLUMN_NAME    = "COLUMN_NAME" ;
	/*  5 */ private static final String DATA_TYPE      = "DATA_TYPE" ; // int : SQL type from java.sql.Types
	//  6    private static final String TYPE_NAME      = "TYPE_NAME" ; // type name (may be null) 
	/*  7 */ private static final String COLUMN_SIZE    = "COLUMN_SIZE" ; // int : column size. For char or date types this is the maximum number of characters, for numeric or decimal types this is precision. 
	//  8    BUFFER_LENGTH ( not used )
	/*  9 */ private static final String DECIMAL_DIGITS = "DECIMAL_DIGITS" ; // int : the number of fractional digits 
	/* 10 */ private static final String NUM_PREC_RADIX = "NUM_PREC_RADIX" ; // int : Radix (typically either 10 or 2) 
	/* 11 */ private static final String NULLABLE       = "NULLABLE" ; // int : is NULL allowed
	// 12    REMARKS 
	/* 13 */ private static final String COLUMN_DEF     = "COLUMN_DEF" ; // String : default value (may be null) 
	// 14    SQL_DATA_TYPE // int : unused  
	// 15    SQL_DATETIME_SUB // int : unused  
	/* 16 */ private static final String CHAR_OCTET_LENGTH = "CHAR_OCTET_LENGTH" ; // int : for char types the maximum number of bytes in the column
	/* 17 */ private static final String ORDINAL_POSITION  = "ORDINAL_POSITION" ; // int : index of column in table (starting at 1) 
	// 18 to 22 unused 
	//--------------------------------------------------------------------------------------------
	protected static ColumnMetaData buildColumnMetaData( ResultSet rs ) throws SQLException
	{
		//--- 1 
	    String tableCatalog = rs.getString(TABLE_CAT);
		
	    //--- 2 
	    String tableSchema  = rs.getString(TABLE_SCHEM);
	    
		//--- 3 
	    String tableName    = rs.getString(TABLE_NAME);

	    //--- 4 : Column Name
	    String colName = rs.getString(COLUMN_NAME);
	
	    //--- 5 : Column JDBC Type (cf "java.sql.Types" )
	    int jdbcTypeCode = rs.getInt(DATA_TYPE);
	
	    //--- 6 : Column Type (original database type)
	    String dbTypeName = rs.getString(TYPE_NAME);	    
	
	    //--- 7 : Column Size : 
	    // For "char" or "date" types this is the maximum number of characters, 
	    // for "numeric" or "decimal" types this is precision
	    int size = rs.getInt(COLUMN_SIZE); 

	    //--- 9 : the number of fractional digits  
	    int decimalDigits = rs.getInt(DECIMAL_DIGITS); 
	    
	    //--- 10 : Radix (typically either 10 or 2) 
	    int numPrecRadix = rs.getInt(NUM_PREC_RADIX); 
	    
	    //--- 11 : Column Nullable (original database type)
	    boolean notNull = false;
	    int nullable = rs.getInt(NULLABLE);
	    // 3 values :
	    // * columnNoNulls - might not allow NULL values
	    // * columnNullable - definitely allows NULL values
	    // * columnNullableUnknown - nullability unknown 
	    if ( nullable == DatabaseMetaData.columnNoNulls )
	    {
	    	notNull = true ;
	    }
	    
	    //--- 12 : Column Comment/Remarks :
	    String comment = rs.getString(REMARKS);
	    
	    //--- 13 : Column default value :
	    String defaultValue = rs.getString(COLUMN_DEF);
	    
	    //--- 16 : 
	    int charOctetLength = rs.getInt(CHAR_OCTET_LENGTH); 
	    
	    //--- 17 : 
	    int ordinalPosition = rs.getInt(ORDINAL_POSITION); 

	    ColumnMetaData columnMetaData = 
			new ColumnMetaData(
					tableCatalog, tableSchema, tableName,
					colName, jdbcTypeCode, dbTypeName, notNull, size,
					decimalDigits, numPrecRadix, 
					charOctetLength, ordinalPosition,
					defaultValue,
					comment);
		
		return columnMetaData ;
	}
	
	//--------------------------------------------------------------------------------------------
	// PRIMARY KEY COLUMNS
	//--------------------------------------------------------------------------------------------
	// Each "primary key column" description has the following columns :
	//  1    private static final String TABLE_CAT      = "TABLE_CAT" ; // table catalog (may be null) 
	//  2    private static final String TABLE_SCHEM    = "TABLE_SCHEM" ; // table schema (may be null) 
	//  3    private static final String TABLE_NAME     = "TABLE_NAME" ; // table name 
	//  4    private static final String COLUMN_NAME    = "COLUMN_NAME" ; // column name
	/*  5 */ private static final String KEY_SEQ        = "KEY_SEQ" ; // short => sequence number within primary key
	/*  6 */ private static final String PK_NAME        = "PK_NAME" ; // String => primary key name (may be null) 
	
	//--------------------------------------------------------------------------------------------
	protected static PrimaryKeyColumnMetaData buildPKColumnMetaData( ResultSet rs ) throws SQLException
	{
	    String tableCatalog = rs.getString(TABLE_CAT);
	    String tableSchema  = rs.getString(TABLE_SCHEM);
	    String tableName    = rs.getString(TABLE_NAME);
	    String columnName   = rs.getString(COLUMN_NAME);
	    short  keySequence  = rs.getShort(KEY_SEQ);
	    String pkName       = rs.getString(PK_NAME);
	    
	    PrimaryKeyColumnMetaData primaryKeyMetaData = 
	    	new PrimaryKeyColumnMetaData(
	    			tableCatalog, tableSchema, tableName, 
	    			columnName, keySequence, pkName);
	    
	    return primaryKeyMetaData ;
	}
	
	//--------------------------------------------------------------------------------------------
	// FOREIGN KEY COLUMNS
	//--------------------------------------------------------------------------------------------
	/*  1 */ private static final String PKTABLE_CAT    = "PKTABLE_CAT" ; // primary key table catalog being imported (may be null) 
	/*  2 */ private static final String PKTABLE_SCHEM  = "PKTABLE_SCHEM" ; // primary key table schema being imported (may be null) 
	/*  3 */ private static final String PKTABLE_NAME   = "PKTABLE_NAME" ; // primary key table name being imported 
	/*  4 */ private static final String PKCOLUMN_NAME  = "PKCOLUMN_NAME" ; // primary key column name being imported 

	/*  5 */ private static final String FKTABLE_CAT    = "FKTABLE_CAT" ; // foreign key table catalog (may be null) 
	/*  6 */ private static final String FKTABLE_SCHEM  = "FKTABLE_SCHEM" ; // foreign key table schema (may be null) 
	/*  7 */ private static final String FKTABLE_NAME   = "FKTABLE_NAME" ; // foreign key table name 
	/*  8 */ private static final String FKCOLUMN_NAME  = "FKCOLUMN_NAME" ; // foreign key column name 
	
	//  9    private static final String KEY_SEQ        = "KEY_SEQ" ; // short => sequence number within foreign key

	/* 10 */ private static final String UPDATE_RULE    = "UPDATE_RULE" ; // short => What happens to a foreign key when the primary key is updated:  
	/* 11 */ private static final String DELETE_RULE    = "DELETE_RULE" ; // short => What happens to the foreign key when primary is deleted. 
	
	/* 12 */ private static final String FK_NAME        = "FK_NAME" ; // String => foreign key name (may be null) 
	// 13    private static final String PK_NAME        = "PK_NAME" ; // String => primary key name (may be null)  

	/* 14 */ private static final String DEFERRABILITY  = "DEFERRABILITY" ; // short => can the evaluation of foreign key constraints be deferred until commit

	//--------------------------------------------------------------------------------------------
	protected static ForeignKeyColumnMetaData buildFKColumnMetaData( ResultSet rs ) throws SQLException
	{
	    String pkTableCatalog = rs.getString(PKTABLE_CAT);
	    String pkTableSchema  = rs.getString(PKTABLE_SCHEM);
	    String pkTableName    = rs.getString(PKTABLE_NAME);
	    String pkColumnName   = rs.getString(PKCOLUMN_NAME);
	    
	    String fkTableCatalog = rs.getString(FKTABLE_CAT);
	    String fkTableSchema  = rs.getString(FKTABLE_SCHEM);
	    String fkTableName    = rs.getString(FKTABLE_NAME);
	    String fkColumnName   = rs.getString(FKCOLUMN_NAME);
	    	    
	    short  keySequence    = rs.getShort(KEY_SEQ);

	    short  updateRule     = rs.getShort(UPDATE_RULE);
	    short  deleteRule     = rs.getShort(DELETE_RULE);
	    
	    String fkName         = rs.getString(FK_NAME);
	    String pkName         = rs.getString(PK_NAME);
	    
	    short  deferrability  = rs.getShort(DEFERRABILITY);

	    ForeignKeyColumnMetaData foreignKeyMetaData = 
	    	new ForeignKeyColumnMetaData(
	    			pkTableCatalog, pkTableSchema, pkTableName, pkColumnName,
	    			fkTableCatalog, fkTableSchema, fkTableName, fkColumnName,
	    			keySequence, updateRule, deleteRule,
	    			fkName, pkName,
	    			deferrability );
	    
	    return foreignKeyMetaData ;
	}
}
