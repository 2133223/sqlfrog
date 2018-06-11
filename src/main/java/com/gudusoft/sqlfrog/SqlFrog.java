
package com.gudusoft.sqlfrog;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TParseTreeNode;
import gudusoft.gsqlparser.scriptWriter.TScriptGenerator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gudusoft.sqlfrog.converter.ConverterFactory;
import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.converter.join.AnsiJoinConverter;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.ConvertPoint;
import com.gudusoft.sqlfrog.model.CopyingStructure;
import com.gudusoft.sqlfrog.model.DataType;
import com.gudusoft.sqlfrog.model.FrogResult;
import com.gudusoft.sqlfrog.model.Identifier;
import com.gudusoft.sqlfrog.model.JoinCondition;
import com.gudusoft.sqlfrog.model.LimitResultSet;
import com.gudusoft.sqlfrog.scanner.ScannerFactory;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class SqlFrog
{

	public FrogResult scan( File sqlFile, EDbVendor source, EDbVendor target )
	{

		TGSqlParser sqlparser = new TGSqlParser( source );
		sqlparser.sqlfilename = sqlFile.getAbsolutePath( );
		int result = sqlparser.parse( );
		if ( result != 0 )
		{
			FrogResult convertResult = new FrogResult( );
			convertResult.appendErrorMessage( sqlparser,
					sqlparser.getErrormessage( ) );
			return convertResult;
		}

		return scan( source, target, sqlparser );
	}

	public FrogResult scan( String sql, EDbVendor source, EDbVendor target )
	{

		TGSqlParser sqlparser = new TGSqlParser( source );
		sqlparser.sqltext = sql;
		int result = sqlparser.parse( );
		if ( result != 0 )
		{
			FrogResult convertResult = new FrogResult( );
			convertResult.appendErrorMessage( sqlparser,
					sqlparser.getErrormessage( ) );
			return convertResult;
		}

		return scan( source, target, sqlparser );
	}

	public FrogResult convert( File sqlFile, EDbVendor source,
			EDbVendor target, boolean ignoreConvertException )
	{

		TGSqlParser sqlparser = new TGSqlParser( source );
		sqlparser.sqlfilename = sqlFile.getAbsolutePath( );
		int result = sqlparser.parse( );
		if ( result != 0 )
		{
			FrogResult convertResult = new FrogResult( );
			convertResult.appendErrorMessage( sqlparser,
					sqlparser.getErrormessage( ) );
			return convertResult;
		}

		return convert( source, target, ignoreConvertException, sqlparser );
	}

	public FrogResult convert( String sql, EDbVendor source, EDbVendor target,
			boolean ignoreConvertException )
	{
		TGSqlParser sqlparser = new TGSqlParser( source );
		sqlparser.sqltext = sql;
		int result = sqlparser.parse( );
		if ( result != 0 )
		{
			FrogResult convertResult = new FrogResult( );
			convertResult.appendErrorMessage( sqlparser,
					sqlparser.getErrormessage( ) );
			return convertResult;
		}
		return convert( source, target, ignoreConvertException, sqlparser );
	}

	private FrogResult convert( EDbVendor source, EDbVendor target,
			boolean ignoreConvertException, TGSqlParser sqlparser )
	{
		FrogResult convertResult = new FrogResult( );
		List<ConvertPoint<? extends TParseTreeNode>> points = ScannerFactory.getScanner( )
				.scan( sqlparser );
		List<TCustomSqlStatement> convertJoinToAnsi = new ArrayList<TCustomSqlStatement>( );

		for ( ConvertPoint<? extends TParseTreeNode> point : points )
		{
			if ( point instanceof JoinCondition )
			{
				try
				{
					ConvertInfo info = ConverterFactory.getJoinConditionConverter( point.getVender( ) )
							.scan( (JoinCondition) point, target );
					if ( info.isNeedAnsiJoin( ) )
					{
						TCustomSqlStatement stmt = SQLUtil.getTopStmt( SQLUtil.getParentStmt( ( (JoinCondition) point ).getElement( ) ) );
						if ( stmt != null && !convertJoinToAnsi.contains( stmt ) )
						{
							convertJoinToAnsi.add( stmt );
						}
					}
				}
				catch ( ConvertException e )
				{
					convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
					if ( !ignoreConvertException )
					{
						return convertResult;
					}
				}
			}
		}

		if ( !convertJoinToAnsi.isEmpty( ) )
		{
			AnsiJoinConverter convert = convertToAnsiJoin( source,
					SQLUtil.getContent( sqlparser ) );
			if ( !SQLUtil.isEmpty( convert.getErrorMessage( ) ) )
			{
				convertResult.appendErrorMessage( sqlparser,
						convert.getErrorMessage( ) );
			}

			TGSqlParser newSqlparser = new TGSqlParser( source );
			newSqlparser.sqltext = convert.getQuery( );
			int result = newSqlparser.parse( );
			if ( result != 0 )
			{
				convertResult.appendErrorMessage( sqlparser,
						sqlparser.getErrormessage( ) );
				return convertResult;
			}

			points = ScannerFactory.getScanner( ).scan( newSqlparser );
			newSqlparser.sqlfilename = sqlparser.sqlfilename;
			sqlparser = newSqlparser;
		}

		for ( ConvertPoint<? extends TParseTreeNode> point : points )
		{
			if ( point instanceof DataType )
			{
				try
				{
					ConverterFactory.getDataTypeConverter( point.getVender( ) )
							.convert( (DataType) point, target );
				}
				catch ( ConvertException e )
				{
					convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
					if ( !ignoreConvertException )
					{
						return convertResult;
					}
				}
			}
			if ( point instanceof Identifier )
			{
				try
				{
					ConverterFactory.getIdentifierConverter( point.getVender( ) )
							.convert( (Identifier) point, target );
				}
				catch ( ConvertException e )
				{
					convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
					if ( !ignoreConvertException )
					{
						return convertResult;
					}
				}
			}
			if ( point instanceof JoinCondition )
			{
				try
				{
					if ( ConverterFactory.getJoinConditionConverter( point.getVender( ) )
							.enableConvert( target ) )
					{
						ConverterFactory.getJoinConditionConverter( point.getVender( ) )
								.convert( (JoinCondition) point, target );
					}
				}
				catch ( ConvertException e )
				{
					convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
					if ( !ignoreConvertException )
					{
						return convertResult;
					}
				}
			}
			if ( point instanceof LimitResultSet )
			{
				ConvertException e = new ConvertException( "F856, limiting result sets is incompatible with ANSI SQL, line:"
						+ point.getPosition( ).getX( )
						+ ", column:"
						+ point.getPosition( ).getY( )
						+ "." );
				convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
				if ( !ignoreConvertException )
				{
					return convertResult;
				}
			}
			if ( point instanceof CopyingStructure )
			{
				ConvertException e = new ConvertException( "F031-01, copying structure is incompatible with ANSI SQL, line:"
						+ point.getPosition( ).getX( )
						+ ", column:"
						+ point.getPosition( ).getY( )
						+ "." );
				convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
				if ( !ignoreConvertException )
				{
					return convertResult;
				}
			}
		}

		TScriptGenerator generator = new TScriptGenerator( );
		convertResult.appendResult( generator.generateScript( sqlparser.sqlstatements ) );
		return convertResult;
	}

	private AnsiJoinConverter convertToAnsiJoin( EDbVendor vendor, String sql )
	{
		AnsiJoinConverter convert = new AnsiJoinConverter( sql, vendor );
		convert.convert( );
		return convert;
	}

	private FrogResult scan( EDbVendor source, EDbVendor target,
			TGSqlParser sqlparser )
	{
		FrogResult convertResult = new FrogResult( );
		List<ConvertPoint<? extends TParseTreeNode>> points = ScannerFactory.getScanner( )
				.scan( sqlparser );

		for ( ConvertPoint<? extends TParseTreeNode> point : points )
		{
			if ( point instanceof DataType )
			{
				try
				{
					ConvertInfo info = ConverterFactory.getDataTypeConverter( point.getVender( ) )
							.scan( (DataType) point, target );
					if ( info.isNeedConvert( ) )
					{
						convertResult.appendResult( info.toString( ) );
					}
				}
				catch ( ConvertException e )
				{
					convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
				}
			}
			if ( point instanceof Identifier )
			{
				try
				{
					ConvertInfo info = ConverterFactory.getIdentifierConverter( point.getVender( ) )
							.scan( (Identifier) point, target );
					if ( info.isNeedConvert( ) )
					{
						convertResult.appendResult( info.toString( ) );
					}
				}
				catch ( ConvertException e )
				{
					convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
				}
			}
			if ( point instanceof JoinCondition )
			{
				try
				{
					ConvertInfo info = ConverterFactory.getJoinConditionConverter( point.getVender( ) )
							.scan( (JoinCondition) point, target );
					if ( info.isNeedConvert( ) )
					{
						convertResult.appendResult( info.toString( ) );
					}
				}
				catch ( ConvertException e )
				{
					convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
				}
			}
			if ( point instanceof LimitResultSet )
			{
				ConvertException e = new ConvertException( "F856, limiting result sets is incompatible with ANSI SQL, line:"
						+ point.getPosition( ).getX( )
						+ ", column:"
						+ point.getPosition( ).getY( )
						+ "." );
				convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
			}
			if ( point instanceof CopyingStructure )
			{
				ConvertException e = new ConvertException( "F031-01, copying structure is incompatible with ANSI SQL, line:"
						+ point.getPosition( ).getX( )
						+ ", column:"
						+ point.getPosition( ).getY( )
						+ "." );
				convertResult.appendErrorMessage( sqlparser, e.getMessage( ) );
			}
		}
		return convertResult;
	}

	public static void main( String[] args )
	{
		if ( args.length < 1 )
		{
			System.out.println( "Usage: java SqlFrog [/f <path_to_sql_file>] [/s <source database type>] [/t <target database type>] [/o <output file path>] [/scan] [/log]" );
			System.out.println( "/f: specify the sql file path to analyze sql convert." );
			System.out.println( "/s: set the source database type. Support oracle, mysql, mssql, db2, netezza, teradata, informix, sybase, postgresql, hive, greenplum and redshift." );
			System.out.println( "/t: set the target database type. Support oracle, mysql, mssql, db2, netezza, teradata, informix, sybase, postgresql, hive, greenplum and redshift." );
			System.out.println( "/o: Option, write the output stream to the specified file." );
			System.out.println( "/scan: Option, only scan the sql convert points." );
			System.out.println( "/log: Option, generate a convert.log file to log information." );
			return;
		}

		List<String> argList = Arrays.asList( args );

		File sqlFile = null;

		if ( argList.indexOf( "/f" ) != -1
				&& argList.size( ) > argList.indexOf( "/f" ) + 1 )
		{
			sqlFile = new File( args[argList.indexOf( "/f" ) + 1] );
			if ( !sqlFile.exists( ) || !sqlFile.isFile( ) )
			{
				System.out.println( sqlFile + " is not a valid file." );
				return;
			}
		}
		else
		{
			System.out.println( "Please specify a sql file path to analyze sql convert." );
			return;
		}

		EDbVendor sourceVendor = null;
		int index = argList.indexOf( "/s" );
		if ( index != -1 && args.length > index + 1 )
		{
			String venderValue = args[index + 1];
			sourceVendor = SQLUtil.getVendor( venderValue );
		}

		if ( sourceVendor == null )
		{
			System.out.println( "Please specify an available source database type." );
			return;
		}

		EDbVendor targetVendor = null;
		index = argList.indexOf( "/t" );
		if ( index != -1 && args.length > index + 1 )
		{
			String venderValue = args[index + 1];
			targetVendor = SQLUtil.getVendor( venderValue );
		}

		if ( targetVendor == null )
		{
			System.out.println( "Please specify an available target database type." );
			return;
		}

		String outputFile = null;

		index = argList.indexOf( "/o" );

		if ( index != -1 && args.length > index + 1 )
		{
			outputFile = args[index + 1];
		}

		FileOutputStream writer = null;
		if ( outputFile != null )
		{
			try
			{
				writer = new FileOutputStream( outputFile );
				System.setOut( new PrintStream( writer ) );
			}
			catch ( FileNotFoundException e )
			{
				e.printStackTrace( );
			}
		}

		SqlFrog frog = new SqlFrog( );

		boolean scan = argList.indexOf( "/scan" ) != -1;
		FrogResult result;

		if ( scan )
		{
			result = frog.scan( sqlFile, sourceVendor, targetVendor );
		}
		else
		{
			result = frog.convert( sqlFile, sourceVendor, targetVendor, true );
		}

		if ( scan )
		{
			if ( !SQLUtil.isEmpty( result.getErrorMessage( ) ) )
			{
				System.out.println( result.getErrorMessage( ) );
			}
			if ( !SQLUtil.isEmpty( result.getResult( ) ) )
			{
				System.out.println( result.getResult( ) );
			}
		}
		else
		{
			if ( !SQLUtil.isEmpty( result.getResult( ) ) )
			{
				System.out.println( result.getResult( ) );
			}
			if ( !SQLUtil.isEmpty( result.getErrorMessage( ) ) )
			{
				System.err.println( result.getErrorMessage( ) );
			}
		}

		try
		{
			if ( writer != null )
			{
				writer.close( );
			}
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}

		boolean log = argList.indexOf( "/log" ) != -1;
		PrintStream pw = null;
		ByteArrayOutputStream sw = null;
		PrintStream systemSteam = System.err;

		try
		{
			sw = new ByteArrayOutputStream( );
			pw = new PrintStream( sw );
			System.setErr( pw );
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
		}

		if ( !SQLUtil.isEmpty( result.getErrorMessage( ) ) )
		{
			System.err.println( "Error log:\n" + result.getErrorMessage( ) );
		}

		if ( sw != null )
		{
			String errorMessage = sw.toString( ).trim( );
			if ( errorMessage.length( ) > 0 )
			{
				if ( log )
				{
					try
					{
						pw = new PrintStream( new File( ".", "convert.log" ) );
						pw.print( errorMessage );
					}
					catch ( FileNotFoundException e )
					{
						e.printStackTrace( );
					}
				}

				System.setErr( systemSteam );
				System.err.println( errorMessage );
			}
		}
	}

}
