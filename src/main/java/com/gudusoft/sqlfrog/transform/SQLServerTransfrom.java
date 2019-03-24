
package com.gudusoft.sqlfrog.transform;

import gudusoft.gsqlparser.nodes.TAliasClause;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TObjectName;
import gudusoft.gsqlparser.nodes.TParseTreeNode;
import gudusoft.gsqlparser.nodes.TResultColumn;

import com.gudusoft.sqlfrog.util.ObjectNameUtils;

public class SQLServerTransfrom implements Transfrom
{

	public static enum TransformType {
		proprietary_column_alias, clean_variable_name
	}

	private TransformType type;

	private TParseTreeNode node;

	public SQLServerTransfrom( TParseTreeNode node, TransformType type )
	{
		this.node = node;
		this.type = type;
	}

	public void transfrom( )
	{
		if ( type == TransformType.proprietary_column_alias )
		{
			transfromColumnAlias( );
		}
		else if ( type == TransformType.clean_variable_name )
		{
			cleanVariableName( );
		}
	}

	private void cleanVariableName( )
	{
		TObjectName name = (TObjectName) node;
		name.getPartToken( ).astext = ObjectNameUtils.cleanVariableName( name );
	}

	private void transfromColumnAlias( )
	{
		TResultColumn column = (TResultColumn) node;
		TExpression expr = column.getExpr( );
		column.setExpr( expr.getGsqlparser( )
				.parseExpression( expr.getRightOperand( ).toScript( ) ) );
		TAliasClause alias = new TAliasClause( );
		alias.setAliasName( expr.getGsqlparser( )
				.parseObjectName( expr.getLeftOperand( ).toScript( ) ) );
		column.setAliasClause( alias );
	}
}
