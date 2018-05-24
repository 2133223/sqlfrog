
package com.gudusoft.sqlfrog.scanner;

import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TParseTreeNode;

import java.util.List;

import com.gudusoft.sqlfrog.model.ConvertPoint;

public interface Scanner
{

	List<ConvertPoint<? extends TParseTreeNode>> scan( TGSqlParser parser );

}
