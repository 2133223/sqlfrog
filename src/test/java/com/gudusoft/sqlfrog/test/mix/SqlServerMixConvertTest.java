
package com.gudusoft.sqlfrog.test.mix;

import gudusoft.gsqlparser.EDbVendor;

import com.gudusoft.sqlfrog.SqlFrog;
import com.gudusoft.sqlfrog.model.FrogResult;
import com.gudusoft.sqlfrog.test.BaseTestCase;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class SqlServerMixConvertTest extends BaseTestCase
{

	public void testSqlServerMixConvertToMysql( )
	{
		String sqltext = "SELECT [select].*\r\n"
				+ "FROM   table1 [select],\r\n"
				+ "       table2 t2\r\n"
				+ "WHERE  [select].f1 *= t2.f1\r\n"
				+ "       AND [select].f2 > 10;";

		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvmssql,
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

	public void testSqlServerMixConvertToOracle( )
	{
		String sqltext = "CREATE TABLE [select]\r\n"
				+ "   (ProductID int PRIMARY KEY NOT NULL,\r\n"
				+ "    ProductName varchar(25) NOT NULL,\r\n"
				+ "    Price money NULL,\r\n"
				+ "    ProductDescription text NULL);\r\n"
				+ "SELECT [select].*\r\n"
				+ "FROM   table1 [select],\r\n"
				+ "       table2 t2\r\n"
				+ "WHERE  [select].f1 *= t2.f1\r\n"
				+ "       AND [select].f2 > 10;";

		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvmssql,
				EDbVendor.dbvoracle,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE \"select\"("
				+ "productid   NUMBER (10) PRIMARY KEY NOT NULL,\r\n"
				+ "productname VARCHAR2 (25) NOT NULL,\r\n"
				+ "price       NUMBER (19,4) NULL,\r\n"
				+ "productdescription CLOB NULL);\r\n"
				+ "SELECT \"select\".*\r\n"
				+ "FROM   table1 \"select\",\r\n"
				+ "       table2 t2\r\n"
				+ "WHERE  \"select\".f1 = t2.f1(+)\r\n"
				+ "       AND \"select\".f2 > 10;";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvoracle ),
				formatResult( convertValue, EDbVendor.dbvoracle ) );
	}
}
