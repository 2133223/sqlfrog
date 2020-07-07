
package com.gudusoft.sqlfrog.converter.function;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TFunctionCall;

public class NVLFunctionConverter
{

	public void convert( TFunctionCall function, EDbVendor targetVendor )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				toOracle( function );
				break;
			case dbvdb2 :
				toDB2( function );
				break;
			case dbvmssql :
				toSqlServer( function );
				break;
			case dbvmysql :
				toMysql( function );
				break;
			case dbvpostgresql :
				toPostgresql( function );
				break;
			case dbvsybase :
				toSybase( function );
				break;
			case dbvansi :
				toANSI( function );
				break;
			case dbvinformix :
				toInformix( function );
				break;
			case dbvnetezza :
				toNetezza( function );
				break;
			case dbvteradata :
				toTeradata( function );
				break;
			case dbvsnowflake :
				toSnowflake( function );
				break;
			default :
		}
	}

	private void toSnowflake( TFunctionCall function )
	{
		if ( getFunctionName( function ).equals( "NVL" ) )
		{
			function.setFunctionName( function.getGsqlparser( )
					.parseObjectName( "NVL" ) );
		}
		else
		{
			function.setFunctionName( function.getGsqlparser( )
					.parseObjectName( "IFNULL" ) );
		}
		
	}

	private void toTeradata( TFunctionCall function )
	{
		function.setFunctionName( function.getGsqlparser( )
				.parseObjectName( "COALESCE" ) );
	}

	private void toNetezza( TFunctionCall function )
	{
		if ( getFunctionName( function ).equals( "NVL" ) )
		{
			function.setFunctionName( function.getGsqlparser( )
					.parseObjectName( "NVL" ) );
		}
		else
		{
			function.setFunctionName( function.getGsqlparser( )
					.parseObjectName( "COALESCE" ) );
		}
	}

	private String getFunctionName( TFunctionCall functionCall )
	{
		return functionCall.getFunctionName( ).toString( ).toUpperCase( );
	}

	private void toInformix( TFunctionCall function )
	{
		function.setFunctionName( function.getGsqlparser( )
				.parseObjectName( "NVL" ) );
	}

	private void toANSI( TFunctionCall function )
	{
		function.setFunctionName( function.getGsqlparser( )
				.parseObjectName( "COALESCE" ) );
	}

	private void toSybase( TFunctionCall function )
	{
		if ( getFunctionName( function ).equals( "NVL" ) )
		{
			function.setFunctionName( function.getGsqlparser( )
					.parseObjectName( "ISNULL" ) );
		}
		else
		{
			function.setFunctionName( function.getGsqlparser( )
					.parseObjectName( "COALESCE" ) );
		}
	}

	private void toDB2( TFunctionCall function )
	{
		function.setFunctionName( function.getGsqlparser( )
				.parseObjectName( "COALESCE" ) );
	}

	private void toPostgresql( TFunctionCall function )
	{
		function.setFunctionName( function.getGsqlparser( )
				.parseObjectName( "COALESCE" ) );
	}

	private void toMysql( TFunctionCall function )
	{
		function.setFunctionName( function.getGsqlparser( )
				.parseObjectName( "COALESCE" ) );
	}

	private void toSqlServer( TFunctionCall function )
	{
		if ( getFunctionName( function ).equals( "NVL" ) )
		{
			function.setFunctionName( function.getGsqlparser( )
					.parseObjectName( "ISNULL" ) );
		}
		else
		{
			function.setFunctionName( function.getGsqlparser( )
					.parseObjectName( "COALESCE" ) );
		}
	}

	private void toOracle( TFunctionCall function )
	{
		function.setFunctionName( function.getGsqlparser( )
				.parseObjectName( "NVL" ) );
	}
}
