
package com.gudusoft.sqlfrog.converter.join;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.EJoinType;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.IExpressionVisitor;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TJoinItem;
import gudusoft.gsqlparser.nodes.TJoinList;
import gudusoft.gsqlparser.nodes.TParseTreeNode;
import gudusoft.gsqlparser.nodes.TTable;
import gudusoft.gsqlparser.nodes.TTableList;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AnsiJoinConverter
{

	enum jointype {
		inner, left, right, cross, join, full
	};

	class FromClause
	{

		TTable table;
		TTable joinTable;
		String joinClause;
		String condition;
	}

	class JoinCondition
	{

		public String lefttable, righttable, leftcolumn, rightcolumn;
		public jointype jt;
		public Boolean used;
		public TExpression lexpr, rexpr, expr;
	}

	class getJoinConditionVisitor implements IExpressionVisitor
	{

		Boolean isFirstExpr = true;
		ArrayList<JoinCondition> jrs = new ArrayList<JoinCondition>( );

		public ArrayList<JoinCondition> getJrs( )
		{
			return jrs;
		}

		boolean is_compare_condition( EExpressionType t )
		{
			return ( ( t == EExpressionType.simple_comparison_t )
					|| ( t == EExpressionType.group_comparison_t )
					|| ( t == EExpressionType.in_t ) || ( t == EExpressionType.pattern_matching_t ) );
		}

		TExpression getCompareCondition( TExpression expr )
		{
			if ( is_compare_condition( expr.getExpressionType( ) ) )
				return expr;
			TExpression parentExpr = expr.getParentExpr( );
			if ( parentExpr == null )
				return null;
			return getCompareCondition( parentExpr );
		}

		private void analyzeJoinCondition( TExpression expr,
				TExpression parent_expr )
		{
			TExpression slexpr, srexpr, lc_expr = expr;

			if ( lc_expr.getGsqlparser( ).getDbVendor( ) == EDbVendor.dbvmssql )
			{
				if ( lc_expr.getExpressionType( ) == EExpressionType.left_join_t
						|| lc_expr.getExpressionType( ) == EExpressionType.right_join_t )
				{
					analyzeMssqlJoinCondition( lc_expr );
				}
			}

			slexpr = lc_expr.getLeftOperand( );
			srexpr = lc_expr.getRightOperand( );

			if ( is_compare_condition( lc_expr.getExpressionType( ) ) )
			{

				if ( slexpr.isOracleOuterJoin( ) || srexpr.isOracleOuterJoin( ) )
				{
					JoinCondition jr = new JoinCondition( );
					jr.used = false;
					jr.lexpr = slexpr;
					jr.rexpr = srexpr;
					jr.expr = expr;
					if ( slexpr.isOracleOuterJoin( ) )
					{
						// If the plus is on the left, the join type is right
						// out join.
						jr.jt = jointype.right;
						// remove (+)
						slexpr.getEndToken( ).setString( "" );
					}
					if ( srexpr.isOracleOuterJoin( ) )
					{
						// If the plus is on the right, the join type is left
						// out join.
						jr.jt = jointype.left;
						srexpr.getEndToken( ).setString( "" );
					}

					jr.lefttable = getExpressionTable( slexpr );
					jr.righttable = getExpressionTable( srexpr );

					jrs.add( jr );
					// System.out.printf( "join condition: %s\n", expr.toString(
					// ) );
				}
				else if ( ( slexpr.getExpressionType( ) == EExpressionType.simple_object_name_t )
						&& ( !slexpr.toString( ).startsWith( ":" ) )
						&& ( !slexpr.toString( ).startsWith( "?" ) )
						&& ( srexpr.getExpressionType( ) == EExpressionType.simple_object_name_t )
						&& ( !srexpr.toString( ).startsWith( ":" ) )
						&& ( !srexpr.toString( ).startsWith( "?" ) ) )
				{
					JoinCondition jr = new JoinCondition( );
					jr.used = false;
					jr.lexpr = slexpr;
					jr.rexpr = srexpr;
					jr.expr = expr;
					jr.jt = jointype.inner;
					jr.lefttable = getExpressionTable( slexpr );
					jr.righttable = getExpressionTable( srexpr );
					jrs.add( jr );
					// System.out.printf(
					// "join condition: %s, %s:%d, %s:%d, %s\n",
					// expr.toString( ),
					// slexpr.toString( ),
					// slexpr.getExpressionType( ),
					// srexpr.toString( ),
					// srexpr.getExpressionType( ),
					// srexpr.getObjectOperand( ).getObjectType( ) );
				}
				else if ( ( slexpr.getExpressionType( ) == EExpressionType.simple_object_name_t )
						&& ( !slexpr.toString( ).startsWith( ":" ) )
						&& ( !slexpr.toString( ).startsWith( "?" ) )
						&& ( srexpr.getExpressionType( ) == EExpressionType.function_t ) )
				{
					List<String> tables = new ArrayList<String>( );
					getExpressionTables( tables, srexpr );
					if ( !tables.isEmpty( ) )
					{
						for ( int i = 0; i < tables.size( ); i++ )
						{
							JoinCondition jr = new JoinCondition( );
							jr.used = false;
							jr.lexpr = slexpr;
							jr.rexpr = srexpr;
							jr.expr = expr;
							jr.jt = jointype.inner;
							jr.lefttable = getExpressionTable( slexpr );
							jr.righttable = tables.get( i );
							jrs.add( jr );
						}
					}
				}
			}
			else if ( slexpr != null
					&& slexpr.isOracleOuterJoin( )
					&& srexpr == null )
			{
				JoinCondition jr = new JoinCondition( );
				jr.used = false;
				jr.lexpr = slexpr;
				jr.rexpr = srexpr;
				jr.expr = expr;

				jr.jt = jointype.right;
				// remove (+)
				slexpr.getEndToken( ).setString( "" );

				jr.lefttable = getExpressionTable( slexpr );
				jr.righttable = null;

				jrs.add( jr );
			}
			else if ( lc_expr.isOracleOuterJoin( )
					&& parent_expr != null
					&& !is_compare_condition( parent_expr.getExpressionType( ) ) )
			{
				TExpression expression = getCompareCondition( parent_expr );
				if ( expression != null )
				{
					slexpr = expression.getLeftOperand( );
					srexpr = expression.getRightOperand( );

					JoinCondition jr = new JoinCondition( );
					jr.used = false;
					jr.lexpr = slexpr;
					jr.rexpr = srexpr;
					jr.expr = expression;
					lc_expr.getEndToken( ).setString( "" );
					if ( slexpr.getEndToken( ).posinlist >= lc_expr.getStartToken( ).posinlist )
					{
						jr.jt = jointype.right;
					}
					else
					{
						jr.jt = jointype.left;
					}

					jr.lefttable = getExpressionTable( slexpr );
					jr.righttable = getExpressionTable( srexpr );

					jrs.add( jr );
				}
			}

		}

		private void analyzeMssqlJoinCondition( TExpression expr )
		{
			TExpression slexpr = expr.getLeftOperand( );
			TExpression srexpr = expr.getRightOperand( );

			JoinCondition jr = new JoinCondition( );
			jr.used = false;
			jr.lexpr = slexpr;
			jr.rexpr = srexpr;
			jr.expr = expr;
			expr.getOperatorToken( ).setString( "=" );
			if ( expr.getExpressionType( ) == EExpressionType.left_join_t )
			{
				jr.jt = jointype.left;
			}
			if ( expr.getExpressionType( ) == EExpressionType.right_join_t )
			{
				jr.jt = jointype.right;
			}

			jr.lefttable = getExpressionTable( slexpr );
			jr.righttable = getExpressionTable( srexpr );

			jrs.add( jr );
		}

		public boolean exprVisit( TParseTreeNode pNode, boolean isLeafNode )
		{
			TExpression expr = (TExpression) pNode;
			if ( expr.getExpressionType( ) == EExpressionType.function_t )
			{
				if ( expr.getFunctionCall( ).getArgs( ) != null )
				{
					for ( int i = 0; i < expr.getFunctionCall( )
							.getArgs( )
							.size( ); i++ )
					{
						TExpression arg = expr.getFunctionCall( )
								.getArgs( )
								.getExpression( i );
						analyzeJoinCondition( arg, expr );
						if ( isLeafNode )
						{
							exprVisit( arg, isLeafNode );
						}
					}
				}
				else if ( expr.getFunctionCall( ).getExpr1( ) != null )
				{
					TExpression arg = expr.getFunctionCall( ).getExpr1( );
					analyzeJoinCondition( arg, expr );
					if ( isLeafNode )
					{
						exprVisit( arg, isLeafNode );
					}
				}
			}
			else
			{
				analyzeJoinCondition( expr, null );
			}
			return true;

		}
	}

	private String ErrorMessage = "";

	public String getErrorMessage( )
	{
		return ErrorMessage;
	}

	private int ErrorNo;

	private String query;
	private EDbVendor vendor;

	public AnsiJoinConverter( String sql, EDbVendor vendor )
	{
		this.query = sql;
		this.vendor = vendor;
	}

	public String getQuery( )
	{
		return this.query.replaceAll( "(?m)^[ \t]*\r?\n", "" );
	}

	public int convert( )
	{
		TGSqlParser sqlparser = new TGSqlParser( vendor );
		sqlparser.sqltext = this.query;
		ErrorNo = sqlparser.parse( );
		if ( ErrorNo != 0 )
		{
			ErrorMessage = sqlparser.getErrormessage( );
			return ErrorNo;
		}

		TCustomSqlStatement stmt = sqlparser.sqlstatements.get( 0 );
		analyzeSelect( stmt );
		this.query = stmt.toString( );
		return ErrorNo;
	}

	private boolean isNameOfTable( TTable table, String name )
	{
		return ( name == null ) ? false : table.getName( )
				.equalsIgnoreCase( name );
	}

	private boolean isAliasOfTable( TTable table, String alias )
	{
		if ( table.getAliasClause( ) == null )
		{
			return false;
		}
		else
			return ( alias == null ) ? false : table.getAliasClause( )
					.toString( )
					.equalsIgnoreCase( alias );
	}

	private boolean isNameOrAliasOfTable( TTable table, String str )
	{
		return isAliasOfTable( table, str ) || isNameOfTable( table, str );
	}

	private boolean areTableJoined( TTable lefttable, TTable righttable,
			ArrayList<JoinCondition> jrs )
	{

		boolean ret = false;

		for ( int i = 0; i < jrs.size( ); i++ )
		{
			JoinCondition jc = jrs.get( i );
			if ( jc.used )
			{
				continue;
			}
			ret = isNameOrAliasOfTable( lefttable, jc.lefttable )
					&& isNameOrAliasOfTable( righttable, jc.righttable );
			if ( ret )
				break;
			ret = isNameOrAliasOfTable( lefttable, jc.righttable )
					&& isNameOrAliasOfTable( righttable, jc.lefttable );
			if ( ret )
				break;
		}

		return ret;
	}

	private String getJoinType( ArrayList<JoinCondition> jrs )
	{
		String str = "inner join";
		for ( int i = 0; i < jrs.size( ); i++ )
		{
			if ( jrs.get( i ).jt == jointype.left )
			{
				str = "left outer join";
				break;
			}
			else if ( jrs.get( i ).jt == jointype.right )
			{
				str = "right outer join";
				break;
			}
			else if ( jrs.get( i ).jt == jointype.full )
			{
				str = "full outer join";
				break;
			}
			else if ( jrs.get( i ).jt == jointype.cross )
			{
				str = "cross join";
				break;
			}
			else if ( jrs.get( i ).jt == jointype.join )
			{
				str = "join";
				break;
			}
		}

		return str;
	}

	private ArrayList<JoinCondition> getJoinCondition( TTable lefttable,
			TTable righttable, ArrayList<JoinCondition> jrs )
	{
		ArrayList<JoinCondition> lcjrs = new ArrayList<JoinCondition>( );
		for ( int i = 0; i < jrs.size( ); i++ )
		{
			JoinCondition jc = jrs.get( i );
			if ( jc.used )
			{
				continue;
			}

			if ( isNameOrAliasOfTable( lefttable, jc.lefttable )
					&& isNameOrAliasOfTable( righttable, jc.righttable ) )
			{
				lcjrs.add( jc );
				jc.used = true;
			}
			else if ( isNameOrAliasOfTable( lefttable, jc.righttable )
					&& isNameOrAliasOfTable( righttable, jc.lefttable ) )
			{
				if ( jc.jt == jointype.left )
					jc.jt = jointype.right;
				else if ( jc.jt == jointype.right )
					jc.jt = jointype.left;

				lcjrs.add( jc );
				jc.used = true;
			}
			else if ( ( jc.lefttable == null )
					&& ( isNameOrAliasOfTable( lefttable, jc.righttable ) || isNameOrAliasOfTable( righttable,
							jc.righttable ) ) )
			{
				lcjrs.add( jc );
				jc.used = true;
			}
			else if ( ( jc.righttable == null )
					&& ( isNameOrAliasOfTable( lefttable, jc.lefttable ) || isNameOrAliasOfTable( righttable,
							jc.lefttable ) ) )
			{
				if ( jc.jt == jointype.left )
					jc.jt = jointype.right;
				else if ( jc.jt == jointype.right )
					jc.jt = jointype.left;
				lcjrs.add( jc );
				jc.used = true;
			}
		}
		return lcjrs;
	}

	private void analyzeSelect( final TCustomSqlStatement stmt )
	{
		if ( stmt instanceof TSelectSqlStatement )
		{
			final TSelectSqlStatement select = (TSelectSqlStatement) stmt;
			if ( !select.isCombinedQuery( ) )
			{
				for ( int i = 0; i < select.getStatements( ).size( ); i++ )
				{
					if ( select.getStatements( ).get( i ) instanceof TSelectSqlStatement )
					{
						analyzeSelect( (TSelectSqlStatement) select.getStatements( )
								.get( i ) );
					}
				}

				if ( select.tables.size( ) == 1 )
					return;

				if ( select.getWhereClause( ) == null )
				{
					if ( select.tables.size( ) > 1 )
					{
						if ( !hasJoin( select.joins ) )
						{
							// cross join
							String str = getFullNameWithAliasString( select.tables.getTable( 0 ) );
							for ( int i = 1; i < select.tables.size( ); i++ )
							{
								str = str
										+ "\ncross join "
										+ getFullNameWithAliasString( select.tables.getTable( i ) );
							}

							for ( int k = select.joins.size( ) - 1; k > 0; k-- )
							{
								select.joins.removeJoin( k );
							}
							select.joins.getJoin( 0 ).setString( str );
						}
					}
				}
				else
				{

					getJoinConditionVisitor v = new getJoinConditionVisitor( );

					// get join conditions
					select.getWhereClause( )
							.getCondition( )
							.preOrderTraverse( v );
					ArrayList<JoinCondition> jrs = v.getJrs( );

					if ( select.joins != null && select.joins.size( ) > 0 )
					{
						for ( int i = 0; i < select.joins.size( ); i++ )
						{
							TJoin join = select.joins.getJoin( i );
							for ( int j = 0; j < join.getJoinItems( ).size( ); j++ )
							{
								TJoinItem item = join.getJoinItems( )
										.getJoinItem( j );
								JoinCondition jr = new JoinCondition( );
								jr.expr = item.getOnCondition( );
								jr.used = false;
								jr.lexpr = jr.expr.getLeftOperand( );
								jr.rexpr = jr.expr.getRightOperand( );
								jr.lefttable = getExpressionTable( jr.lexpr );
								jr.righttable = getExpressionTable( jr.rexpr );
								if ( item.getJoinType( ) == EJoinType.inner )
								{
									jr.jt = jointype.inner;
									jrs.add( jr );
								}
								if ( item.getJoinType( ) == EJoinType.left
										|| item.getJoinType( ) == EJoinType.leftouter )
								{
									jr.jt = jointype.left;
									jrs.add( jr );
								}
								if ( item.getJoinType( ) == EJoinType.right
										|| item.getJoinType( ) == EJoinType.rightouter )
								{
									jr.jt = jointype.right;
									jrs.add( jr );
								}
								if ( item.getJoinType( ) == EJoinType.full
										|| item.getJoinType( ) == EJoinType.fullouter )
								{
									jr.jt = jointype.full;
									jrs.add( jr );
								}
								if ( item.getJoinType( ) == EJoinType.join )
								{
									jr.jt = jointype.join;
									jrs.add( jr );
								}
								if ( item.getJoinType( ) == EJoinType.cross )
								{
									jr.jt = jointype.cross;
									jrs.add( jr );
								}
							}
						}
					}

					List<TTable> tables = new ArrayList<TTable>( );
					for ( int i = 0; i < select.tables.size( ); i++ )
					{
						tables.add( select.tables.getTable( i ) );
					}

					TCustomSqlStatement parentStmt = select;
					while ( parentStmt.getParentStmt( ) != null )
					{
						parentStmt = select.getParentStmt( );
						if ( parentStmt instanceof TSelectSqlStatement )
						{
							TSelectSqlStatement temp = (TSelectSqlStatement) parentStmt;
							for ( int i = 0; i < temp.tables.size( ); i++ )
							{
								tables.add( temp.tables.getTable( i ) );
							}
						}
					}

					boolean tableUsed[] = new boolean[tables.size( )];
					for ( int i = 0; i < tables.size( ); i++ )
					{
						tableUsed[i] = false;
					}

					// make first table to be the left most joined table
					String fromclause = getFullNameWithAliasString( tables.get( 0 ) );

					tableUsed[0] = true;
					boolean foundTableJoined;
					final ArrayList<FromClause> fromClauses = new ArrayList<FromClause>( );
					for ( ;; )
					{
						foundTableJoined = false;

						for ( int i = 0; i < tables.size( ); i++ )
						{
							TTable lcTable1 = tables.get( i );

							TTable leftTable = null, rightTable = null;
							for ( int j = i + 1; j < tables.size( ); j++ )
							{
								TTable lcTable2 = tables.get( j );
								if ( areTableJoined( lcTable1, lcTable2, jrs ) )
								{
									if ( tableUsed[i] && ( !tableUsed[j] ) )
									{
										leftTable = lcTable1;
										rightTable = lcTable2;
									}
									else if ( ( !tableUsed[i] ) && tableUsed[j] )
									{
										leftTable = lcTable2;
										rightTable = lcTable1;
									}
									else if ( ( !tableUsed[i] )
											&& ( !tableUsed[j] ) )
									{
										leftTable = lcTable1;
										rightTable = lcTable2;
									}

									if ( ( leftTable != null )
											&& ( rightTable != null ) )
									{
										ArrayList<JoinCondition> lcjrs = getJoinCondition( leftTable,
												rightTable,
												jrs );
										if ( lcjrs.isEmpty( ) )
											continue;
										FromClause fc = new FromClause( );
										fc.table = leftTable;
										fc.joinTable = rightTable;
										fc.joinClause = getJoinType( lcjrs );
										String condition = "";
										for ( int k = 0; k < lcjrs.size( ); k++ )
										{
											TExpression expr = lcjrs.get( k ).expr;
											if ( expr.toString( ) != null )
											{
												if ( condition.length( ) > 0 )
												{
													condition += " and ";
												}
												condition += expr.toString( );
											}
											expr.remove2( );
										}
										fc.condition = condition;

										fromClauses.add( fc );
										tableUsed[i] = true;
										tableUsed[j] = true;

										foundTableJoined = true;
									}
								}
							}
						}

						if ( !foundTableJoined )
						{
							break;
						}
					}

					// are all join conditions used?
					for ( int i = 0; i < jrs.size( ); i++ )
					{
						JoinCondition jc = jrs.get( i );
						if ( !jc.used )
						{
							for ( int j = fromClauses.size( ) - 1; j >= 0; j-- )
							{
								if ( isNameOrAliasOfTable( fromClauses.get( j ).joinTable,
										jc.lefttable )
										|| isNameOrAliasOfTable( fromClauses.get( j ).joinTable,
												jc.righttable ) )
								{
									if ( jc.expr.toString( ) != null )
									{
										fromClauses.get( j ).condition += " and "
												+ jc.expr.toString( );
									}
									jc.used = true;
									jc.expr.remove2( );
									break;

								}
							}
						}
					}

					for ( int i = 0; i < select.tables.size( ); i++ )
					{
						if ( !tableUsed[i] )
						{
							ErrorNo++;
							ErrorMessage += String.format( "%sError %d, Message: %s",
									System.getProperty( "line.separator" ),
									ErrorNo,
									"This table has no join condition: "
											+ select.tables.getTable( i )
													.getFullName( ) );
						}
					}

					Collections.sort( fromClauses,
							new Comparator<FromClause>( ) {

								public int compare( FromClause o1, FromClause o2 )
								{
									return indexOf( select, o1.joinTable )
											- indexOf( select, o2.joinTable );
								}

								private int indexOf(
										TSelectSqlStatement select,
										TTable joinTable )
								{
									TTableList tables = select.tables;
									for ( int i = 0; i < tables.size( ); i++ )
									{
										if ( joinTable != null
												&& tables.getTable( i )
														.equals( joinTable ) )
											return i;
									}
									return -1;
								}
							} );

					Collections.sort( fromClauses,
							new Comparator<FromClause>( ) {

								public int compare( FromClause o1, FromClause o2 )
								{
									if ( o1.table.equals( o2.joinTable ) )
										return 1;
									else if ( o2.table.equals( o1.joinTable ) )
										return -1;
									else
										return fromClauses.indexOf( o1 )
												- fromClauses.indexOf( o2 );
								}
							} );

					// link all join clause
					for ( int i = 0; i < fromClauses.size( ); i++ )
					{
						FromClause fc = fromClauses.get( i );
						fromclause += "\n"
								+ fc.joinClause
								+ " "
								+ getFullNameWithAliasString( fc.joinTable )
								+ " on "
								+ fc.condition;
					}

					for ( int k = select.joins.size( ) - 1; k > 0; k-- )
					{
						select.joins.removeJoin( k );
					}

					select.joins.getJoin( 0 ).setString( fromclause );

					if ( ( select.getWhereClause( )
							.getCondition( )
							.getStartToken( ) == null )
							|| ( select.getWhereClause( )
									.getCondition( )
									.toString( )
									.trim( )
									.length( ) == 0 ) )
					{
						// no where condition, remove WHERE keyword
						select.getWhereClause( ).fastSetString( " " );

					}
					else
					{
						select.getWhereClause( )
								.getCondition( )
								.fastSetString( select.getWhereClause( )
										.getCondition( )
										.toString( )
										.trim( ) );
					}
				}
			}
			else
			{
				analyzeSelect( select.getLeftStmt( ) );
				analyzeSelect( select.getRightStmt( ) );
			}
		}
		else if ( stmt.getStatements( ) != null )
		{
			for ( int i = 0; i < stmt.getStatements( ).size( ); i++ )
			{
				analyzeSelect( stmt.getStatements( ).get( i ) );
			}
		}
	}

	private boolean hasJoin( TJoinList joins )
	{
		if ( joins == null )
			return false;
		for ( int i = 0; i < joins.size( ); i++ )
		{
			if ( joins.getJoin( i ).getJoinItems( ) != null
					&& joins.getJoin( i ).getJoinItems( ).size( ) > 0 )
				return true;
		}
		return false;
	}

	private String getFullNameWithAliasString( TTable table )
	{
		if ( table.getSubquery( ) != null )
		{
			if ( table.getAliasClause( ) != null )
			{
				return table.getSubquery( )
						+ " "
						+ table.getAliasClause( ).toString( );
			}
			else
			{
				return table.getSubquery( ).toString( );
			}
		}
		else if ( table.getFullName( ) != null )
			return table.getFullNameWithAliasString( );
		else
			return table.toString( );
	}

	private void getExpressionTables( List<String> tables, TExpression expr )
	{
		if ( expr.getExpressionType( ) == EExpressionType.function_t )
		{
			if ( expr.getFunctionCall( ).getArgs( ) != null )
			{
				for ( int i = 0; i < expr.getFunctionCall( ).getArgs( ).size( ); i++ )
				{
					getExpressionTables( tables, expr.getFunctionCall( )
							.getArgs( )
							.getExpression( i ) );
				}
			}
			if ( expr.getFunctionCall( ).getExpr1( ) != null )
			{
				getExpressionTables( tables, expr.getFunctionCall( ).getExpr1( ) );

			}
			if ( expr.getFunctionCall( ).getExpr2( ) != null )
			{
				getExpressionTables( tables, expr.getFunctionCall( ).getExpr2( ) );
			}
			if ( expr.getFunctionCall( ).getExpr3( ) != null )
			{
				getExpressionTables( tables, expr.getFunctionCall( ).getExpr3( ) );
			}
		}
		else if ( expr.getObjectOperand( ) != null )
			tables.add( expr.getObjectOperand( ).getObjectString( ) );
		else if ( expr.getLeftOperand( ) != null
				&& expr.getLeftOperand( ).getObjectOperand( ) != null )
			tables.add( expr.getLeftOperand( )
					.getObjectOperand( )
					.getObjectString( ) );
		else if ( expr.getRightOperand( ) != null
				&& expr.getRightOperand( ).getObjectOperand( ) != null )
			tables.add( expr.getRightOperand( )
					.getObjectOperand( )
					.getObjectString( ) );
	}

	private String getExpressionTable( TExpression expr )
	{
		if ( expr.getExpressionType( ) == EExpressionType.function_t )
		{
			if ( expr.getFunctionCall( ).getArgs( ) != null )
			{
				for ( int i = 0; i < expr.getFunctionCall( ).getArgs( ).size( ); i++ )
				{
					String table = getExpressionTable( expr.getFunctionCall( )
							.getArgs( )
							.getExpression( i ) );
					if ( table != null )
						return table;
				}
			}
			if ( expr.getFunctionCall( ).getExpr1( ) != null )
			{
				String table = getExpressionTable( expr.getFunctionCall( )
						.getExpr1( ) );
				if ( table != null )
					return table;
			}
		}
		else if ( expr.getObjectOperand( ) != null )
			return expr.getObjectOperand( ).getObjectString( );
		else if ( expr.getLeftOperand( ) != null
				&& expr.getLeftOperand( ).getObjectOperand( ) != null )
			return expr.getLeftOperand( ).getObjectOperand( ).getObjectString( );
		else if ( expr.getRightOperand( ) != null
				&& expr.getRightOperand( ).getObjectOperand( ) != null )
			return expr.getRightOperand( )
					.getObjectOperand( )
					.getObjectString( );

		return null;
	}
}