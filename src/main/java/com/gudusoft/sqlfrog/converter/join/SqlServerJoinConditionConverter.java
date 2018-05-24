
package com.gudusoft.sqlfrog.converter.join;

import gudusoft.gsqlparser.EComparisonType;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.nodes.TExpression;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.JoinCondition;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class SqlServerJoinConditionConverter extends
		AbstractJoinConditionConverter
{

	protected ConvertInfo convert( JoinCondition join, EDbVendor targetVendor,
			boolean convert ) throws ConvertException
	{
		if ( join == null || join.getElement( ) == null )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "Join condition should not be null." );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		EDbVendor sourceVendor = join.getElement( )
				.getGsqlparser( )
				.getDbVendor( );
		if ( !EDbVendor.dbvmssql.equals( sourceVendor ) )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "Join condition should match the mssql vendor, but "
					+ SQLUtil.getVendorName( sourceVendor ) );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		ConvertInfo info = generateConvertInfo( join.getElement( ),
				targetVendor );
		boolean ansi = true;

		switch ( targetVendor )
		{
			case dbvoracle :
				if ( convert )
				{
					convertSqlServerJoinToOracle( join.getElement( ) );
				}
				ansi = false;
				break;
			default :
				break;
		}

		if ( ansi )
		{
			info.setNeedAnsiJoin( true );
			info.setInfo( "Only can convert the sql server join "
					+ " to "
					+ SQLUtil.getVendorName( targetVendor )
					+ " ansi join automatically." );
		}

		return info;
	}

	private void convertSqlServerJoinToOracle( TExpression joinCondition )
	{
		TExpression leftExpression = joinCondition.getLeftOperand( );
		TExpression rightExpression = joinCondition.getRightOperand( );
		TSourceToken operatorToken = joinCondition.getOperatorToken( );

		if ( joinCondition.getExpressionType( ) == EExpressionType.left_join_t )
		{
			rightExpression.setOracleOuterJoin( true );
		}
		else if ( joinCondition.getExpressionType( ) == EExpressionType.right_join_t )
		{
			leftExpression.setOracleOuterJoin( true );
		}

		operatorToken.setString( "=" );
		joinCondition.setExpressionType( EExpressionType.simple_comparison_t );
		joinCondition.setComparisonType( EComparisonType.equals );
	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		switch ( targetVendor )
		{
			case dbvoracle :
				return true;
			default :
				return false;
		}
	}

}
