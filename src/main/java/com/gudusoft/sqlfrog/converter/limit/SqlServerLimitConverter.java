
package com.gudusoft.sqlfrog.converter.limit;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.nodes.TLimitClause;
import gudusoft.gsqlparser.nodes.TTopClause;
import gudusoft.gsqlparser.nodes.TWhereClause;
import gudusoft.gsqlparser.stmt.TDeleteSqlStatement;
import gudusoft.gsqlparser.stmt.TMergeSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import gudusoft.gsqlparser.stmt.TUpdateSqlStatement;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.LimitResultSet;

public class SqlServerLimitConverter extends AbstractLimitConverter
{

	protected ConvertInfo convert( LimitResultSet limit,
			EDbVendor targetVendor, boolean convert ) throws ConvertException
	{
		if ( limit == null || limit.getElement( ) == null )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "Limit should not be null." );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		TTopClause topClause = (TTopClause) limit.getElement( );

		if ( convert )
		{
			if ( targetVendor == EDbVendor.dbvoracle )
			{
				convertToOracle( topClause );
			}
			else if ( targetVendor == EDbVendor.dbvpostgresql )
			{
				convertToPostgresql( topClause );
			}
			else
				throw generateConvertException( topClause, targetVendor );
		}

		return null;
	}

	private void convertToPostgresql( TTopClause topClause )
	{
		TLimitClause limitClause = new TLimitClause( );
		limitClause.setRow_count( topClause.getExpr( ) );

		TCustomSqlStatement stmt = topClause.getStartToken( ).stmt;
		if ( stmt instanceof TSelectSqlStatement )
		{
			( (TSelectSqlStatement) stmt ).setTopClause( null );
			( (TSelectSqlStatement) stmt ).setLimitClause( limitClause );
		}
		else if ( stmt instanceof TUpdateSqlStatement )
		{
			( (TUpdateSqlStatement) stmt ).setTopClause( null );
			( (TUpdateSqlStatement) stmt ).setLimitClause( limitClause );
		}
		else if ( stmt instanceof TDeleteSqlStatement )
		{
			( (TDeleteSqlStatement) stmt ).setTopClause( null );
			( (TDeleteSqlStatement) stmt ).setLimitClause( limitClause );
		}
		else if ( stmt instanceof TMergeSqlStatement )
		{
			( (TMergeSqlStatement) stmt ).setTopClause( null );
			( (TMergeSqlStatement) stmt ).setLimitClause( limitClause );
		}
	}

	private void convertToOracle( TTopClause topClause )
	{
		TWhereClause whereClause = topClause.getStartToken( ).stmt.getWhereClause( );
		if ( whereClause != null )
		{
			whereClause.setCondition( topClause.getGsqlparser( )
					.parseExpression( whereClause.getCondition( )
							+ " AND ROWNUM<="
							+ topClause.getExpr( ).toString( ) ) );
		}
		else
		{
			whereClause = new TWhereClause( );
			whereClause.setCondition( topClause.getGsqlparser( )
					.parseExpression( "ROWNUM<="
							+ topClause.getExpr( ).toString( ) ) );
			topClause.getStartToken( ).stmt.setWhereClause( whereClause );
		}

		TCustomSqlStatement stmt = topClause.getStartToken( ).stmt;
		if ( stmt instanceof TSelectSqlStatement )
		{
			( (TSelectSqlStatement) stmt ).setTopClause( null );
		}
		else if ( stmt instanceof TUpdateSqlStatement )
		{
			( (TUpdateSqlStatement) stmt ).setTopClause( null );
		}
		else if ( stmt instanceof TDeleteSqlStatement )
		{
			( (TDeleteSqlStatement) stmt ).setTopClause( null );
		}
		else if ( stmt instanceof TMergeSqlStatement )
		{
			( (TMergeSqlStatement) stmt ).setTopClause( null );
		}

	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		if ( targetVendor == EDbVendor.dbvoracle )
		{
			return true;
		}
		else if ( targetVendor == EDbVendor.dbvpostgresql )
		{
			return true;
		}
		return false;
	}

}
