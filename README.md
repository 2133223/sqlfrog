# sqlfrog
This is a tool that translates SQL dialect among various databases.

An online live demo is available to illustrate the feature of this tool.
http://107.170.101.241:8080/sqlfrog/

Document:
https://github.com/sqlparser/sqlfrog/wiki

## How this tool works

- Read and parse input SQL query, turn SQL text into a parse tree.
- Scan the generated parse tree node to find out SQL syntax that need to be converted from the source database to the target database.
- Modify the parse tree node to meet the syntax requirement of the target database.
- Rebuild the SQL query from the parse tree for the target database.
