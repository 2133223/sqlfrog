package com.gudusoft.sqlfrog.converter.function;

import com.gudusoft.sqlfrog.util.SourceTokenSearcher;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.EFunctionType;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TExpressionList;
import gudusoft.gsqlparser.nodes.TFunctionCall;
import gudusoft.gsqlparser.nodes.TParseTreeNodeList;

public class ExtractFunctionConverter {

	public void convert(TFunctionCall function, EDbVendor targetVendor) {
		switch (targetVendor) {
		case dbvmssql:
			toSqlServer(function);
			break;
		case dbvoracle:
			toOracle(function);
			break;
		default:
		}
	}

	private void toOracle(TFunctionCall function) {
		if (function.getExtract_time_token().astext.toUpperCase().equals("EPOCH")) {
			TExpression expression = getExpression(function);
			expression.setLeftOperand(function.getGsqlparser().parseExpression("(to_date("
					+ function.getExpr1().toScript() + ") - to_date('19700101', 'YYYYMMDD'))"));
			expression.setExpressionType(EExpressionType.arithmetic_times_t);
			expression.setRightOperand(function.getGsqlparser().parseExpression("86400"));
		}
	}

	private TExpression getExpression(TFunctionCall function) {
		TParseTreeNodeList list = function.getStartToken().getNodesStartFromThisToken();
		for (int i = 0; i < list.size(); i++) {
			if (list.getElement(i) instanceof TExpression) {
				return (TExpression) list.getElement(i);
			}
		}

		return null;
	}

	private void toSqlServer(TFunctionCall function) {
		function.setFunctionName(function.getGsqlparser().parseObjectName("datepart"));
		function.setFunctionType(EFunctionType.unknown_t);
		SourceTokenSearcher.forwardSearch(function.getExtract_time_token(), 5, "from").astext = ",";
		TExpressionList exprs = new TExpressionList();
		exprs.addExpression(function.getGsqlparser().parseExpression(function.getExtract_time_token().astext));
		exprs.addExpression(function.getExpr1());
		function.setArgs(exprs);
	}

}
