
package com.gudusoft.sqlfrog.test;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.pp.para.GFmtOptFactory;
import gudusoft.gsqlparser.pp.stmtformatter.SqlFormatter;
import junit.framework.TestCase;

public abstract class BaseTestCase extends TestCase
{

	protected String formatResult( String sql, EDbVendor vendor )
	{
		final TGSqlParser sqlparser = new TGSqlParser( vendor );
		sqlparser.sqltext = sql;
		sqlparser.parse( );
		String convertValue = new SqlFormatter( ).format( sqlparser,
				GFmtOptFactory.newInstance( ) ).trim( );
		return convertValue;
	}
}
