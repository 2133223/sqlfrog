
package com.gudusoft.sqlfrog.test.datatype;

import gudusoft.gsqlparser.EDbVendor;

import com.gudusoft.sqlfrog.SqlFrog;
import com.gudusoft.sqlfrog.model.FrogResult;
import com.gudusoft.sqlfrog.test.BaseTestCase;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class SqlServerDataTypeConvertTest extends BaseTestCase
{

	public void testSqlServerCharDataTypeToOracle( )
	{
		String sqltext = "CREATE TABLE DATA_TYPE (\r\n"
				+ "CHAR_TYPE          CHAR,\r\n"
				+ "CHAR_TYPE_1        CHAR (2000),\r\n"
				+ "CHAR_TYPE_2        CHAR (2001),\r\n"
				+ "VARCHAR2_TYPE      VARCHAR (4000),\r\n"
				+ "VARCHAR2_TYPE_1    VARCHAR (4001),\r\n"
				+ "VARCHAR2_TYPE_2    VARCHAR (MAX),\r\n"
				+ "NCHAR_TYPE         NCHAR,\r\n"
				+ "NCHAR_TYPE_1       NCHAR (2000),\r\n"
				+ "NCHAR_TYPE_2       NCHAR (2001),\r\n"
				+ "NVARCHAR2_TYPE     NVARCHAR (4000),\r\n"
				+ "NVARCHAR2_TYPE_1   NVARCHAR (4001),\r\n"
				+ "NVARCHAR2_TYPE_2   NVARCHAR (MAX),\r\n"
				+ "TEXT_TYPE          TEXT,\r\n"
				+ "NTEXT_TYPE         NTEXT);";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvmssql,
				EDbVendor.dbvoracle,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE DATA_TYPE("
				+ "CHAR_TYPE         CHAR,\r\n"
				+ "CHAR_TYPE_1       CHAR (2000),\r\n"
				+ "CHAR_TYPE_2       CLOB,\r\n"
				+ "VARCHAR2_TYPE     VARCHAR2 (4000),\r\n"
				+ "VARCHAR2_TYPE_1   CLOB,\r\n"
				+ "VARCHAR2_TYPE_2   CLOB,\r\n"
				+ "NCHAR_TYPE        NCHAR,\r\n"
				+ "NCHAR_TYPE_1      NCHAR (2000),\r\n"
				+ "NCHAR_TYPE_2      NCLOB,\r\n"
				+ "NVARCHAR2_TYPE    NVARCHAR2 (4000),\r\n"
				+ "NVARCHAR2_TYPE_1  NCLOB,\r\n"
				+ "NVARCHAR2_TYPE_2  NCLOB,\r\n"
				+ "TEXT_TYPE         CLOB,\r\n"
				+ "NTEXT_TYPE        NCLOB)";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvoracle ),
				formatResult( convertValue, EDbVendor.dbvoracle ) );
	}

	public void testSqlServerBinaryDataTypeToOracle( )
	{
		String sqltext = "CREATE TABLE DATA_TYPE (\r\n"
				+ "BINARY_TYPE        BINARY,\r\n"
				+ "BINARY_TYPE_1      BINARY (2000),\r\n"
				+ "BINARY_TYPE_2      BINARY (2001),\r\n"
				+ "VARBINARY_TYPE     VARBINARY,\r\n"
				+ "VARBINARY_TYPE_1   VARBINARY (2000),\r\n"
				+ "VARBINARY_TYPE_2   VARBINARY (2001),\r\n"
				+ "VARBINARY_TYPE_3   VARBINARY (MAX),\r\n"
				+ "IMAGE_TYPE         IMAGE);";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvmssql,
				EDbVendor.dbvoracle,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE DATA_TYPE("
				+ "BINARY_TYPE        RAW (1),\r\n"
				+ "BINARY_TYPE_1      RAW (2000),\r\n"
				+ "BINARY_TYPE_2      BLOB,\r\n"
				+ "VARBINARY_TYPE     RAW (1),\r\n"
				+ "VARBINARY_TYPE_1   RAW (2000),\r\n"
				+ "VARBINARY_TYPE_2   BLOB,\r\n"
				+ "VARBINARY_TYPE_3   BLOB,\r\n"
				+ "IMAGE_TYPE         BLOB)";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvoracle ),
				formatResult( convertValue, EDbVendor.dbvoracle ) );
	}

	public void testSqlServerNumberDataTypeToOracle( )
	{
		String sqltext = "CREATE TABLE DATA_TYPE (\r\n"
				+ "bigint_TYPE        bigint,\r\n"
				+ "int_TYPE           int,\r\n"
				+ "smallint_TYPE      smallint,\r\n"
				+ "tinyint_TYPE       tinyint,\r\n"
				+ "bit_TYPE           bit,\r\n"
				+ "numeric_TYPE_1     numeric,\r\n"
				+ "numeric_TYPE_2     numeric (10),\r\n"
				+ "numeric_TYPE_3     numeric (10,2),\r\n"
				+ "decimal_TYPE_1     decimal,\r\n"
				+ "decimal_TYPE_2     decimal (10),\r\n"
				+ "decimal_TYPE_3     decimal (10,2),\r\n"
				+ "money_TYPE         money,\r\n"
				+ "smallmoney_TYPE    smallmoney,\r\n"
				+ "float_TYPE         float,\r\n"
				+ "float_TYPE_1       float(20),\r\n"
				+ "REAL_TYPE          real);";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvmssql,
				EDbVendor.dbvoracle,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE DATA_TYPE("
				+ "BIGINT_TYPE       NUMBER (19),\r\n"
				+ "INT_TYPE          NUMBER (10),\r\n"
				+ "SMALLINT_TYPE     NUMBER (5),\r\n"
				+ "TINYINT_TYPE      NUMBER (3),\r\n"
				+ "BIT_TYPE          NUMBER (3),\r\n"
				+ "NUMERIC_TYPE_1    NUMBER,\r\n"
				+ "NUMERIC_TYPE_2    NUMBER (10),\r\n"
				+ "NUMERIC_TYPE_3    NUMBER (10,2),\r\n"
				+ "DECIMAL_TYPE_1    NUMBER,\r\n"
				+ "DECIMAL_TYPE_2    NUMBER (10),\r\n"
				+ "DECIMAL_TYPE_3    NUMBER (10,2),\r\n"
				+ "MONEY_TYPE        NUMBER (19,4),\r\n"
				+ "SMALLMONEY_TYPE   NUMBER (10,4),\r\n"
				+ "FLOAT_TYPE        FLOAT (49),\r\n"
				+ "FLOAT_TYPE_1      FLOAT (49),\r\n"
				+ "REAL_TYPE         FLOAT (23))";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvoracle ),
				formatResult( convertValue, EDbVendor.dbvoracle ) );
	}

	public void testSqlServerDateTimeDataTypeToOracle( )
	{
		String sqltext = "CREATE TABLE DATA_TYPE (\r\n"
				+ "DATE_TYPE             DATE,\r\n"
				+ "DATETIME_TYPE         DATETIME,\r\n"
				+ "DATETIME2_TYPE        DATETIME2,\r\n"
				+ "DATETIMEOFFSET_TYPE   DATETIMEOFFSET,\r\n"
				+ "SMALLDATETIME_TYPE    SMALLDATETIME,\r\n"
				+ "TIME_TYPE             TIME);";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvmssql,
				EDbVendor.dbvoracle,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "CREATE TABLE DATA_TYPE("
				+ "DATE_TYPE             DATE,\r\n"
				+ "DATETIME_TYPE         DATE,\r\n"
				+ "DATETIME2_TYPE        TIMESTAMP,\r\n"
				+ "DATETIMEOFFSET_TYPE   TIMESTAMP WITH TIME ZONE,\r\n"
				+ "SMALLDATETIME_TYPE    DATE,\r\n"
				+ "TIME_TYPE             TIMESTAMP)";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvoracle ),
				formatResult( convertValue, EDbVendor.dbvoracle ) );
	}
}
