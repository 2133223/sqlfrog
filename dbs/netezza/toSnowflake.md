### Netezza to Snowflake

#### datatype

netezza|snowflake|comment
---|---|---
ST_GEOMETRY|VARBINARY|hex_to_geometry() 
time with time zone(alias timetz)|TIMESTAMP_TZ|length is different 
interval (alias timespan)|int8|
numeric(6,0)||A time duration
numeric(8,0)||A date duration
numeric(14,0) numeric(15,1) numeric(16,2) numeric(17,3) numeric(18,4) numeric(19,5) numeric(20,6)||A timestamp duration
boolean (alias bool)|boolean|On or off need single quote in snowflake
rowid|N/A|
transaction ID|N/A|
dataslice|N/A|


#### built-in function
netezza|snowflake|comment
---|---|---
dceil()|ceil()
dfloor()|floor()|
n!|FACTORIAL(n)|
log(x)|log(10, x)|
random()|random()/10000000000000000000|
fpow(a,b)|pow(a,b)|
numeric_sqrt(x)|sqrt(x)|
intNand(x,y)|N/A|
intNor(x,y)|N/A|
intNxor(x,y)|N/A|
intNnot(x)|N/A|
intNshl(x,y [,z])|N/A|
intNshr(x,y [,z])|N/A|
btrim(s)|TRIM(s)|
btrim(s,t)|TRIM(s,t)|
INSTR(source-string,search-string)|REGEXP_INSTR( <subject> , <pattern> )|
INSTR(source-string,search-string,start)|REGEXP_INSTR( <subject> , <pattern>  , <position>)|
INSTR(source-string,search-string,start,instance)|REGEXP_INSTR( <subject> , <pattern>, <position>, <occurrence>)|
strpos(s,b)|POSITION( s IN b )|
unichr(n)|CHAR(n)|
unicodes(s,unit,base)|N/A|
encrypt(varchar text, varchar key [, int algorithm [, varchar IV]])|N/A|
decrypt(varchar text, varchar key [, int algorithm [, varchar IV]])|N/A|
encrypt_nvarchar(nvarchar text, nvarchar key [, int algorithm [, varchar IV]])|N/A|
encrypt_nvarchar(nvarchar text, nvarchar key )|ENCRYPT( <value_to_encrypt> , <passphrase> )|
decrypt_nvarchar(varchar text, nvarchar key [, int algorithm[, varchar IV]])|N/A|
decrypt_nvarchar(varchar text, nvarchar key)|DECRYPT( <value_to_encrypt> , <passphrase> )|
fpe_encrypt(bigint number, varchar(ANY) key, varchar(ANY) iv, bigint mask)|
fpe_decrypt(bigint number, varchar(ANY) key, varchar(ANY) iv, bigint mask)|
compress(varchar input[, int level])|N/A|
compress(nvarchar input[, int level])|N/A|
decompress(varchar input)|N/A|
decompress(nvarchar input)|N/A|
compress_nvarchar(nvarchar input[, int level])|N/A|
uuencode(varchar input)|N/A|
CURRENT_TIME()|CURRENT_TIME(0)|
days_between(timestamp t1, timestamp t2)|N/A|
hours_between(timestamp t1, timestamp t2)|N/A|
minutes_between(timestamp t1, timestamp t2)|N/A|
next_week(date input)|N/A|
next_month(date input)|N/A|
next_quarter(date input)|N/A|
next_year(date input)|N/A|
seconds_between(timestamp t1, timestamp t2)|N/A|
this_month(date input)|N/A|
this_week(date input)|N/A|
this_quarter(date input)|N/A|
this_year(date input)|N/A|
weeks_between()|N/A|
CURDATE()|N/A|
CURTIME()|N/A|
age(ts,ts)|N/A|
age(ts)|N/A|
overlaps(a,b,c,d) |N/A|
duration_add(a,b) |N/A|
duration_subtract(a,b) |N/A|
timeofday()|N/A|
timezone(timestamp from_tz to_tz) |N/A|
now()|sysdate()|





