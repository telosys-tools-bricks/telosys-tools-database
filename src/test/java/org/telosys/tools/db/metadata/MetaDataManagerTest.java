package org.telosys.tools.db.metadata;

import java.util.regex.Pattern;

import junit.framework.Assert;
import junit.framework.TestCase;


public class MetaDataManagerTest extends TestCase {
	
	public void testIsExcludedTable_patterns_null() {
		// Given
		MetaDataManager m = new MetaDataManager(null);
		
		String tableName = "abcde";
		Pattern patternTableNameExclude = null;
		Pattern patternTableNameInclude = null;
		
		// When
		boolean isExcluded = m.isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
		
		// Then
		Assert.assertFalse(isExcluded);
	}

	public void testIsExcludedTable_not_excluded() {
		// Given
		MetaDataManager m = new MetaDataManager(null);
		
		String tableName = "abcde";
		Pattern patternTableNameExclude = Pattern.compile("test");
		Pattern patternTableNameInclude = null;
		
		// When
		boolean isExcluded = m.isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
		
		// Then
		Assert.assertFalse(isExcluded);
	}

	public void testIsExcludedTable_excluded() {
		// Given
		MetaDataManager m = new MetaDataManager(null);
		
		String tableName = "abcde";
		Pattern patternTableNameExclude = Pattern.compile("a.*");
		Pattern patternTableNameInclude = null;
		
		// When
		boolean isExcluded = m.isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
		
		// Then
		Assert.assertTrue(isExcluded);
	}

	public void testIsExcludedTable_not_included() {
		// Given
		MetaDataManager m = new MetaDataManager(null);
		
		String tableName = "abcde";
		Pattern patternTableNameExclude = null;
		Pattern patternTableNameInclude = Pattern.compile("test");
		
		// When
		boolean isExcluded = m.isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
		
		// Then
		Assert.assertTrue(isExcluded);
	}

	public void testIsExcludedTable_included() {
		// Given
		MetaDataManager m = new MetaDataManager(null);
		
		String tableName = "abcde";
		Pattern patternTableNameExclude = null;
		Pattern patternTableNameInclude = Pattern.compile("a.*");
		
		// When
		boolean isExcluded = m.isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
		
		// Then
		Assert.assertFalse(isExcluded);
	}

	public void testIsExcludedTable_excluded_and_included() {
		// Given
		MetaDataManager m = new MetaDataManager(null);
		
		String tableName = "abcde";
		Pattern patternTableNameExclude = Pattern.compile("a.*");
		Pattern patternTableNameInclude = Pattern.compile("a.*");
		
		// When
		boolean isExcluded = m.isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
		
		// Then
		Assert.assertTrue(isExcluded);
	}

	public void testIsExcludedTable_excluded_and_not_included() {
		// Given
		MetaDataManager m = new MetaDataManager(null);
		
		String tableName = "abcde";
		Pattern patternTableNameExclude = Pattern.compile("a.*");
		Pattern patternTableNameInclude = Pattern.compile("test");
		
		// When
		boolean isExcluded = m.isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
		
		// Then
		Assert.assertTrue(isExcluded);
	}

	public void testIsExcludedTable_not_excluded_and_included() {
		// Given
		MetaDataManager m = new MetaDataManager(null);
		
		String tableName = "abcde";
		Pattern patternTableNameExclude = Pattern.compile("test");
		Pattern patternTableNameInclude = Pattern.compile("a.*");
		
		// When
		boolean isExcluded = m.isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
		
		// Then
		Assert.assertFalse(isExcluded);
	}

	public void testIsExcludedTable_not_excluded_and_not_included() {
		// Given
		MetaDataManager m = new MetaDataManager(null);
		
		String tableName = "abcde";
		Pattern patternTableNameExclude = Pattern.compile("test");
		Pattern patternTableNameInclude = Pattern.compile("test");
		
		// When
		boolean isExcluded = m.isExcludedTable(tableName, patternTableNameInclude, patternTableNameExclude);
		
		// Then
		Assert.assertTrue(isExcluded);
	}
	
}
