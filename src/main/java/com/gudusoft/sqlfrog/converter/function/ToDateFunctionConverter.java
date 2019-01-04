package com.gudusoft.sqlfrog.converter.function;

import gudusoft.gsqlparser.EDataType;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.EFunctionType;
import gudusoft.gsqlparser.nodes.TFunctionCall;
import gudusoft.gsqlparser.nodes.TTypeName;

public class ToDateFunctionConverter {

	public void convert(TFunctionCall function, EDbVendor targetVendor) {
		switch (targetVendor) {
		case dbvmssql:
			toSqlServer(function);
			break;
		default:
		}
	}

	private void toSqlServer(TFunctionCall function) {
		function.setFunctionName(function.getGsqlparser().parseObjectName("convert"));
		function.setFunctionType(EFunctionType.convert_t);
		function.setTypename(new TTypeName(EDataType.datetime_t));
		function.setParameter(function.getArgs().getExpression(0));
	}

}
