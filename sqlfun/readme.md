This document explains the structure used to document a database function in the JSON format.

### funcName
Name of the function. Function has the same name but has the different arguments syntax will be documented
in a different file. 

funcName can't be null.

### arguments

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