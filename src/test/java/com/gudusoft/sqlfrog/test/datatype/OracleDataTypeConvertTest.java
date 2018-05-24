
package com.gudusoft.sqlfrog.test.datatype;

import gudusoft.gsqlparser.EDbVendor;

import com.gudusoft.sqlfrog.SqlFrog;
import com.gudusoft.sqlfrog.model.FrogResult;
import com.gudusoft.sqlfrog.test.BaseTestCase;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class OracleDataTypeConvertTest extends BaseTestCase
{

	public void testOracleCharDataTypeToMssql( )
	{
		String sqltext = "CREATE TABLE DATA_TYPE (\r\n"
				+ "CHAR_TYPE      CHAR,\r\n"
				+ "CHAR_TYPE_1    CHAR (2000),\r\n"
				+ "VARCHAR2_TYPE  VARCHAR2 (4000),\r\n"
				+ "NCHAR_TYPE     NCHAR,\r\n"
				+ "NCHAR_TYPE_1   NCHAR (2000),\r\n"
				+ "NVARCHAR2_TYPE NVARCHAR2 (4000));";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvmssql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE DATA_TYPE("
				+ "CHAR_TYPE      CHAR,\r\n"
				+ "CHAR_TYPE_1    CHAR (2000),\r\n"
				+ "VARCHAR2_TYPE  VARCHAR (4000),\r\n"
				+ "NCHAR_TYPE     NVARCHAR,\r\n"
				+ "NCHAR_TYPE_1   NVARCHAR (2000),\r\n"
				+ "NVARCHAR2_TYPE NVARCHAR (4000))";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvmssql ),
				formatResult( convertValue, EDbVendor.dbvmssql ) );
	}

	public void testOracleBulitInDataTypeToMssql( )
	{
		String sqltext = "CREATE TABLE DATA_TYPE (\r\n"
				+ "LONG_TYPE      LONG,\r\n"
				+ "LONG_RAW_TYPE  LONG RAW,\r\n"
				+ "RAW_TYPE       RAW (2000),\r\n"
				+ "CLOB_TYPE      CLOB,\r\n"
				+ "NCLOB_TYPE     NCLOB,\r\n"
				+ "BLOB_TYPE      BLOB,\r\n"
				+ "BFILE_TYPE     BFILE);";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvmssql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE DATA_TYPE("
				+ "LONG_TYPE      VARCHAR (MAX),\r\n"
				+ "LONG_RAW_TYPE  IMAGE,\r\n"
				+ "RAW_TYPE       VARBINARY (2000),\r\n"
				+ "CLOB_TYPE      VARCHAR (MAX),\r\n"
				+ "NCLOB_TYPE     TEXT,\r\n"
				+ "BLOB_TYPE      IMAGE,\r\n"
				+ "BFILE_TYPE     VARBINARY (MAX))";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvmssql ),
				formatResult( convertValue, EDbVendor.dbvmssql ) );
	}

	public void testOracleNumberDataTypeToMssql( )
	{
		String sqltext = "CREATE TABLE DATA_TYPE (\r\n"
				+ "NUMBER_TYPE         NUMBER,\r\n"
				+ "NUMBER_TYPE_1       NUMBER (10),\r\n"
				+ "NUMBER_TYPE_2       NUMBER (10,2),\r\n"
				+ "NUMBER_TYPE_3       NUMBER (2,5),\r\n"
				+ "FLOAT_TYPE          FLOAT,\r\n"
				+ "FLOAT_TYPE_1        FLOAT (53),\r\n"
				+ "FLOAT_TYPE_2        FLOAT (54),\r\n"
				+ "BINARY_FLOAT_TYPE   BINARY_FLOAT,\r\n"
				+ "BINARY_DOUBLE_TYPE  BINARY_DOUBLE);";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvmssql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE DATA_TYPE("
				+ "NUMBER_TYPE         FLOAT,\r\n"
				+ "NUMBER_TYPE_1       NUMERIC (10),\r\n"
				+ "NUMBER_TYPE_2       NUMERIC (10,2),\r\n"
				+ "NUMBER_TYPE_3       NUMERIC (5,5),\r\n"
				+ "FLOAT_TYPE          FLOAT,\r\n"
				+ "FLOAT_TYPE_1        FLOAT (53),\r\n"
				+ "FLOAT_TYPE_2        FLOAT,\r\n"
				+ "BINARY_FLOAT_TYPE   FLOAT (24),\r\n"
				+ "BINARY_DOUBLE_TYPE  FLOAT (53))";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvmssql ),
				formatResult( convertValue, EDbVendor.dbvmssql ) );
	}

	public void testOracleDateTimeDataTypeToMssql( )
	{
		String sqltext = "CREATE TABLE DATA_TYPE (\r\n"
				+ "INTERVAL_TYPE_1     INTERVAL YEAR TO MONTH,\r\n"
				+ "INTERVAL_TYPE_2     INTERVAL DAY TO SECOND);";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvmssql,
				true );
		assertFalse( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		sqltext = "CREATE TABLE DATA_TYPE (\r\n"
				+ "DATE_TYPE           DATE,\r\n"
				+ "TIMESTAMP_TYPE      TIMESTAMP(9),\r\n"
				+ "TIMESTAMP_TYPE_1    TIMESTAMP(9) WITH TIME ZONE,\r\n"
				+ "TIMESTAMP_TYPE_2    TIMESTAMP(9) WITH LOCAL TIME ZONE);";
		result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvmssql,
				true );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE DATA_TYPE("
				+ "DATE_TYPE          DATETIME,\r\n"
				+ "TIMESTAMP_TYPE     DATETIME,\r\n"
				+ "TIMESTAMP_TYPE_1   VARCHAR (37),\r\n"
				+ "TIMESTAMP_TYPE_2   VARCHAR (37))";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvmssql ),
				formatResult( convertValue, EDbVendor.dbvmssql ) );
	}
}
