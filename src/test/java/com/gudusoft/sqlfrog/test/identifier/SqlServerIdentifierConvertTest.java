
package com.gudusoft.sqlfrog.test.identifier;

import gudusoft.gsqlparser.EDbVendor;

import com.gudusoft.sqlfrog.SqlFrog;
import com.gudusoft.sqlfrog.model.FrogResult;
import com.gudusoft.sqlfrog.test.BaseTestCase;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class SqlServerIdentifierConvertTest extends BaseTestCase
{

	public void testSqlServerIdentifierToMysql( )
	{
		String sqltext = "SELECT * FROM [select] WHERE [select].id > 100;";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvmssql,
				EDbVendor.dbvmysql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "SELECT * FROM `select` WHERE `select`.id > 100";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvmssql ),
				formatResult( convertValue, EDbVendor.dbvmssql ) );
	}

	public void testMysqlIdentifierToOracle( )
	{
		String sqltext = "SELECT * FROM [select] WHERE [select].id > 100;";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvmssql,
				EDbVendor.dbvoracle,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "SELECT * FROM \"select\" WHERE \"select\".id > 100";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvoracle ),
				formatResult( convertValue, EDbVendor.dbvoracle ) );
	}
}
