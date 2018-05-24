
package com.gudusoft.sqlfrog.model;

import gudusoft.gsqlparser.TGSqlParser;

import java.io.File;

import com.gudusoft.sqlfrog.util.SQLUtil;

public class FrogResult
{

	private StringBuilder result = new StringBuilder( );
	private StringBuilder errorMessage = new StringBuilder( );

	public StringBuilder appendErrorMessage( TGSqlParser sqlparser,
			String errorFragment )
	{
		if ( SQLUtil.isEmpty( errorFragment ) )
			return errorMessage;
		else
			return errorMessage.append( getErrorFile( sqlparser ) )
					.append( errorFragment.trim( ) )
					.append( "\n" );
	}

	private String getErrorFile( TGSqlParser sqlparser )
	{
		if ( !SQLUtil.isEmpty( sqlparser.getSqlfilename( ) ) )
		{
			return "File: "
					+ new File( sqlparser.getSqlfilename( ) ).getName( )
					+ ", ";
		}
		return "";
	}

	public StringBuilder appendResult( String resultFragment )
	{
		if ( !SQLUtil.isEmpty( resultFragment ) )
		{
			return result.append( resultFragment.trim( ) ).append( "\n" );
		}
		else
			return result;
	}

	public String getResult( )
	{
		return result.toString( );
	}

	public String getErrorMessage( )
	{
		return errorMessage.toString( );
	}
}
