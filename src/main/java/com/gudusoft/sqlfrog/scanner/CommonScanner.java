
package com.gudusoft.sqlfrog.scanner;

import java.util.ArrayList;
import java.util.List;

import com.gudusoft.sqlfrog.model.AutomaticKey;
import com.gudusoft.sqlfrog.model.Concatenation;
import com.gudusoft.sqlfrog.model.ConvertPoint;
import com.gudusoft.sqlfrog.model.CopyingStructure;
import com.gudusoft.sqlfrog.model.DataType;
import com.gudusoft.sqlfrog.model.Function;
import com.gudusoft.sqlfrog.model.JoinCondition;
import com.gudusoft.sqlfrog.model.LimitResultSet;
import com.gudusoft.sqlfrog.model.LocalTimestamp;
import com.gudusoft.sqlfrog.model.QuotedIdentifier;
import com.gudusoft.sqlfrog.model.SequenceIdentifier;
import com.gudusoft.sqlfrog.model.Table;

import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.EDeclareType;
import gudusoft.gsqlparser.EExpressionType;
import gudusoft.gsqlparser.TBaseType;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import gudusoft.gsqlparser.nodes.TAliasClause;
import gudusoft.gsqlparser.nodes.TAlterTableOption;
import gudusoft.gsqlparser.nodes.TCTE;
import gudusoft.gsqlparser.nodes.TCTEList;
import gudusoft.gsqlparser.nodes.TCaseExpression;
import gudusoft.gsqlparser.nodes.TColumnDefinition;
import gudusoft.gsqlparser.nodes.TColumnDefinitionList;
import gudusoft.gsqlparser.nodes.TConnectByClause;
import gudusoft.gsqlparser.nodes.TConstant;
import gudusoft.gsqlparser.nodes.TConstraint;
import gudusoft.gsqlparser.nodes.TConstraintList;
import gudusoft.gsqlparser.nodes.TDeclareVariable;
import gudusoft.gsqlparser.nodes.TDeclareVariableList;
import gudusoft.gsqlparser.nodes.TExceptionClause;
import gudusoft.gsqlparser.nodes.TExceptionHandler;
import gudusoft.gsqlparser.nodes.TExceptionHandlerList;
import gudusoft.gsqlparser.nodes.TExecParameter;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TExpressionList;
import gudusoft.gsqlparser.nodes.TForUpdate;
import gudusoft.gsqlparser.nodes.TFunctionCall;
import gudusoft.gsqlparser.nodes.TGroupBy;
import gudusoft.gsqlparser.nodes.TGroupByItem;
import gudusoft.gsqlparser.nodes.TGroupByItemList;
import gudusoft.gsqlparser.nodes.TGroupingExpressionItem;
import gudusoft.gsqlparser.nodes.TGroupingSet;
import gudusoft.gsqlparser.nodes.THierarchical;
import gudusoft.gsqlparser.nodes.TInExpr;
import gudusoft.gsqlparser.nodes.TInformixOuterClause;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TJoinItem;
import gudusoft.gsqlparser.nodes.TJoinList;
import gudusoft.gsqlparser.nodes.TKeepDenseRankClause;
import gudusoft.gsqlparser.nodes.TLimitClause;
import gudusoft.gsqlparser.nodes.TMergeDeleteClause;
import gudusoft.gsqlparser.nodes.TMergeInsertClause;
import gudusoft.gsqlparser.nodes.TMergeUpdateClause;
import gudusoft.gsqlparser.nodes.TMergeWhenClause;
import gudusoft.gsqlparser.nodes.TMultiTarget;
import gudusoft.gsqlparser.nodes.TMultiTargetList;
import gudusoft.gsqlparser.nodes.TObjectName;
import gudusoft.gsqlparser.nodes.TObjectNameList;
import gudusoft.gsqlparser.nodes.TOrderBy;
import gudusoft.gsqlparser.nodes.TOrderByItem;
import gudusoft.gsqlparser.nodes.TOrderByItemList;
import gudusoft.gsqlparser.nodes.TParameterDeclaration;
import gudusoft.gsqlparser.nodes.TParameterDeclarationList;
import gudusoft.gsqlparser.nodes.TParseTreeNode;
import gudusoft.gsqlparser.nodes.TParseTreeVisitor;
import gudusoft.gsqlparser.nodes.TPivotClause;
import gudusoft.gsqlparser.nodes.TPivotInClause;
import gudusoft.gsqlparser.nodes.TPivotedTable;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.nodes.TResultColumnList;
import gudusoft.gsqlparser.nodes.TRollupCube;
import gudusoft.gsqlparser.nodes.TSequenceOption;
import gudusoft.gsqlparser.nodes.TTable;
import gudusoft.gsqlparser.nodes.TTableHint;
import gudusoft.gsqlparser.nodes.TTopClause;
import gudusoft.gsqlparser.nodes.TTrimArgument;
import gudusoft.gsqlparser.nodes.TTypeAttribute;
import gudusoft.gsqlparser.nodes.TTypeAttributeList;
import gudusoft.gsqlparser.nodes.TTypeName;
import gudusoft.gsqlparser.nodes.TUnpivotInClause;
import gudusoft.gsqlparser.nodes.TVarDeclStmt;
import gudusoft.gsqlparser.nodes.TViewAliasItem;
import gudusoft.gsqlparser.nodes.TWhenClauseItem;
import gudusoft.gsqlparser.nodes.TWhenClauseItemList;
import gudusoft.gsqlparser.nodes.TWhereClause;
import gudusoft.gsqlparser.nodes.TWindowDef;
import gudusoft.gsqlparser.nodes.TWindowFrame;
import gudusoft.gsqlparser.nodes.TWindowFrameBoundary;
import gudusoft.gsqlparser.nodes.TWithinGroup;
import gudusoft.gsqlparser.stmt.TAlterTableStatement;
import gudusoft.gsqlparser.stmt.TAssignStmt;
import gudusoft.gsqlparser.stmt.TCaseStmt;
import gudusoft.gsqlparser.stmt.TCloseStmt;
import gudusoft.gsqlparser.stmt.TCommentOnSqlStmt;
import gudusoft.gsqlparser.stmt.TCommonBlock;
import gudusoft.gsqlparser.stmt.TCreateDatabaseSqlStatement;
import gudusoft.gsqlparser.stmt.TCreateIndexSqlStatement;
import gudusoft.gsqlparser.stmt.TCreateMaterializedSqlStatement;
import gudusoft.gsqlparser.stmt.TCreateSchemaSqlStatement;
import gudusoft.gsqlparser.stmt.TCreateSequenceStmt;
import gudusoft.gsqlparser.stmt.TCreateSynonymStmt;
import gudusoft.gsqlparser.stmt.TCreateTableSqlStatement;
import gudusoft.gsqlparser.stmt.TCreateViewSqlStatement;
import gudusoft.gsqlparser.stmt.TCursorDeclStmt;
import gudusoft.gsqlparser.stmt.TDeleteSqlStatement;
import gudusoft.gsqlparser.stmt.TDropIndexSqlStatement;
import gudusoft.gsqlparser.stmt.TDropTableSqlStatement;
import gudusoft.gsqlparser.stmt.TDropViewSqlStatement;
import gudusoft.gsqlparser.stmt.TElsifStmt;
import gudusoft.gsqlparser.stmt.TExitStmt;
import gudusoft.gsqlparser.stmt.TFetchStmt;
import gudusoft.gsqlparser.stmt.TIfStmt;
import gudusoft.gsqlparser.stmt.TInsertSqlStatement;
import gudusoft.gsqlparser.stmt.TLoopStmt;
import gudusoft.gsqlparser.stmt.TMergeSqlStatement;
import gudusoft.gsqlparser.stmt.TOpenStmt;
import gudusoft.gsqlparser.stmt.TOpenforStmt;
import gudusoft.gsqlparser.stmt.TRaiseStmt;
import gudusoft.gsqlparser.stmt.TReturnStmt;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import gudusoft.gsqlparser.stmt.TTruncateStatement;
import gudusoft.gsqlparser.stmt.TUpdateSqlStatement;
import gudusoft.gsqlparser.stmt.TUseDatabase;
import gudusoft.gsqlparser.stmt.mssql.TMssqlBlock;
import gudusoft.gsqlparser.stmt.mssql.TMssqlCommit;
import gudusoft.gsqlparser.stmt.mssql.TMssqlCreateFunction;
import gudusoft.gsqlparser.stmt.mssql.TMssqlCreateProcedure;
import gudusoft.gsqlparser.stmt.mssql.TMssqlCreateTrigger;
import gudusoft.gsqlparser.stmt.mssql.TMssqlCreateXmlSchemaCollectionStmt;
import gudusoft.gsqlparser.stmt.mssql.TMssqlDeclare;
import gudusoft.gsqlparser.stmt.mssql.TMssqlExecute;
import gudusoft.gsqlparser.stmt.mssql.TMssqlGo;
import gudusoft.gsqlparser.stmt.mssql.TMssqlIfElse;
import gudusoft.gsqlparser.stmt.mssql.TMssqlReturn;
import gudusoft.gsqlparser.stmt.mssql.TMssqlRollback;
import gudusoft.gsqlparser.stmt.mssql.TMssqlSaveTran;
import gudusoft.gsqlparser.stmt.mssql.TMssqlSet;
import gudusoft.gsqlparser.stmt.oracle.TBasicStmt;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlContinue;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateFunction;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreatePackage;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateProcedure;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateTrigger;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateType;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateTypeBody;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlCreateType_Placeholder;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlExecImmeStmt;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlForallStmt;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlGotoStmt;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlNullStmt;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlRecordTypeDefStmt;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlTableTypeDefStmt;
import gudusoft.gsqlparser.stmt.oracle.TPlsqlVarrayTypeDefStmt;
import gudusoft.gsqlparser.stmt.oracle.TSqlplusCmdStatement;

public class CommonScanner extends TParseTreeVisitor implements Scanner
{

	private List<ConvertPoint<? extends TParseTreeNode>> convertPoints = new ArrayList<ConvertPoint<? extends TParseTreeNode>>( );

	public List<ConvertPoint<? extends TParseTreeNode>> scan( TGSqlParser parser )
	{
		convertPoints.clear( );
		for ( int i = 0; i < parser.sqlstatements.size( ); i++ )
		{
			parser.sqlstatements.get( i ).accept( this );
		}
		return convertPoints;
	}

	public void postVisit( TConstraint node )
	{
		switch ( node.getConstraint_type( ) )
		{
			case primary_key :
				convertPoints.add( new AutomaticKey( node ) );
				break;
			default :
				break;
		}
	}

	public void postVisit( TExpression expression )
	{
		if ( "CURRENT_TIMESTAMP".equalsIgnoreCase( expression.toString( )
				.toUpperCase( ) ) )
		{
			if ( expression.getGsqlparser( ).getDbVendor( ) == EDbVendor.dbvdb2
					|| expression.getGsqlparser( ).getDbVendor( ) == EDbVendor.dbvmssql )
			{
				convertPoints.add( new LocalTimestamp( expression ) );
			}
		}
		if ( expression.getGsqlparser( ).getDbVendor( ) == EDbVendor.dbvmssql )
		{
			if ( expression.getExpressionType( ) == EExpressionType.left_join_t
					|| expression.getExpressionType( ) == EExpressionType.right_join_t )
			{
				convertPoints.add( new JoinCondition( expression ) );
			}

			if ( expression.getExpressionType( ) == EExpressionType.arithmetic_plus_t )
			{
				TExpression slexpr = expression.getLeftOperand( );
				TExpression srexpr = expression.getRightOperand( );

				if ( slexpr.getExpressionType( ) != EExpressionType.simple_constant_t
						|| srexpr.getExpressionType( ) != EExpressionType.simple_constant_t )
				{
					convertPoints.add( new Concatenation( expression ) );
				}
			}
		}
		else if ( expression.getGsqlparser( ).getDbVendor( ) == EDbVendor.dbvoracle )
		{
			if ( is_compare_condition( expression.getExpressionType( ) ) )
			{
				TExpression slexpr = expression.getLeftOperand( );
				TExpression srexpr = expression.getRightOperand( );

				if ( slexpr.isOracleOuterJoin( ) || srexpr.isOracleOuterJoin( ) )
				{
					convertPoints.add( new JoinCondition( expression ) );
				}
			}
		}
	}

	public void postVisit( TLimitClause limit )
	{
		convertPoints.add( new LimitResultSet( limit ) );
	}

	public void postVisit( TTopClause top )
	{
		convertPoints.add( new LimitResultSet( top ) );
	}

	public void postVisit( TSelectSqlStatement select )
	{
		if ( select.getIntoClause( ) != null )
		{
			convertPoints.add( new CopyingStructure( select ) );
		}

		if ( select.getJoins( ).size( ) > 1
				&& select.dbvendor == EDbVendor.dbvoracle
				&& select.getJoins( ).getJoin( 0 ).getJoinItems( ).size( ) == 0 )
		{
			if ( select.getWhereClause( ) != null )
			{
				convertPoints.add( new JoinCondition( select.getWhereClause( )
						.getCondition( ) ) );
			}
		}
	}

	public void postVisit( TCreateTableSqlStatement create )
	{
		if ( create.getSubQuery( ) != null )
		{
			convertPoints.add( new CopyingStructure( create ) );
		}
	}

	private boolean is_compare_condition( EExpressionType t )
	{
		return ( ( t == EExpressionType.simple_comparison_t )
				|| ( t == EExpressionType.group_comparison_t ) || ( t == EExpressionType.in_t ) );
	}

	public void postVisit( TFunctionCall function )
	{
		convertPoints.add( new Function( function ) );
	}

	public void postVisit( TConstant constant )
	{

	}

	public void postVisit( TObjectName identifier )
	{
		if ( isQuotedIdentifier( identifier ) )
		{
			convertPoints.add( new QuotedIdentifier( identifier ) );
		}

		String partString = identifier.getPartString( ).toUpperCase( );
		if ( identifier.getStartToken( ).getDbvendor( ) == EDbVendor.dbvoracle
				|| identifier.getStartToken( ).getDbvendor( ) == EDbVendor.dbvdb2 )
		{
			if ( partString.equals( "CURRVAL" )
					|| partString.equals( "NEXTVAL" ) )
			{
				convertPoints.add( new SequenceIdentifier( identifier ) );
			}
		}
	}

	private boolean isQuotedIdentifier( TObjectName identifier )
	{
		String content = identifier.toString( );
		if ( identifier.getStartToken( ).getDbvendor( ) == EDbVendor.dbvmssql
				&& content.contains( "[" )
				&& !content.replaceAll( "\\[.+?\\]", "" ).equals( content ) )
		{
			return true;
		}
		else if ( identifier.getStartToken( ).getDbvendor( ) == EDbVendor.dbvmysql
				&& content.contains( "`" )
				&& !content.replaceAll( "`.+?`", "" ).equals( content ) )
		{
			return true;
		}
		else if ( content.contains( "\"" )
				&& !content.replaceAll( "\\\".+?\\\"", "" ).equals( content ) )
		{
			return true;
		}
		return false;
	}

	public void postVisit( TTypeName type )
	{
		convertPoints.add( new DataType( type ) );
	}

	public void preVisit( TSelectSqlStatement node )
	{
		if ( node.getCteList( ) != null )
		{
			node.getCteList( ).accept( this );
		}

		if ( node.isCombinedQuery( ) )
		{
			node.getLeftStmt( ).accept( this );
			node.getRightStmt( ).accept( this );

			if ( node.getOrderbyClause( ) != null )
			{
				node.getOrderbyClause( ).accept( this );
			}

			if ( node.getLimitClause( ) != null )
			{
				node.getLimitClause( ).accept( this );
			}

			if ( node.getForUpdateClause( ) != null )
			{
				node.getForUpdateClause( ).accept( this );
			}

			if ( node.getComputeClause( ) != null )
			{
				node.getComputeClause( ).accept( this );
			}
		}

		if ( node.getValueClause( ) != null )
		{
			node.getValueClause( ).accept( this );
		}
		if ( node.getTopClause( ) != null )
		{
			node.getTopClause( ).accept( this );
		}

		if ( node.getSelectDistinct( ) != null )
		{
			node.getSelectDistinct( ).accept( this );
		}

		if ( node.getResultColumnList( ) != null )
		{
			for ( int i = 0; i < node.getResultColumnList( ).size( ); i++ )
			{
				node.getResultColumnList( ).getElement( i ).accept( this );
			}
		}

		if ( node.getIntoClause( ) != null )
		{
			node.getIntoClause( ).accept( this );
		}

		if ( node.joins.size( ) > 0 )
		{
			node.joins.accept( this );
		}

		if ( node.getWhereClause( ) != null )
		{
			node.getWhereClause( ).accept( this );
		}

		if ( node.getHierarchicalClause( ) != null )
		{
			node.getHierarchicalClause( ).accept( this );
		}

		if ( node.getGroupByClause( ) != null )
		{
			node.getGroupByClause( ).accept( this );
		}

		if ( node.getQualifyClause( ) != null )
		{
			node.getQualifyClause( ).accept( this );
		}

		if ( node.getOrderbyClause( ) != null )
		{
			node.getOrderbyClause( ).accept( this );
		}

		if ( node.getLimitClause( ) != null )
		{
			node.getLimitClause( ).accept( this );
		}

		if ( node.getForUpdateClause( ) != null )
		{
			node.getForUpdateClause( ).accept( this );
		}

		if ( node.getComputeClause( ) != null )
		{
			node.getComputeClause( ).accept( this );
		}
	}

	public void preVisit( TResultColumnList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getResultColumn( i ).accept( this );
		}
	}

	public void preVisit( TResultColumn node )
	{
		node.getExpr( ).accept( this );
		if ( node.getAliasClause( ) != null )
		{
			node.getAliasClause( ).accept( this );
		}
	}

	public void preVisit( TAliasClause node )
	{
		node.getAliasName( ).accept( this );
	}

	public void preVisit( TInExpr node )
	{
		if ( node.getSubQuery( ) != null )
		{
			node.getSubQuery( ).accept( this );
		}
		if ( node.getGroupingExpressionItemList( ) != null )
		{
			node.getGroupingExpressionItemList( ).accept( this );
		}
	}

	public void preVisit( TExpressionList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getExpression( i ).accept( this );
		}
	}

	public void preVisit( TGroupingExpressionItem node )
	{
		if ( node.getExpr( ) != null )
		{
			node.getExpr( ).accept( this );
		}
		if ( node.getExprList( ) != null )
		{
			node.getExprList( ).accept( this );
		}
	}

	public void preVisit( TJoin node )
	{
		if ( node.getTable( ) != null )
		{
			node.getTable( ).accept( this );
		}
		switch ( node.getKind( ) )
		{
			case TBaseType.join_source_fake :
				// node.getTable( ).accept( this );
				break;
			case TBaseType.join_source_table :
			case TBaseType.join_source_join :

				for ( int i = node.getJoinItems( ).size( ) - 1; i >= 0; i-- )
				{
					TJoinItem joinItem = node.getJoinItems( ).getJoinItem( i );

					if ( joinItem.getTable( ) != null )
					{
						joinItem.getTable( ).accept( this );
					}
					if ( joinItem.getJoin( ) != null )
					{
						joinItem.getJoin( ).accept( this );
					}
					if ( joinItem.getOnCondition( ) != null )
					{
						joinItem.getOnCondition( ).accept( this );
					}
				}
		}

	}

	public void preVisit( TUnpivotInClause node )
	{
		for ( int i = 0; i < node.getItems( ).size( ); i++ )
		{
			node.getItems( ).getElement( i ).accept( this );
		}
	}

	public void preVisit( TPivotInClause node )
	{
		if ( node.getItems( ) != null )
		{
			node.getItems( ).accept( this );
		}
		if ( node.getSubQuery( ) != null )
		{
			node.getSubQuery( ).accept( this );
		}
	}

	public void preVisit( TPivotedTable node )
	{
		for ( int i = node.getPivotClauseList( ).size( ) - 1; i >= 0; i-- )
		{
			TPivotClause pivotClause = node.getPivotClauseList( )
					.getElement( i );
			if ( pivotClause.getAliasClause( ) != null )
			{
				pivotClause.getAliasClause( ).accept( this );
			}
			pivotClause.accept( this );

			if ( i == 0 )
			{
				node.getTableSource( ).accept( this );
			}
		}
	}

	public void preVisit( TPivotClause node )
	{
		if ( node.getAggregation_function( ) != null )
		{
			node.getAggregation_function( ).accept( this );
		}
		if ( node.getValueColumn( ) != null )
		{
			node.getValueColumn( ).accept( this );
		}
		if ( node.getValueColumnList( ) != null )
		{
			for ( int i = 0; i < node.getValueColumnList( ).size( ); i++ )
			{
				node.getValueColumnList( ).getObjectName( i ).accept( this );
			}
		}
		if ( node.getPivotColumn( ) != null )
		{
			node.getPivotColumn( ).accept( this );
		}
		if ( node.getPivotColumnList( ) != null )
		{
			node.getPivotColumnList( ).accept( this );
		}

		if ( node.getAggregation_function_list( ) != null )
		{
			node.getAggregation_function_list( ).accept( this );
		}

		if ( node.getIn_result_list( ) != null )
		{
			node.getIn_result_list( ).accept( this );
		}

		if ( node.getPivotInClause( ) != null )
		{
			node.getPivotInClause( ).accept( this );
		}

		if ( node.getUnpivotInClause( ) != null )
		{
			node.getUnpivotInClause( ).accept( this );
		}
	}

	public void preVisit( TTable node )
	{
		switch ( node.getTableType( ) )
		{
			case objectname :
			{
				node.getTableName( ).accept( this );
				if ( node.getAliasClause( ) != null )
				{
					node.getAliasClause( ).accept( this );
				}
				break;
			}
			case tableExpr :
			{
				node.getTableExpr( ).accept( this );

				if ( node.getAliasClause( ) != null )
				{
					node.getAliasClause( ).accept( this );
				}
				break;
			}
			case subquery :
			{
				node.getSubquery( ).accept( this );
				if ( node.getAliasClause( ) != null )
				{
					node.getAliasClause( ).accept( this );
				}
				convertPoints.add( new Table( node ) );
				break;
			}
			case function :
			{
				node.getFuncCall( ).accept( this );
				if ( node.getAliasClause( ) != null )
				{
					node.getAliasClause( ).accept( this );
				}
				break;
			}
			case pivoted_table :
			{
				node.getPivotedTable( ).accept( this );
				break;
			}
			case output_merge :
			{
				node.getOutputMerge( ).accept( this );
				break;
			}
			case containsTable :
			{
				node.getContainsTable( ).accept( this );
				break;
			}
			case openrowset :
			{
				node.getOpenRowSet( ).accept( this );
				break;
			}
			case openxml :
			{
				node.getOpenXML( ).accept( this );
				break;
			}
			case opendatasource :
			{
				node.getOpenDatasource( ).accept( this );
				break;
			}
			case openquery :
			{
				node.getOpenquery( ).accept( this );
				break;
			}
			case datachangeTable :
			{
				node.getDatachangeTable( ).accept( this );
				break;
			}
			case rowList :
			{
				node.getRowList( ).accept( this );
				break;
			}
			case xmltable :
			{
				node.getXmlTable( ).accept( this );
				break;
			}
			case informixOuter :
			{
				preVisit( node.getOuterClause( ) );
				break;
			}
			case table_ref_list :
			{
				node.getFromTableList( ).accept( this );
				break;
			}
			case hiveFromQuery :
			{
				node.getHiveFromQuery( ).accept( this );
				break;
			}
			default :
				break;

		}

		if ( node.getTableHintList( ) != null )
		{
			for ( int i = 0; i < node.getTableHintList( ).size( ); i++ )
			{
				TTableHint tableHint = node.getTableHintList( ).getElement( i );
				tableHint.accept( this );
			}
		}
	}

	public void preVisit( TInformixOuterClause node )
	{
		System.out.println( node );
	}

	public void preVisit( TTableHint node )
	{
	}

	public void preVisit( TObjectName node )
	{

	}

	public void preVisit( TJoinList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getJoin( i ).accept( this );
		}
	}

	public void preVisit( TObjectNameList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getObjectName( i ).accept( this );
		}
	}

	public void preVisit( TWhereClause node )
	{
		node.getCondition( ).accept( this );
	}

	public void preVisit( THierarchical node )
	{
		if ( node.getConnectByList( ) != null )
		{
			for ( int i = 0; i < node.getConnectByList( ).size( ); i++ )
			{
				node.getConnectByList( ).getElement( i ).accept( this );
			}
		}

		if ( node.getStartWithClause( ) != null )
		{
			node.getStartWithClause( ).accept( this );
		}
	}

	public void preVisit( TConnectByClause node )
	{
		node.getCondition( ).accept( this );
	}

	public void preVisit( TRollupCube node )
	{
		node.getItems( ).accept( this );
	}

	public void preVisit( TGroupBy node )
	{
		if ( node.getItems( ) != null )
		{
			node.getItems( ).accept( this );
		}
		if ( node.getHavingClause( ) != null )
		{
			node.getHavingClause( ).accept( this );
		}
	}

	public void preVisit( TGroupByItem node )
	{
		if ( node.getExpr( ) != null )
		{
			TExpression ge = node.getExpr( );
			ge.accept( this );
		}
		else if ( node.getGroupingSet( ) != null )
		{
			node.getGroupingSet( ).accept( this );
		}
		else if ( node.getRollupCube( ) != null )
		{
			node.getRollupCube( ).accept( this );
		}
	}

	public void preVisit( TGroupingSet node )
	{
		for ( int i = 0; i < node.getItems( ).size( ); i++ )
		{
			if ( node.getItems( )
					.getGroupingSetItem( i )
					.getGrouping_expression( ) != null )
			{
				TExpression ge = node.getItems( )
						.getGroupingSetItem( i )
						.getGrouping_expression( );
				ge.accept( this );
			}
			else if ( node.getItems( )
					.getGroupingSetItem( i )
					.getRollupCubeClause( ) != null )
			{
				TRollupCube rollupCube = node.getItems( )
						.getGroupingSetItem( i )
						.getRollupCubeClause( );
				rollupCube.accept( this );
			}
		}
	}

	public void preVisit( TGroupByItemList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getGroupByItem( i ).accept( this );
		}
	}

	public void preVisit( TOrderBy node )
	{
		node.getItems( ).accept( this );
	}

	public void preVisit( TOrderByItem node )
	{
		if ( node.getSortKey( ) != null )
		{
			node.getSortKey( ).accept( this );
		}
	}

	public void preVisit( TOrderByItemList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getOrderByItem( i ).accept( this );
		}
	}

	public void preVisit( TForUpdate node )
	{
	}

	public void preVisit( TStatementList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.get( i ).accept( this );
		}
	}

	public void preVisit( TPlsqlCreatePackage node )
	{
		if ( node.getEndlabelName( ) != null )
		{
			node.getEndlabelName( ).accept( this );
		}
		if ( node.getDeclareStatements( ).size( ) > 0 )
		{
			node.getDeclareStatements( ).accept( this );
		}
	}

	public void preVisit( TMssqlCreateFunction node )
	{
		node.getFunctionName( ).accept( this );
		if ( node.getEndlabelName( ) != null )
		{
			node.getEndlabelName( ).accept( this );
		}

		if ( node.getReturnDataType( ) != null )
		{
			node.getReturnDataType( ).accept( this );
		}

		if ( node.getParameterDeclarations( ) != null )
		{
			node.getParameterDeclarations( ).accept( this );
		}

		if ( node.getBodyStatements( ).size( ) > 0 )
		{
			node.getBodyStatements( ).accept( this );
		}
	}

	public void preVisit( TCreateDatabaseSqlStatement stmt )
	{
		stmt.getDatabaseName( ).accept( this );
	}

	public void preVisit( TCreateSchemaSqlStatement stmt )
	{
		stmt.getSchemaName( ).accept( this );
	}

	public void preVisit( TUseDatabase stmt )
	{
		stmt.getDatabaseName( ).accept( this );
	}

	public void preVisit( TMssqlBlock node )
	{
		if ( node.getBodyStatements( ).size( ) > 0 )
		{
			node.getBodyStatements( ).accept( this );
		}
	}

	private void doProcedureSpecification( TPlsqlCreateProcedure node )
	{
		node.getProcedureName( ).accept( this );

		if ( node.getEndlabelName( ) != null )
		{
			node.getEndlabelName( ).accept( this );
		}

		if ( node.getParameterDeclarations( ) != null )
			node.getParameterDeclarations( ).accept( this );
		if ( node.getInnerStatements( ).size( ) > 0 )
			node.getInnerStatements( ).accept( this );
		if ( node.getDeclareStatements( ).size( ) > 0 )
		{
			node.getDeclareStatements( ).accept( this );
		}

		if ( node.getBodyStatements( ).size( ) > 0 )
		{
			node.getBodyStatements( ).accept( this );
		}

		if ( node.getExceptionClause( ) != null )
			node.getExceptionClause( ).accept( this );
	}

	private void doFunctionSpecification( TPlsqlCreateFunction node )
	{
		if ( node.isWrapped( ) )
			return;
		node.getFunctionName( ).accept( this );
		if ( node.getEndlabelName( ) != null )
		{
			node.getEndlabelName( ).accept( this );
		}
		node.getReturnDataType( ).accept( this );

		if ( node.getParameterDeclarations( ) != null )
		{
			node.getParameterDeclarations( ).accept( this );
		}
		if ( node.getDeclareStatements( ).size( ) > 0 )
		{
			node.getDeclareStatements( ).accept( this );
		}
		if ( node.getBodyStatements( ).size( ) > 0 )
		{
			node.getBodyStatements( ).accept( this );
		}
		if ( node.getExceptionClause( ) != null )
			node.getExceptionClause( ).accept( this );
	}

	public void preVisit( TPlsqlCreateFunction node )
	{
		switch ( node.getKind( ) )
		{
			case TBaseType.kind_create :
				doFunctionSpecification( node );
				break;
			case TBaseType.kind_declare :
				node.getFunctionName( ).accept( this );
				if ( node.getParameterDeclarations( ) != null )
					node.getParameterDeclarations( ).accept( this );
				break;
			case TBaseType.kind_define :
				doFunctionSpecification( node );
				break;
		}
	}

	public void preVisit( TCommonBlock node )
	{
		if ( node.getLabelName( ) != null )
		{
			node.getLabelName( ).accept( this );
		}
		if ( node.getDeclareStatements( ).size( ) > 0 )
			node.getDeclareStatements( ).accept( this );
		if ( node.getBodyStatements( ).size( ) > 0 )
			node.getBodyStatements( ).accept( this );

		if ( node.getExceptionClause( ) != null )
			node.getExceptionClause( ).accept( this );

	}

	public void preVisit( TExceptionClause node )
	{
		node.getHandlers( ).accept( this );
	}

	public void preVisit( TExceptionHandler node )
	{
		node.getExceptionNames( ).accept( this );
		node.getStatements( ).accept( this );
	}

	public void preVisit( TExceptionHandlerList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getExceptionHandler( i ).accept( this );
		}

	}

	public void preVisit( TAlterTableOption node )
	{
		switch ( node.getOptionType( ) )
		{
			case AddColumn :
				node.getColumnDefinitionList( ).accept( this );
				break;
			case AlterColumn :
				node.getColumnName( ).accept( this );
				break;
			case ChangeColumn :
				node.getColumnName( ).accept( this );
				break;
			case DropColumn :
				for ( int i = 0; i < node.getColumnNameList( ).size( ); i++ )
				{
					node.getColumnNameList( ).getObjectName( i ).accept( this );
				}
				break;
			case ModifyColumn :
				node.getColumnDefinitionList( ).accept( this );
				break;
			case RenameColumn :
				node.getColumnName( ).accept( this );
				node.getNewColumnName( ).accept( this );
				break;
			case AddConstraint :
				node.getConstraintList( ).accept( this );
				break;
			case switchPartition :
				node.getNewTableName( ).accept( this );
				if ( node.getPartitionExpression1( ) != null )
				{
					node.getPartitionExpression1( ).accept( this );
				}
				if ( node.getPartitionExpression2( ) != null )
				{
					node.getPartitionExpression2( ).accept( this );
				}
				break;
			default :

		}
	}

	public void preVisit( TAlterTableStatement stmt )
	{
		stmt.getTableName( ).accept( this );

		if ( stmt.getAlterTableOptionList( ) != null )
		{
			for ( int i = 0; i < stmt.getAlterTableOptionList( ).size( ); i++ )
			{
				stmt.getAlterTableOptionList( )
						.getAlterTableOption( i )
						.accept( this );
			}
		}

	}

	public void preVisit( TTypeName node )
	{

	}

	public void preVisit( TColumnDefinition node )
	{
		node.getColumnName( ).accept( this );
		if ( node.getDatatype( ) != null )
		{
			node.getDatatype( ).accept( this );
		}

		if ( ( node.getConstraints( ) != null )
				&& ( node.getConstraints( ).size( ) > 0 ) )
		{
			node.getConstraints( ).accept( this );
		}
	}

	public void preVisit( TColumnDefinitionList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getColumn( i ).accept( this );
		}
	}

	public void preVisit( TMergeWhenClause node )
	{
		if ( node.getCondition( ) != null )
		{
			node.getCondition( ).accept( this );
		}

		if ( node.getUpdateClause( ) != null )
		{
			node.getUpdateClause( ).accept( this );
		}

		if ( node.getInsertClause( ) != null )
		{
			node.getInsertClause( ).accept( this );
		}

		if ( node.getDeleteClause( ) != null )
		{
			node.getDeleteClause( ).accept( this );
		}

	}

	public void preVisit( TMergeUpdateClause node )
	{

		if ( node.getUpdateColumnList( ) != null )
		{
			// node.getUpdateColumnList().accept(this);
			for ( int i = 0; i < node.getUpdateColumnList( ).size( ); i++ )
			{
				node.getUpdateColumnList( )
						.getResultColumn( i )
						.getExpr( )
						.accept( this );
			}
		}

		if ( node.getUpdateWhereClause( ) != null )
		{
			node.getUpdateWhereClause( ).accept( this );
		}

		if ( node.getDeleteWhereClause( ) != null )
		{
			node.getDeleteWhereClause( ).accept( this );
		}

	}

	public void preVisit( TMergeInsertClause node )
	{
		if ( node.getColumnList( ) != null )
		{
			node.getColumnList( ).accept( this );
		}

		if ( node.getValuelist( ) != null )
		{
			for ( int i = 0; i < node.getValuelist( ).size( ); i++ )
			{
				node.getValuelist( )
						.getResultColumn( i )
						.getExpr( )
						.accept( this );
			}
		}

		if ( node.getInsertWhereClause( ) != null )
		{
			node.getInsertWhereClause( ).accept( this );
		}
	}

	public void preVisit( TMergeDeleteClause node )
	{
	}

	public void preVisit( TConstraint node )
	{
		switch ( node.getConstraint_type( ) )
		{
			case notnull :
				break;
			case unique :
				break;
			case check :
				if ( node.getCheckCondition( ) != null )
				{
					node.getCheckCondition( ).accept( this );
				}
				break;
			case primary_key :
				if ( node.getColumnList( ) != null )
				{
					node.getColumnList( ).accept( this );
				}
				break;
			case foreign_key :
			case reference :
				if ( node.getColumnList( ) != null )
				{
					node.getColumnList( ).accept( this );
				}
				if ( node.getReferencedObject( ) != null )
				{
					node.getReferencedObject( ).accept( this );
				}
				if ( node.getReferencedColumnList( ) != null )
				{
					node.getReferencedColumnList( ).accept( this );
				}
				break;
			case default_value :
				node.getDefaultExpression( ).accept( this );
				break;
			default :
				break;
		}
	}

	public void preVisit( TConstraintList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getConstraint( i ).accept( this );
		}
	}

	public void preVisit( TCreateMaterializedSqlStatement stmt )
	{
		stmt.getViewName( ).accept( this );

		if ( stmt.getViewAliasClause( ) != null )
		{
			for ( int i = 0; i < stmt.getViewAliasClause( )
					.getViewAliasItemList( )
					.size( ); i++ )
			{
				TViewAliasItem viewAliasItem = stmt.getViewAliasClause( )
						.getViewAliasItemList( )
						.getViewAliasItem( i );
				if ( viewAliasItem.getAlias( ) == null )
					continue;
				viewAliasItem.getAlias( ).accept( this );
			}
		}
		stmt.getSubquery( ).accept( this );
	}

	public void preVisit( TCreateViewSqlStatement stmt )
	{
		if ( stmt.getViewAttributeList( ) != null )
		{
			for ( int i = 0; i < stmt.getViewAttributeList( ).size( ); i++ )
			{
				stmt.getViewAttributeList( ).getObjectName( i ).accept( this );;
			}
		}
		stmt.getViewName( ).accept( this );

		if ( stmt.getViewAliasClause( ) != null )
		{
			for ( int i = 0; i < stmt.getViewAliasClause( )
					.getViewAliasItemList( )
					.size( ); i++ )
			{
				TViewAliasItem viewAliasItem = stmt.getViewAliasClause( )
						.getViewAliasItemList( )
						.getViewAliasItem( i );
				if ( viewAliasItem.getAlias( ) == null )
					continue;
				viewAliasItem.getAlias( ).accept( this );
			}
		}
		stmt.getSubquery( ).accept( this );
	}

	public void postVisit( TCreateViewSqlStatement stmt )
	{

	}

	public void preVisit( TMssqlCreateTrigger stmt )
	{
		stmt.getTriggerName( ).accept( this );
		stmt.getOnTable( ).accept( this );
		if ( stmt.getBodyStatements( ).size( ) > 0 )
			stmt.getBodyStatements( ).accept( this );
	}

	public void preVisit( TCreateSequenceStmt stmt )
	{
		stmt.getSequenceName( ).accept( this );
		if ( stmt.getOptions( ) != null )
		{
			for ( int i = 0; i < stmt.getOptions( ).size( ); i++ )
			{
				TSequenceOption sequenceOption = stmt.getOptions( )
						.getElement( i );
				switch ( sequenceOption.getSequenceOptionType( ) )
				{
					case start :
					case startWith :
						break;
					case restart :
					case restartWith :
						break;
					case increment :
					case incrementBy :
						break;
					case minValue :
						break;
					case maxValue :
						break;
					case cycle :
						break;
					case noCycle :
						break;
					case cache :
						break;
					case noCache :
						break;
					case order :
						break;
					case noOrder :
						break;
					default :
						break;
				}

			}
		}
	}

	public void preVisit( TCreateSynonymStmt stmt )
	{
		stmt.getSynonymName( ).accept( this );
		stmt.getForName( ).accept( this );
	}

	public void preVisit( TExecParameter node )
	{
		if ( node.getParameterName( ) != null )
		{
			node.getParameterName( ).accept( this );
		}
		node.getParameterValue( ).accept( this );
	}

	public void preVisit( TMssqlExecute stmt )
	{
		switch ( stmt.getExecType( ) )
		{
			case TBaseType.metExecSp :
				stmt.getModuleName( ).accept( this );
				if ( stmt.getParameters( ) != null )
				{
					for ( int i = 0; i < stmt.getParameters( ).size( ); i++ )
					{
						stmt.getParameters( )
								.getExecParameter( i )
								.accept( this );
					}
				}
				break;
			default :
				break;
		}
	}

	public void preVisit( TMssqlDeclare stmt )
	{
		switch ( stmt.getDeclareType( ) )
		{
			case variable :
				if ( stmt.getDeclareType( ) == EDeclareType.variable )
				{
					stmt.getVariables( ).accept( this );
				}
				break;
			default :
				break;
		}
	}

	public void preVisit( TMssqlSet stmt )
	{
		switch ( stmt.getSetType( ) )
		{
			case TBaseType.mstUnknown :
				break;
			case TBaseType.mstLocalVar :
				stmt.getVarName( ).accept( this );
				stmt.getVarExpr( ).accept( this );
				break;
			case TBaseType.mstLocalVarCursor :
				break;
			case TBaseType.mstSetCmd :
				break;
			case TBaseType.mstXmlMethod :
				break;
			case TBaseType.mstSybaseLocalVar :
				break;
			default :
				break;

		}
	}

	public void preVisit( TMergeSqlStatement stmt )
	{

		if ( stmt.getCteList( ) != null )
		{
			stmt.getCteList( ).accept( this );
		}

		stmt.getTargetTable( ).accept( this );

		stmt.getUsingTable( ).accept( this );

		stmt.getCondition( ).accept( this );

		if ( stmt.getWhenClauses( ) != null )
		{
			for ( int i = 0; i < stmt.getWhenClauses( ).size( ); i++ )
			{
				TMergeWhenClause whenClause = stmt.getWhenClauses( )
						.getElement( i );
				whenClause.accept( this );
			}
		}
	}

	public void preVisit( TCreateIndexSqlStatement stmt )
	{
		if ( stmt.getIndexName( ) != null )
		{
			stmt.getIndexName( ).accept( this );
		}

		stmt.getTableName( ).accept( this );

		for ( int i = 0; i < stmt.getColumnNameList( ).size( ); i++ )
		{
			TOrderByItem orderByItem = stmt.getColumnNameList( )
					.getOrderByItem( i );
			orderByItem.getSortKey( ).accept( this );
		}
	}

	public void preVisit( TCreateTableSqlStatement stmt )
	{
		stmt.getTargetTable( ).accept( this );

		if ( stmt.getColumnList( ).size( ) > 0 )
		{
			stmt.getColumnList( ).accept( this );
		}

		if ( ( stmt.getTableConstraints( ) != null )
				&& ( stmt.getTableConstraints( ).size( ) > 0 ) )
		{
			stmt.getTableConstraints( ).accept( this );
		}

		if ( stmt.getSubQuery( ) != null )
		{
			stmt.getSubQuery( ).accept( this );
		}
	}

	public void preVisit( TDropIndexSqlStatement stmt )
	{
	}

	public void preVisit( TDropTableSqlStatement stmt )
	{
		stmt.getTableName( ).accept( this );
	}

	public void preVisit( TTruncateStatement stmt )
	{
		stmt.getTableName( ).accept( this );
	}

	public void preVisit( TDropViewSqlStatement stmt )
	{
		stmt.getViewName( ).accept( this );;
	}

	public void preVisit( TDeleteSqlStatement stmt )
	{

		if ( stmt.getCteList( ) != null )
		{
			stmt.getCteList( ).accept( this );
		}

		if ( stmt.getTopClause( ) != null )
		{
			stmt.getTopClause( ).accept( this );
		}

		stmt.getTargetTable( ).accept( this );

		if ( stmt.joins.size( ) > 0 )
		{
			stmt.joins.accept( this );
		}

		if ( stmt.getOutputClause( ) != null )
		{
			stmt.getOutputClause( ).accept( this );
		}

		if ( stmt.getWhereClause( ) != null )
		{
			stmt.getWhereClause( ).accept( this );
		}

		if ( stmt.getReturningClause( ) != null )
		{
			stmt.getReturningClause( ).accept( this );
		}

	}

	public void postVisit( TDeleteSqlStatement stmt )
	{

	}

	public void preVisit( TUpdateSqlStatement stmt )
	{
		if ( stmt.getCteList( ) != null )
		{
			stmt.getCteList( ).accept( this );
		}

		if ( stmt.getTopClause( ) != null )
		{
			stmt.getTopClause( ).accept( this );
		}

		stmt.getTargetTable( ).accept( this );

		for ( int i = 0; i < stmt.getResultColumnList( ).size( ); i++ )
		{
			stmt.getResultColumnList( )
					.getResultColumn( i )
					.getExpr( )
					.accept( this );
		}

		if ( stmt.joins.size( ) > 0 )
		{
			stmt.joins.accept( this );
		}

		if ( stmt.getWhereClause( ) != null )
		{
			stmt.getWhereClause( ).accept( this );
		}

		if ( stmt.getOrderByClause( ) != null )
		{
			stmt.getOrderByClause( ).accept( this );
		}

		if ( stmt.getLimitClause( ) != null )
		{
			stmt.getLimitClause( ).accept( this );
		}

		if ( stmt.getOutputClause( ) != null )
		{
			stmt.getOutputClause( ).accept( this );
		}

		if ( stmt.getReturningClause( ) != null )
		{
			stmt.getReturningClause( ).accept( this );
		}
	}

	public void preVisit( TWithinGroup withinGroup )
	{
		withinGroup.getOrderBy( ).accept( this );
	}

	public void preVisit( TKeepDenseRankClause keepDenseRankClause )
	{
		keepDenseRankClause.getOrderBy( ).accept( this );
	}

	public void preVisit( TPlsqlCreateProcedure node )
	{

		switch ( node.getKind( ) )
		{
			case TBaseType.kind_define :
				doProcedureSpecification( node );
				break;
			case TBaseType.kind_declare :
				node.getProcedureName( ).accept( this );
				if ( node.getParameterDeclarations( ) != null )
					node.getParameterDeclarations( ).accept( this );

				break;
			case TBaseType.kind_create :
				doProcedureSpecification( node );
				break;
		}
	}

	public void preVisit( TWindowDef windowDef )
	{
		if ( windowDef.getWithinGroup( ) != null )
		{
			windowDef.getWithinGroup( ).accept( this );
		}

		if ( windowDef.getKeepDenseRankClause( ) != null )
		{
			windowDef.getKeepDenseRankClause( ).accept( this );
		}

		if ( windowDef.isIncludingOverClause( ) )
		{
			if ( windowDef.getPartitionClause( ) != null )
			{
				windowDef.getPartitionClause( )
						.getExpressionList( )
						.accept( this );
			}

			if ( windowDef.getOrderBy( ) != null )
			{
				windowDef.getOrderBy( ).accept( this );
			}

			if ( windowDef.getWindowFrame( ) != null )
			{
				TWindowFrame windowFrame = windowDef.getWindowFrame( );
				windowFrame.getStartBoundary( ).accept( this );
				if ( windowFrame.getEndBoundary( ) != null )
				{
					windowFrame.getEndBoundary( ).accept( this );
				}
			}
		}
	}

	public void preVisit( TFunctionCall node )
	{
		node.getFunctionName( ).accept( this );
		switch ( node.getFunctionType( ) )
		{
			case unknown_t :
				if ( node.getArgs( ) != null )
				{
					node.getArgs( ).accept( this );
				}
				break;
			case udf_t :
			case case_n_t :
			case chr_t :
				if ( node.getArgs( ) != null )
				{
					node.getArgs( ).accept( this );
				}
				if ( node.getAnalyticFunction( ) != null )
				{
					node.getAnalyticFunction( ).accept( this );
				}
				break;
			case cast_t :
				node.getExpr1( ).accept( this );
				node.getTypename( ).accept( this );
				break;
			case convert_t :
				if ( node.getTypename( ) != null )
				{
					node.getTypename( ).accept( this );
				}
				node.getParameter( ).accept( this );
				break;
			case trim_t :
				if ( node.getTrimArgument( ) != null )
				{
					TTrimArgument trimArgument = node.getTrimArgument( );
					if ( trimArgument.getTrimCharacter( ) != null )
					{
						trimArgument.getTrimCharacter( ).accept( this );
					}
					trimArgument.getStringExpression( ).accept( this );
				}
				break;
			case extract_t :
				if ( node.getArgs( ) != null )
				{
					node.getArgs( ).accept( this );
				}
				else
				{
					if ( node.getExpr1( ) != null )
					{
						node.getExpr1( ).accept( this );
					}
				}
				break;
			case treat_t :
				node.getExpr1( ).accept( this );
				node.getTypename( ).accept( this );
				break;
			case contains_t :
				node.getExpr1( ).accept( this );
				node.getExpr2( ).accept( this );
				break;
			case freetext_t :
				node.getExpr1( ).accept( this );
				node.getExpr2( ).accept( this );
				break;
			case substring_t :
				node.getExpr1( ).accept( this );
				if ( node.getExpr2( ) != null )
				{
					node.getExpr2( ).accept( this );
				}
				if ( node.getExpr3( ) != null )
				{
					node.getExpr3( ).accept( this );
				}
				break;
			case range_n_t :
			case position_t :
			case xmlquery_t :
			case xmlcast_t :
			case match_against_t :
			case adddate_t :
			case date_add_t :
			case subdate_t :
			case date_sub_t :
			case timestampadd_t :
			case timestampdiff_t :
				break;
			default :
				if ( node.getArgs( ) != null )
				{
					node.getArgs( ).accept( this );
				}
				break;
		}

		if ( node.getWindowDef( ) != null )
		{
			node.getWindowDef( ).accept( this );
		}
	}

	public void preVisit( TWindowFrameBoundary boundary )
	{
	}

	public void preVisit( TInsertSqlStatement stmt )
	{
		if ( stmt.getCteList( ) != null )
		{
			stmt.getCteList( ).accept( this );
		}

		if ( stmt.getTargetTable( ) != null )
		{
			stmt.getTargetTable( ).accept( this );
		}

		if ( stmt.getColumnList( ) != null )
		{
			stmt.getColumnList( ).accept( this );
		}

		switch ( stmt.getInsertSource( ) )
		{
			case values :
				for ( int i = 0; i < stmt.getValues( ).size( ); i++ )
				{
					TMultiTarget multiTarget = stmt.getValues( )
							.getMultiTarget( i );

					for ( int j = 0; j < multiTarget.getColumnList( ).size( ); j++ )
					{
						if ( multiTarget.getColumnList( )
								.getResultColumn( j )
								.isPlaceHolder( ) )
							continue; // teradata allow empty value
						multiTarget.getColumnList( )
								.getResultColumn( j )
								.getExpr( )
								.accept( this );
					}

				}
				break;
			case subquery :
				stmt.getSubQuery( ).accept( this );
				break;
			case values_empty :
				break;
			case values_function :
				stmt.getFunctionCall( ).accept( this );
				break;
			case values_oracle_record :
				stmt.getRecordName( ).accept( this );
				break;
			case set_column_value :
				stmt.getSetColumnValues( ).accept( this );
				break;
			case execute :
				stmt.getExecuteStmt( ).accept( this );
				break;
			default :
				break;
		}

		if ( stmt.getReturningClause( ) != null )
		{
			stmt.getReturningClause( ).accept( this );
		}
	}

	public void preVisit( TMultiTarget node )
	{
		if ( node.getColumnList( ) != null )
		{
			node.getColumnList( ).accept( this );
		}

		if ( node.getSubQuery( ) != null )
		{
			node.getSubQuery( ).accept( this );
		}
	}

	public void preVisit( TMultiTargetList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getMultiTarget( i ).accept( this );
		}
	}

	public void preVisit( TCTE node )
	{
		node.getTableName( ).accept( this );

		if ( node.getColumnList( ) != null )
		{
			node.getColumnList( ).accept( this );
		}
		if ( node.getSubquery( ) != null )
		{
			node.getSubquery( ).accept( this );
		}
		else if ( node.getUpdateStmt( ) != null )
		{
			node.getUpdateStmt( ).accept( this );
		}
		else if ( node.getInsertStmt( ) != null )
		{
			node.getInsertStmt( ).accept( this );
		}
		else if ( node.getDeleteStmt( ) != null )
		{
			node.getDeleteStmt( ).accept( this );
		}
	}

	public void preVisit( TCTEList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getCTE( i ).accept( this );
		}
	}

	public void preVisit( TAssignStmt node )
	{
		node.getLeft( ).accept( this );
		node.getExpression( ).accept( this );
	}

	public void preVisit( TMssqlCreateXmlSchemaCollectionStmt stmt )
	{
		stmt.getSchemaName( ).accept( this );
		stmt.getExpr( ).accept( this );
	}

	public void preVisit( TIfStmt node )
	{
		node.getCondition( ).accept( this );
		node.getThenStatements( ).accept( this );
		if ( node.getElseifStatements( ).size( ) > 0 )
		{
			for ( int i = 0; i < node.getElseifStatements( ).size( ); i++ )
			{
				TElsifStmt elsifStmt = (TElsifStmt) node.getElseifStatements( )
						.get( i );

				elsifStmt.getCondition( ).accept( this );
				elsifStmt.getThenStatements( ).accept( this );
			}
		}
		if ( node.getElseStatements( ).size( ) > 0 )
		{
			node.getElseStatements( ).accept( this );
		}
	}

	public void preVisit( TMssqlIfElse node )
	{
		if ( node.getCondition( ) != null )
		{
			node.getCondition( ).accept( this );
		}

		node.getStmt( ).accept( this );
	}

	public void preVisit( TBasicStmt node )
	{
		node.getExpr( ).accept( this );
	}

	public void preVisit( TCaseStmt node )
	{
	}

	public void preVisit( TCaseExpression node )
	{
		if ( node.getInput_expr( ) != null )
		{
			node.getInput_expr( ).accept( this );
		}
		for ( int i = 0; i < node.getWhenClauseItemList( ).size( ); i++ )
		{
			node.getWhenClauseItemList( )
					.getWhenClauseItem( i )
					.getComparison_expr( )
					.accept( this );
			node.getWhenClauseItemList( )
					.getWhenClauseItem( i )
					.getReturn_expr( )
					.accept( this );
		}

		if ( node.getElse_expr( ) != null )
		{
			node.getElse_expr( ).accept( this );
		}

		if ( node.getElse_statement_list( ).size( ) > 0 )
		{
			node.getElse_statement_list( ).accept( this );
		}
	}

	public void preVisit( TWhenClauseItemList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getWhenClauseItem( i ).accept( this );
		}
	}

	public void preVisit( TWhenClauseItem node )
	{
		node.getComparison_expr( ).accept( this );
		if ( node.getReturn_expr( ) != null )
		{
			node.getReturn_expr( ).accept( this );
		}
		else if ( node.getStatement_list( ).size( ) > 0 )
		{
			node.getStatement_list( ).accept( this );
		}
	}

	public void preVisit( TCloseStmt node )
	{
		node.getCursorName( ).accept( this );
	}

	public void preVisit( TPlsqlCreateTrigger stmt )
	{
		stmt.getTriggerName( ).accept( this );
		stmt.getTriggerBody( ).accept( this );
	}

	public void preVisit( TTypeAttribute node )
	{
		node.getAttributeName( ).accept( this );
		node.getDatatype( ).accept( this );
	}

	public void preVisit( TTypeAttributeList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getAttributeItem( i ).accept( this );
		}
	}

	public void preVisit( TPlsqlCreateTypeBody stmt )
	{
		stmt.getTypeName( ).accept( this );
		stmt.getBodyStatements( ).accept( this );
	}

	public void preVisit( TPlsqlVarrayTypeDefStmt node )
	{
		node.getTypeName( ).accept( this );
		node.getElementDataType( ).accept( this );
	}

	public void preVisit( TPlsqlTableTypeDefStmt node )
	{
		node.getTypeName( ).accept( this );
		node.getElementDataType( ).accept( this );
	}

	public void preVisit( TPlsqlCreateType node )
	{
		node.getTypeName( ).accept( this );

		if ( node.getAttributes( ) != null )
		{
			for ( int i = 0; i < node.getAttributes( ).size( ); i++ )
			{
				node.getAttributes( )
						.getAttributeItem( i )
						.getAttributeName( )
						.accept( this );
				node.getAttributes( )
						.getAttributeItem( i )
						.getDatatype( )
						.accept( this );
			}
		}
	}

	public void preVisit( TPlsqlCreateType_Placeholder node )
	{
		TPlsqlCreateType createType = null;

		switch ( node.getCreatedType( ) )
		{
			case octIncomplete :
				createType = node.getObjectStatement( );
				createType.getTypeName( ).accept( this );
				break;
			case octObject :
				node.getObjectStatement( ).accept( this );
				break;
			case octNestedTable :
				node.getNestedTableStatement( ).accept( this );
				break;
			case octVarray :
				node.getVarrayStatement( ).accept( this );
				break;
			default :
				break;

		}
	}

	public void preVisit( TMssqlCommit node )
	{
	}

	public void preVisit( TMssqlRollback node )
	{

	}

	public void preVisit( TMssqlSaveTran node )
	{

	}

	public void postVisit( TMssqlSaveTran node )
	{

	}

	public void preVisit( TMssqlGo node )
	{
	}

	public void preVisit( TMssqlCreateProcedure node )
	{
		node.getProcedureName( ).accept( this );

		if ( node.getParameterDeclarations( ) != null )
			node.getParameterDeclarations( ).accept( this );

		if ( node.getBodyStatements( ).size( ) > 0 )
		{
			node.getBodyStatements( ).accept( this );
		}
	}

	public void preVisit( TParameterDeclarationList list )
	{
		for ( int i = 0; i < list.size( ); i++ )
		{
			list.getParameterDeclarationItem( i ).accept( this );
		}
	}

	public void preVisit( TParameterDeclaration node )
	{
		node.getParameterName( ).accept( this );
		node.getDataType( ).accept( this );
		if ( node.getDefaultValue( ) != null )
		{
			node.getDefaultValue( ).accept( this );
		}
	}

	public void preVisit( TDeclareVariable node )
	{
		node.getVariableName( ).accept( this );
		if ( node.getDatatype( ) != null )
			node.getDatatype( ).accept( this );

		if ( node.getDefaultValue( ) != null )
		{
			node.getDefaultValue( ).accept( this );
		}
	}

	public void preVisit( TDeclareVariableList node )
	{
		for ( int i = 0; i < node.size( ); i++ )
		{
			node.getDeclareVariable( i ).accept( this );
		}
	}

	public void preVisit( TVarDeclStmt node )
	{
		switch ( node.getDeclareType( ) )
		{
			case constant :
				node.getElementName( ).accept( this );
				node.getDataType( ).accept( this );
				node.getDefaultValue( ).accept( this );
				break;
			case variable :
				node.getElementName( ).accept( this );
				node.getDataType( ).accept( this );
				if ( node.getDefaultValue( ) != null )
				{
					node.getDefaultValue( ).accept( this );
				}
				break;
			case exception :
				node.getElementName( ).accept( this );
				break;
			case subtype :
				node.getElementName( ).accept( this );
				node.getDataType( ).accept( this );
				break;
			default :
				break;
		}
	}

	public void preVisit( TRaiseStmt node )
	{
		if ( node.getExceptionName( ) != null )
		{
			node.getExceptionName( ).accept( this );
		}
	}

	public void preVisit( TReturnStmt node )
	{
		if ( node.getExpression( ) != null )
		{
			node.getExpression( ).accept( this );
		}
	}

	public void preVisit( TMssqlReturn node )
	{
		if ( node.getReturnExpr( ) != null )
		{
			node.getReturnExpr( ).accept( this );
		}
	}

	public void preVisit( TPlsqlRecordTypeDefStmt stmt )
	{
		stmt.getTypeName( ).accept( this );

		for ( int i = 0; i < stmt.getFieldDeclarations( ).size( ); i++ )
		{
			stmt.getFieldDeclarations( )
					.getParameterDeclarationItem( i )
					.accept( this );
		}
	}

	public void preVisit( TSqlplusCmdStatement stmt )
	{

	}

	public void preVisit( TCursorDeclStmt stmt )
	{
		switch ( stmt.getKind( ) )
		{
			case TCursorDeclStmt.kind_ref_cursor_type_definition :
				stmt.getCursorTypeName( ).accept( this );
				if ( stmt.getRowtype( ) != null )
				{
					stmt.getRowtype( ).accept( this );
				}
				break;
			case TCursorDeclStmt.kind_cursor_declaration :
				stmt.getCursorName( ).accept( this );
				if ( stmt.getCursorParameterDeclarations( ) != null )
				{
					stmt.getCursorParameterDeclarations( ).accept( this );
				}
				if ( stmt.getRowtype( ) != null )
				{
					stmt.getRowtype( ).accept( this );
				}
				stmt.getSubquery( ).accept( this );
				break;
			default :
				break;
		}
	}

	public void preVisit( TLoopStmt stmt )
	{
		switch ( stmt.getKind( ) )
		{
			case TLoopStmt.basic_loop :
				break;
			case TLoopStmt.cursor_for_loop :
				break;
			case TLoopStmt.for_loop :
				break;
			case TLoopStmt.while_loop :
				break;
		}

		if ( stmt.getRecordName( ) != null )
		{
			stmt.getRecordName( ).accept( this );

			if ( stmt.getSubquery( ) != null )
			{
				stmt.getSubquery( ).accept( this );
			}
			else if ( stmt.getCursorName( ) != null )
			{
				stmt.getCursorName( ).accept( this );
				if ( stmt.getCursorParameterNames( ) != null )
				{
					stmt.getCursorParameterNames( ).accept( this );
				}
			}
		}
		if ( stmt.getCondition( ) != null )
		{
			stmt.getCondition( ).accept( this );
		}
		if ( stmt.getBodyStatements( ) != null )
		{
			stmt.getBodyStatements( ).accept( this );
		}
	}

	public void preVisit( TPlsqlContinue stmt )
	{
		if ( stmt.getLabelName( ) != null )
		{
			stmt.getLabelName( ).accept( this );
		}
		if ( stmt.getCondition( ) != null )
		{
			stmt.getCondition( ).accept( this );
		}

	}

	public void preVisit( TPlsqlExecImmeStmt stmt )
	{
		if ( stmt.getDynamicStringExpr( ) != null )
		{
			stmt.getDynamicStringExpr( ).accept( this );
		}

		if ( stmt.getDynamicStatements( ) != null )
		{
			stmt.getDynamicStatements( ).accept( this );
		}

	}

	public void preVisit( TExitStmt stmt )
	{
		if ( stmt.getExitlabelName( ) != null )
		{
			stmt.getExitlabelName( ).accept( this );
		}
		if ( stmt.getWhenCondition( ) != null )
		{
			stmt.getWhenCondition( ).accept( this );
		}
	}

	public void preVisit( TFetchStmt stmt )
	{
		stmt.getCursorName( ).accept( this );
		stmt.getVariableNames( ).accept( this );
	}

	public void preVisit( TPlsqlGotoStmt stmt )
	{
		stmt.getGotolabelName( ).accept( this );
	}

	public void preVisit( TPlsqlNullStmt stmt )
	{
	}

	public void preVisit( TCommentOnSqlStmt stmt )
	{
		stmt.getObjectName( ).accept( this );
	}

	public void preVisit( TOpenStmt stmt )
	{
		stmt.getCursorName( ).accept( this );
		if ( stmt.getCursorParameterNames( ) != null )
		{
			stmt.getCursorParameterNames( ).accept( this );
		}
	}

	public void preVisit( TOpenforStmt stmt )
	{
		stmt.getCursorVariableName( ).accept( this );
		if ( stmt.getSubquery( ) != null )
		{
			stmt.getSubquery( ).accept( this );
		}
	}

	public void preVisit( TPlsqlForallStmt stmt )
	{
		stmt.getIndexName( ).accept( this );

		if ( stmt.getLower_bound( ) != null )
		{
			stmt.getLower_bound( ).accept( this );
		}
		if ( stmt.getUpper_bound( ) != null )
		{
			stmt.getUpper_bound( ).accept( this );
		}
		if ( stmt.getCollectionName( ) != null )
		{
			stmt.getCollectionName( ).accept( this );
		}
		if ( stmt.getCollecitonNameExpr( ) != null )
		{
			stmt.getCollecitonNameExpr( ).accept( this );
		}

		stmt.getStatement( ).accept( this );
	}

	public void preVisit( TJoinItem node )
	{
		if ( node.getKind( ) == TBaseType.join_source_table )
		{
			node.getTable( ).accept( this );
		}
		else if ( node.getKind( ) == TBaseType.join_source_join )
		{
			node.getJoin( ).accept( this );
		}

		if ( node.getOnCondition( ) != null )
		{
			node.getOnCondition( ).accept( this );
		}

		if ( node.getUsingColumns( ) != null )
		{
			node.getUsingColumns( ).accept( this );
		}
	}

	public void preVisit( TExpression expr )
	{
		if ( expr.getLeftOperand( ) != null )
		{
			expr.getLeftOperand( ).accept( this );
		}

		if ( expr.getRightOperand( ) != null )
		{
			expr.getRightOperand( ).accept( this );
		}

		if ( expr.getSubQuery( ) != null )
		{
			expr.getSubQuery( ).accept( this );
		}

		if ( expr.getObjectOperand( ) != null )
		{
			expr.getObjectOperand( ).accept( this );
		}

		if ( expr.getExprList( ) != null )
		{
			expr.getExprList( ).accept( this );
		}

		if ( expr.getLikeEscapeOperand( ) != null )
		{
			expr.getLikeEscapeOperand( ).accept( this );
		}

		if ( expr.getBetweenOperand( ) != null )
		{
			expr.getBetweenOperand( ).accept( this );
		}

		if ( expr.getCaseExpression( ) != null )
		{
			expr.getCaseExpression( ).accept( this );
		}

		if ( expr.getFunctionCall( ) != null )
		{
			expr.getFunctionCall( ).accept( this );
		}
	}

	protected void handleDataType( )
	{
	}

	protected void handleFunction( )
	{
	}

	protected void handleIdentifier( )
	{
	}

	protected void handleJoin( )
	{
	}

	protected void handleKeyword( )
	{
	}
}
