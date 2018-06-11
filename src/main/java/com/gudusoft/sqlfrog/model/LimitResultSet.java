
package com.gudusoft.sqlfrog.model;

import gudusoft.gsqlparser.nodes.TLimitClause;
import gudusoft.gsqlparser.nodes.TParseTreeNode;
import gudusoft.gsqlparser.nodes.TTopClause;

public class LimitResultSet extends ConvertPoint<TParseTreeNode>
{

	public LimitResultSet( TLimitClause limit )
	{
		super( limit );
	}

	public LimitResultSet( TTopClause top )
	{
		super( top );
	}

}
