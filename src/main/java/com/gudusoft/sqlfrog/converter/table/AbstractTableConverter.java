
package com.gudusoft.sqlfrog.converter.table;

import com.gudusoft.sqlfrog.converter.TableConverter;
import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;
import com.gudusoft.sqlfrog.model.Table;

import gudusoft.gsqlparser.EDbVendor;

public abstract class AbstractTableConverter implements TableConverter
{

	@Override
	public ConvertInfo scan( Table table, EDbVendor targetVendor )
			throws ConvertException
	{
		return convert( table, targetVendor, false );
	}

	@Override
	public void convert( Table table, EDbVendor targetVendor )
			throws ConvertException
	{
		convert( table, targetVendor, true );
	}

	protected abstract ConvertInfo convert( Table table,
			EDbVendor targetVendor, boolean convert ) throws ConvertException;
}
