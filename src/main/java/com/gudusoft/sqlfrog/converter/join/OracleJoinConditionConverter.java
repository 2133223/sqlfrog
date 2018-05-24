
package com.gudusoft.sqlfrog.converter.join;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.nodes.TExpression;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.JoinCondition;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class OracleJoinConditionConverter extends
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
		if ( !EDbVendor.dbvoracle.equals( sourceVendor ) )
		{
			IllegalArgumentException exception = new IllegalArgumentException( "Join condition should match the oracle vendor, but "
					+ SQLUtil.getVendorName( sourceVendor ) );
			throw new ConvertException( exception.getMessage( ), exception );
		}

		ConvertInfo info = generateConvertInfo( join.getElement( ),
				targetVendor );
		boolean ansi = true;

		switch ( targetVendor )
		{
			case dbvmssql :
				if ( convert )
				{
					convertOracleJoinToSqlServer( join.getElement( ) );
				}
				ansi = false;
				break;
			default :
				break;
		}

		if ( ansi )
		{
			info.setNeedAnsiJoin( true );
			info.setInfo( "Only can convert the oracle join "
					+ " to "
					+ SQLUtil.getVendorName( targetVendor )
					+ " ansi join automatically." );
		}

		return info;
	}

	private void convertOracleJoinToSqlServer( TExpression joinCondition )
	{
		TExpression leftExpression = joinCondition.getLeftOperand( );
		TExpression rightExpression = joinCondition.getRightOperand( );
		TSourceToken operatorToken = joinCondition.getOperatorToken( );

		if ( rightExpression.isOracleOuterJoin( ) )
		{
			operatorToken.setString( "*=" );
			joinCondition.setExpressionType( EExpressionType.left_join_t );
			rightExpression.getEndToken( ).removeMyFromTokenList( );
			rightExpression.setOracleOuterJoin( false );
		}
		else if ( leftExpression.isOracleOuterJoin( ) )
		{
			operatorToken.setString( "=*" );
			joinCondition.setExpressionType( EExpressionType.right_join_t );
			leftExpression.getEndToken( ).removeMyFromTokenList( );
			leftExpression.setOracleOuterJoin( false );
		}
	}

	@Override
	public boolean enableConvert( EDbVendor targetVendor )
	{
		switch ( targetVendor )
		{
			case dbvmssql :
				return true;
			default :
				return false;
		}
	}
}
