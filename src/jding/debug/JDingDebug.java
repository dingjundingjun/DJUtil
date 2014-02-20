package jding.debug;

import android.util.Log;

public class JDingDebug
{
	private static boolean debug = true;
	public static void printfD(String tag,String p)
	{
		if(debug == true)
			Log.d(tag, p);
	}
	
	public static void printfE(String tag,String p)
	{
		if(debug == true)
			Log.e(tag, p);
	}
	
	public static void printfSystem(String p)
	{
		if(debug == true)
			System.out.println(p);
	}
	
	/**
	 * 打印类名和行号字符串
	 * @param str
	 * @return
	 */
	private static String printfclassLineStr( String str )
	{
		StringBuffer strBuffer = new StringBuffer( );
		StackTraceElement[] mStackTrace = new Throwable( ).getStackTrace( );
		
		strBuffer.append( str );
		strBuffer.append( "，File:" + mStackTrace[2].getFileName( ) );
		strBuffer.append( "，line:" + mStackTrace[2].getLineNumber( ) );
		strBuffer.append( "，Method:" + mStackTrace[2].getMethodName( ) );
		
		return strBuffer.toString( );
	}
}
