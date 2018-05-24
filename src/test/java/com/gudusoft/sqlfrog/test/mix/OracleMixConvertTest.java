
package com.gudusoft.sqlfrog.test.mix;

import gudusoft.gsqlparser.EDbVendor;

import com.gudusoft.sqlfrog.SqlFrog;
import com.gudusoft.sqlfrog.model.FrogResult;
import com.gudusoft.sqlfrog.test.BaseTestCase;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class OracleMixConvertTest extends BaseTestCase
{

	public void testOracleMixConvertToMysql( )
	{
		String sqltext = "SELECT \"select\".*\r\n"
				+ "FROM   table1 \"select\",\r\n"
				+ "       table2 t2\r\n"
				+ "WHERE  \"select\".f1 = t2.f1(+)\r\n"
				+ "       AND \"select\".f2 > 10";

		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvmysql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "SELECT `select`.*\r\n"
				+ "FROM   table1 `select`\r\n"
				+ "       LEFT OUTER JOIN table2 t2\r\n"
				+ "       ON `select`.f1 = t2.f1\r\n"
				+ "WHERE  `select`.f2 > 10";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvmysql ),
				formatResult( convertValue, EDbVendor.dbvmysql ) );
	}

	public void testOracleMixConvertToSqlServer( )
	{
		String sqltext = "create table \"select\" (\r\n"
				+ "order_id  number primary key,\r\n"
				+ "order_dt  date,\r\n"
				+ "cust_id   varchar2(10));\r\n"
				+ "SELECT \"select\".*\r\n"
				+ "FROM   table1 \"select\",\r\n"
				+ "       table2 t2\r\n"
				+ "WHERE  \"select\".f1 = t2.f1(+)\r\n"
				+ "       AND \"select\".f2 > 10";

		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvmssql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE [select]("
				+ "order_id FLOAT PRIMARY KEY,\r\n"
				+ "order_dt DATETIME,\r\n"
				+ "cust_id  VARCHAR (10));\r\n"
				+ "SELECT [select].*\r\n"
				+ "FROM   table1 [select],\r\n"
				+ "       table2 t2\r\n"
				+ "WHERE  [select].f1 *= t2.f1\r\n"
				+ "       AND [select].f2 > 10;";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvmssql ),
				formatResult( convertValue, EDbVendor.dbvmssql ) );
	}
}
