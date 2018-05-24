
package com.gudusoft.sqlfrog.model;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.nodes.TParseTreeNode;

public class ConvertPoint<T extends TParseTreeNode>
{

	private T element;

	public ConvertPoint( T element )
	{
		this.element = element;
	}

	public Tuple<Long> getPosition( )
	{
		if ( element == null )
			return null;
		return new Tuple<Long>( element.getLineNo( ), element.getColumnNo( ) );
	}

	public T getElement( )
	{
		return element;
	}

	public EDbVendor getVender( )
	{
		if ( element == null )
			return null;
		if ( element.getGsqlparser( ) != null )
			return element.getGsqlparser( ).getDbVendor( );
		if ( element.getStartToken( ) != null )
		{
			return element.getStartToken( ).getDbvendor( );
		}
		return null;
	}
}
