
package com.gudusoft.sqlfrog.test.table;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.gudusoft.sqlfrog.SqlFrog;
import com.gudusoft.sqlfrog.model.FrogResult;
import com.gudusoft.sqlfrog.test.BaseTestCase;
import com.gudusoft.sqlfrog.util.SQLUtil;

import gudusoft.gsqlparser.EDbVendor;

public class PostgresqlSubqueryTableConvertTest extends BaseTestCase
{

	public void testOracleTableToPostgresql( )
	{
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			keyGenerator.init(new SecureRandom());
			SecretKey secretKey = keyGenerator.generateKey();
			String byteKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
			System.out.println(new String(byteKey));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sqltext = "select a, d from (select a, b, d from c)";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvpostgresql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "select t.a, t.d from (select a, b, d from c) t";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvpostgresql ),
				formatResult( convertValue, EDbVendor.dbvpostgresql ) );
	}
}
