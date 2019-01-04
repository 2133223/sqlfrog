package com.gudusoft.sqlfrog.converter.function;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.EFunctionType;
import gudusoft.gsqlparser.nodes.TFunctionCall;

public class TruncFunctionConverter {

	public void convert(TFunctionCall function, EDbVendor targetVendor) {
		switch (targetVendor) {
		case dbvoracle:
			toOracle(function);
			break;
		case dbvmssql:
			toSqlServer(function);
			break;
		case dbvmysql:
			toMysql(function);
			break;
		case dbvpostgresql:
			toPostgresql(function);
			break;
		default:
		}
	}

	private void toPostgresql(TFunctionCall function) {
		function.setFunctionName(function.getGsqlparser().parseObjectName("trunc"));
	}

	private void toMysql(TFunctionCall function) {
		function.setFunctionName(function.getGsqlparser().parseObjectName("truncate"));
	}

	private void toSqlServer(TFunctionCall function) {
		function.setFunctionName(function.getGsqlparser().parseObjectName("floor"));
		function.setFunctionType(EFunctionType.unknown_t);
	}

	private void toOracle(TFunctionCall function) {
		function.setFunctionName(function.getGsqlparser().parseObjectName("trunc"));
	}
}
