
package com.gudusoft.sqlfrog.model;

import java.io.File;

import com.gudusoft.sqlfrog.util.SQLUtil;

public class ConvertInfo
{

	private boolean needConvert = true;

	private boolean needAnsiJoin = false;

	private String info;

	private Tuple<Long> position;

	private String filePath;

	public String getInfo( )
	{
		return info;
	}

	public void setInfo( String info )
	{
		this.info = info;
	}

	public Tuple<Long> getPosition( )
	{
		return position;
	}

	public void setPosition( Tuple<Long> position )
	{
		this.position = position;
	}

	public String getFilePath( )
	{
		return filePath;
	}

	public void setFilePath( String filePath )
	{
		this.filePath = filePath;
	}

	public boolean isNeedConvert( )
	{
		return needConvert;
	}

	public void setNeedConvert( boolean needConvert )
	{
		this.needConvert = needConvert;
	}

	private String getFile( )
	{
		if ( !SQLUtil.isEmpty( filePath ) )
		{
			return "File: " + new File( filePath ).getName( ) + ". ";
		}
		return "";
	}

	public String toString( )
	{
		if ( SQLUtil.isEmpty( info ) )
			return "";
		else
			return new StringBuilder( ).append( getFile( ) )
					.append( info.trim( ) )
					.append( " Line:"
							+ position.getX( )
							+ ", column:"
							+ position.getY( )
							+ "." )
					.append( "\n" )
					.toString( );
	}

	public boolean isNeedAnsiJoin( )
	{
		return needAnsiJoin;
	}

	public void setNeedAnsiJoin( boolean needAnsiJoin )
	{
		this.needAnsiJoin = needAnsiJoin;
	}

}
