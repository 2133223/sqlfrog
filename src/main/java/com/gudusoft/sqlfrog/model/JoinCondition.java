
package com.gudusoft.sqlfrog.model;

import gudusoft.gsqlparser.nodes.TExpression;

public class JoinCondition extends ConvertPoint<TExpression>
{

	public JoinCondition( TExpression joinExpr )
	{
		super( joinExpr );
	}
}
