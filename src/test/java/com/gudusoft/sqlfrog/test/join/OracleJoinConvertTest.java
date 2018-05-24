
package com.gudusoft.sqlfrog.test.join;

import gudusoft.gsqlparser.EDbVendor;

import com.gudusoft.sqlfrog.SqlFrog;
import com.gudusoft.sqlfrog.model.FrogResult;
import com.gudusoft.sqlfrog.test.BaseTestCase;
import com.gudusoft.sqlfrog.util.SQLUtil;

public class OracleJoinConvertTest extends BaseTestCase
{

	public void testOracleJoinToSqlServer( )
	{
		String sqltext = "select *\r\n"
				+ "from  ods_trf_pnb_stuf_lijst_adrsrt2 lst\r\n"
				+ ", ods_stg_pnb_stuf_pers_adr pas\r\n"
				+ ", ods_stg_pnb_stuf_pers_nat nat\r\n"
				+ ", ods_stg_pnb_stuf_adr adr\r\n"
				+ ", ods_stg_pnb_stuf_np prs\r\n"
				+ "where\r\n"
				+ "pas.soort_adres = lst.soort_adres\r\n"
				+ "and prs.id(+) = nat.prs_id\r\n"
				+ "and adr.id = pas.adr_id\r\n"
				+ "and prs.id = pas.prs_id\r\n"
				+ "and lst.persoonssoort = 'PERSOON'\r\n"
				+ "and pas.einddatumrelatie is null;";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvmssql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "SELECT *\r\n"
				+ "FROM   ods_trf_pnb_stuf_lijst_adrsrt2 lst,\r\n"
				+ "ods_stg_pnb_stuf_pers_adr pas,\r\n"
				+ "ods_stg_pnb_stuf_pers_nat nat,\r\n"
				+ "ods_stg_pnb_stuf_adr adr,\r\n"
				+ "ods_stg_pnb_stuf_np prs\r\n"
				+ "WHERE  pas.soort_adres = lst.soort_adres\r\n"
				+ "AND prs.id =* nat.prs_id\r\n"
				+ "AND adr.id = pas.adr_id\r\n"
				+ "AND prs.id = pas.prs_id\r\n"
				+ "AND lst.persoonssoort = 'PERSOON'\r\n"
				+ "AND pas.einddatumrelatie IS NULL";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvmssql ),
				formatResult( convertValue, EDbVendor.dbvmssql ) );
	}

	public void testOracleJoinToSqlServer2( )
	{
		String sqltext = "SELECT m.*,\r\n"
				+ "altname.last_name  last_name_student,\r\n"
				+ "altname.first_name first_name_student,\r\n"
				+ "ccu.date_joined,\r\n"
				+ "ccu.last_login,\r\n"
				+ "ccu.photo_id,\r\n"
				+ "ccu.last_updated\r\n"
				+ "FROM   summit.mstr m,\r\n"
				+ "summit.alt_name altname,\r\n"
				+ "smmtccon.ccn_user ccu\r\n"
				+ "WHERE  m.id =?\r\n"
				+ "AND m.id = altname.id(+)\r\n"
				+ "AND m.id = ccu.id(+)\r\n"
				+ "AND altname.grad_name_ind(+) = '*';";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvmssql,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "SELECT m.*,\r\n"
				+ "altname.last_name  last_name_student,\r\n"
				+ "altname.first_name first_name_student,\r\n"
				+ "ccu.date_joined,\r\n"
				+ "ccu.last_login,\r\n"
				+ "ccu.photo_id,\r\n"
				+ "ccu.last_updated\r\n"
				+ "FROM   summit.mstr m,\r\n"
				+ "summit.alt_name altname,\r\n"
				+ "smmtccon.ccn_user ccu\r\n"
				+ "WHERE  m.id = ?\r\n"
				+ "AND m.id *= altname.id\r\n"
				+ "AND m.id *= ccu.id\r\n"
				+ "AND altname.grad_name_ind =* '*'";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvmssql ),
				formatResult( convertValue, EDbVendor.dbvmssql ) );
	}

	public void testOracleJoinToAsni( )
	{
		String sqltext = "select *\r\n"
				+ "from  ods_trf_pnb_stuf_lijst_adrsrt2 lst\r\n"
				+ ", ods_stg_pnb_stuf_pers_adr pas\r\n"
				+ ", ods_stg_pnb_stuf_pers_nat nat\r\n"
				+ ", ods_stg_pnb_stuf_adr adr\r\n"
				+ ", ods_stg_pnb_stuf_np prs\r\n"
				+ "where\r\n"
				+ "pas.soort_adres = lst.soort_adres\r\n"
				+ "and prs.id(+) = nat.prs_id\r\n"
				+ "and adr.id = pas.adr_id\r\n"
				+ "and prs.id = pas.prs_id\r\n"
				+ "and lst.persoonssoort = 'PERSOON'\r\n"
				+ "and pas.einddatumrelatie is null;";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvansi,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "SELECT *\r\n"
				+ "FROM   ods_trf_pnb_stuf_lijst_adrsrt2 lst\r\n"
				+ "INNER JOIN ods_stg_pnb_stuf_pers_adr pas\r\n"
				+ "ON pas.soort_adres = lst.soort_adres\r\n"
				+ "RIGHT OUTER JOIN ods_stg_pnb_stuf_pers_nat nat\r\n"
				+ "ON prs.id = nat.prs_id\r\n"
				+ "INNER JOIN ods_stg_pnb_stuf_adr adr\r\n"
				+ "ON adr.id = pas.adr_id\r\n"
				+ "INNER JOIN ods_stg_pnb_stuf_np prs\r\n"
				+ "ON prs.id = pas.prs_id\r\n"
				+ "WHERE  lst.persoonssoort = 'PERSOON'\r\n"
				+ "AND pas.einddatumrelatie IS NULL";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvansi ),
				formatResult( convertValue, EDbVendor.dbvansi ) );
	}

	public void testOracleJoinToAnsi2( )
	{
		String sqltext = "SELECT m.*,\r\n"
				+ "altname.last_name  last_name_student,\r\n"
				+ "altname.first_name first_name_student,\r\n"
				+ "ccu.date_joined,\r\n"
				+ "ccu.last_login,\r\n"
				+ "ccu.photo_id,\r\n"
				+ "ccu.last_updated\r\n"
				+ "FROM   summit.mstr m,\r\n"
				+ "summit.alt_name altname,\r\n"
				+ "smmtccon.ccn_user ccu\r\n"
				+ "WHERE  m.id =?\r\n"
				+ "AND m.id = altname.id(+)\r\n"
				+ "AND m.id = ccu.id(+)\r\n"
				+ "AND altname.grad_name_ind(+) = '*';";
		SqlFrog frog = new SqlFrog( );
		FrogResult result = frog.convert( sqltext,
				EDbVendor.dbvoracle,
				EDbVendor.dbvansi,
				false );
		assertTrue( SQLUtil.isEmpty( result.getErrorMessage( ) ) );

		String convertValue = "SELECT m.*,\r\n"
				+ "altname.last_name  last_name_student,\r\n"
				+ "altname.first_name first_name_student,\r\n"
				+ "ccu.date_joined,\r\n"
				+ "ccu.last_login,\r\n"
				+ "ccu.photo_id,\r\n"
				+ "ccu.last_updated\r\n"
				+ "FROM   summit.mstr m\r\n"
				+ "LEFT OUTER JOIN summit.alt_name altname\r\n"
				+ "ON m.id = altname.id AND altname.grad_name_ind = '*'\r\n"
				+ "LEFT OUTER JOIN smmtccon.ccn_user ccu\r\n"
				+ "ON m.id = ccu.id\r\n"
				+ "WHERE  m.id = ?";

		assertEquals( formatResult( result.getResult( ), EDbVendor.dbvansi ),
				formatResult( convertValue, EDbVendor.dbvansi ) );
	}

}
