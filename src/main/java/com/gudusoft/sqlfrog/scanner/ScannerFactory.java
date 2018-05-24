
package com.gudusoft.sqlfrog.scanner;

public class ScannerFactory
{

	public static Scanner getScanner( )
	{
		return new CommonScanner( );
	}
}
