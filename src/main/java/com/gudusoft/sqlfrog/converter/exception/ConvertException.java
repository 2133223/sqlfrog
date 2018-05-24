
package com.gudusoft.sqlfrog.converter.exception;

public class ConvertException extends UnsupportedOperationException
{

	private static final long serialVersionUID = -1045102649340341241L;

	public ConvertException( )
	{
		super( );
	}

	public ConvertException( String message )
	{
		super( message );
	}

	public ConvertException( String message, Throwable e )
	{
		super( message, e );
	}

}
