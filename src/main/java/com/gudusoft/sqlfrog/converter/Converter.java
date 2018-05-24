
package com.gudusoft.sqlfrog.converter;

import com.gudusoft.sqlfrog.converter.exception.ConvertException;
import com.gudusoft.sqlfrog.model.ConvertInfo;

import gudusoft.gsqlparser.EDbVendor;

public interface Converter<T>
{

	ConvertInfo scan( T node, EDbVendor targetVendor ) throws ConvertException;

	void convert( T node, EDbVendor targetVendor ) throws ConvertException;

	boolean enableConvert( EDbVendor targetVendor );
}
