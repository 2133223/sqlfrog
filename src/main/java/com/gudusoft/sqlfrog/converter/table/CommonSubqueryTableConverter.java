
package com.gudusoft.sqlfrog.converter.table;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.Table;
import com.gudusoft.sqlfrog.model.Tuple;
import com.gudusoft.sqlfrog.util.SQLUtil;

import gudusoft.gsqlparser.EDbObjectType;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TSourceToken;
import gudusoft.gsqlparser.nodes.TAliasClause;
import gudusoft.gsqlparser.nodes.TObjectName;
import gudusoft.gsqlparser.nodes.TTable;

public class CommonSubqueryTableConverter extends AbstractTableConverter {

	protected ConvertInfo convert(Table table, EDbVendor targetVendor, boolean convert) throws ConvertException {
		if (table == null || table.getElement() == null) {
			IllegalArgumentException exception = new IllegalArgumentException("Table should not be null.");
			throw new ConvertException(exception.getMessage(), exception);
		}

		TTable tableElement = table.getElement();

		if (!convert) {
			if (tableElement.getAliasClause() == null && targetVendor == EDbVendor.dbvpostgresql) {
				return handleSubqueryTableAliasConvertPoint(tableElement,
						"Postgresql subquery in FROM must have an alias.");
			}
		} else {
			if (tableElement.getAliasClause() == null && targetVendor == EDbVendor.dbvpostgresql) {

				TAliasClause alias = new TAliasClause();
				TObjectName aliasName = new TObjectName(new TSourceToken("t"), EDbObjectType.table_alias);
				alias.setAliasName(aliasName);
				tableElement.setAliasClause(alias);
				if (tableElement.getLinkedColumns() != null) {
					for (int i = 0; i < tableElement.getLinkedColumns().size(); i++) {
						TObjectName column = tableElement.getLinkedColumns().getObjectName(i);
						column.getColumnToken().astext = aliasName.toString() + "." + column.getColumnToken().astext;
					}
				}
			}
		}

		return null;
	}

	private ConvertInfo handleSubqueryTableAliasConvertPoint(TTable table, String message) {
		ConvertInfo info = new ConvertInfo();
		info.setInfo(message);
		info.setPosition(new Tuple<Long>(table.getLineNo(), table.getColumnNo()));
		String filePath = table.getStartToken().container.getGsqlparser().getSqlfilename();
		if (!SQLUtil.isEmpty(filePath)) {
			info.setFilePath(filePath);
		}
		return info;
	}

	@Override
	public boolean enableConvert(EDbVendor targetVendor) {
		return true;
	}

}
