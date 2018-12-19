
package com.gudusoft.sqlfrog.test.table;

import gudusoft.gsqlparser.EDbVendor;

import com.gudusoft.sqlfrog.SqlFrog;
import com.gudusoft.sqlfrog.model.FrogResult;
import com.gudusoft.sqlfrog.test.BaseTestCase;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class PostgresqlSubqueryTableConvertTest extends BaseTestCase
{

	public void testOracleTableToPostgresql( )
	{
		String sqltext = "select a, d from (select a, b, d from c)";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvpostgresql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "select t.a, t.d from (select a, b, d from c) t";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvpostgresql ),
				formatResult( convertValue, EDbVendor.dbvpostgresql ) );
	}
}
