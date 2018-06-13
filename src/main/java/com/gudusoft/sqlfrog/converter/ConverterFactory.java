
package com.gudusoft.sqlfrog.converter;

import gudusoft.gsqlparser.EDbVendor;

import com.gudusoft.sqlfrog.converter.datatype.AnsiDataTypeConverter;
import com.gudusoft.sqlfrog.converter.datatype.OracleDataTypeConverter;
import com.gudusoft.sqlfrog.converter.datatype.SqlServerDataTypeConverter;
import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.converter.function.CommonFunctionConverter;
import com.gudusoft.sqlfrog.converter.identifier.CommonIdentifierConverter;
import com.gudusoft.sqlfrog.converter.identifier.MysqlIdentifierConverter;
import com.gudusoft.sqlfrog.converter.identifier.SqlServerIdentifierConverter;
import com.gudusoft.sqlfrog.converter.join.OracleJoinConditionConverter;
import com.gudusoft.sqlfrog.converter.join.SqlServerJoinConditionConverter;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class ConverterFactory
{

	public static DataTypeConverter getDataTypeConverter( EDbVendor vender )
	{
		switch ( vender )
		{
			case dbvoracle :
				return new OracleDataTypeConverter( );
			case dbvmssql :
				return new SqlServerDataTypeConverter( );
			case dbvansi :
				return new AnsiDataTypeConverter( );
			default :
				throw new ConvertException( "Doesn't support the "
						+ SQLUtil.getVendorName( vender )
						+ " data type converter!" );
		}
	}

	public static JoinConditionConverter getJoinConditionConverter(
			EDbVendor vender )
	{
		switch ( vender )
		{
			case dbvoracle :
				return new OracleJoinConditionConverter( );
			case dbvmssql :
				return new SqlServerJoinConditionConverter( );
			default :
				throw new ConvertException( "Can't support the "
						+ SQLUtil.getVendorName( vender )
						+ " join condition converter!" );
		}
	}

	public static IdentifierConverter getIdentifierConverter( EDbVendor vender )
	{
		switch ( vender )
		{
			case dbvmssql :
				return new SqlServerIdentifierConverter( );
			case dbvmysql :
				return new MysqlIdentifierConverter( );
			default :
				return new CommonIdentifierConverter( );
		}
	}

	public static FunctionConverter getFunctionConverter( EDbVendor vender )
	{
		switch ( vender )
		{
			default :
				return new CommonFunctionConverter( );
		}
	}
}
