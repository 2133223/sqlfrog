
package com.gudusoft.sqlfrog.model;

import gudusoft.gsqlparser.nodes.TParseTreeNode;
import gudusoft.gsqlparser.stmt.TCreateTableSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

public class CopyingStructure extends ConvertPoint<TParseTreeNode>
{

	public CopyingStructure( TSelectSqlStatement selectWithIntoClause )
	{
		super( selectWithIntoClause );
	}

	public CopyingStructure( TCreateTableSqlStatement createTableWithSelect )
	{
		super( createTableWithSelect );
	}

}
