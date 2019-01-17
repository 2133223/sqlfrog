
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

public class PostgresqlLimitConverter extends AbstractLimitConverter
{

	protected ConvertInfo convert( LimitResultSet limit,
			EDbVendor targetVendor, boolean convert ) throws ConvertException
	{
		if ( limit == null || limit.getElement( ) == null )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "Limit should not be null." );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		TLimitClause limitClause = (TLimitClause) limit.getElement( );

		if ( convert )
		{
			if ( targetVendor == EDbVendor.dbvoracle )
			{
				convertToOracle( limitClause );
			}
			else if ( targetVendor == EDbVendor.dbvmssql )
			{
				convertToSqlServer( limitClause );
			}
			else
				throw generateConvertException( limitClause, targetVendor );
		}

		return null;
	}

	private void convertToSqlServer( TLimitClause limitClause )
	{
		TTopClause topClause = new TTopClause( );
		topClause.setExpr( limitClause.getRow_count( ) );
		limitClause.getStartToken( ).stmt.setTopClause( topClause );
		TCustomSqlStatement stmt = limitClause.getStartToken( ).stmt;
		if ( stmt instanceof TSelectSqlStatement )
		{
			( (TSelectSqlStatement) stmt ).setLimitClause( null );
		}
		else if ( stmt instanceof TUpdateSqlStatement )
		{
			( (TUpdateSqlStatement) stmt ).setLimitClause( null );
		}
		else if ( stmt instanceof TDeleteSqlStatement )
		{
			( (TDeleteSqlStatement) stmt ).setLimitClause( null );
		}
		else if ( stmt instanceof TMergeSqlStatement )
		{
			( (TMergeSqlStatement) stmt ).setLimitClause( null );
		}
	}

	private void convertToOracle( TLimitClause limitClause )
	{
		TWhereClause whereClause = limitClause.getStartToken( ).stmt.getWhereClause( );
		if ( whereClause != null )
		{
			whereClause.setCondition( limitClause.getGsqlparser( )
					.parseExpression( whereClause.getCondition( )
							+ " AND ROWNUM<="
							+ limitClause.getRow_count( ).toString( ) ) );
		}
		else
		{
			whereClause = new TWhereClause( );
			whereClause.setCondition( limitClause.getGsqlparser( )
					.parseExpression( "ROWNUM<="
							+ limitClause.getRow_count( ).toString( ) ) );
			limitClause.getStartToken( ).stmt.setWhereClause( whereClause );
		}
		TCustomSqlStatement stmt = limitClause.getStartToken( ).stmt;
		if ( stmt instanceof TSelectSqlStatement )
		{
			( (TSelectSqlStatement) stmt ).setLimitClause( null );
		}
		else if ( stmt instanceof TUpdateSqlStatement )
		{
			( (TUpdateSqlStatement) stmt ).setLimitClause( null );
		}
		else if ( stmt instanceof TDeleteSqlStatement )
		{
			( (TDeleteSqlStatement) stmt ).setLimitClause( null );
		}
		else if ( stmt instanceof TMergeSqlStatement )
		{
			( (TMergeSqlStatement) stmt ).setLimitClause( null );
		}
	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		if ( targetVendor == EDbVendor.dbvoracle )
		{
			return true;
		}
		else if ( targetVendor == EDbVendor.dbvmssql )
		{
			return true;
		}
		return false;
	}

}
