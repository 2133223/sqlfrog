This document explains the structure used to document a database function in the JSON format.
The property suffixed with * is required in the json document.

Under the database vendor directory, each json file describes the syntax of a function.

### funcName*
Name of the function. Function has the same name but has the different arguments syntax will be documented
in a different file. 

### arguments*
Array of the argument object. This list is empty when a function has no argument.

### argument*
Function argument includes the following properties.

* arguName*, argument name. 
* dataTypes, dataType of the argument, an argument may has more than one dataType, so this property is type of array.
* description, explanation of this argument.
* optional, is this argument optional in the argument list.
* repeated, is this argument should be repeated in the list.
* inoutMode, values are passed to a function in three modes; IN, OUT and INOUT.  
The mode which a variable is passed defines how the variables can be used inside the function. 

### clauses
A list of clause.

### clause
SQL clause used in the function, such as over clause in analytic function.
The clause includes the following properties:

* clauseName*, Name of this clause.
* clauseContext

```json
	"clause": {
		"clauseName": "over",
		"clauseContent": "[ partition_by_clause ] [order_by_clause] [ ROW_or_RANGE_clause ] ) "
	}
```

### returnType
Return dataType of this function.

### description
Explanation of this function.

### targetDBs
The database version this function is applied to. "+" after the version number means 
all version including and after this version.
```json
	"targetDBs": [
	  "2008+",
	  "Azure SQL Database",
	  "Azure SQL Warehouse",
	  "Parallel Data Warehouse"
	],
```

### category*
The category this function belongs to, such as string function.

### ansiCompatible
Compatible with ANSI SQL.

### package
The package this function belongs to, usually, it's Oracle plsql function.

### docLink
The online document for this function.

### sample
Sample SQL including this function.

This is an example of the json code that represents the ASCII function in SQL Server database.

```json
{
	"funcName": "ASCII",
	"arguments": [
	  {
		"argument": {
		  "arguName": "character_expression",
		  "dataTypes": [
			"char",
			"varchar"
		  ],
		  "description": "An expression of type char or varchar"
		}
	  }
	],
	"returnType": "int",
	"description": "Returns the ASCII code value of the leftmost character of a character expression",
	"targetDBs": [
	  "2008+",
	  "Azure SQL Database",
	  "Azure SQL Warehouse",
	  "Parallel Data Warehouse"
	],
	"category": "String",
	"ansiCompatible": "",
	"package": "",
	"docLink": "",
	"sample": "SELECT ASCII('A') AS A, ASCII('B') AS B,   \nASCII('a') AS a, ASCII('b') AS b,  \nASCII(1) AS [1], ASCII(2) AS [2];"
}
```