# csv-bulk-commands

## How to run

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=""
```

```
[INFO] Scanning for projects...
[INFO] 
[INFO] -------------------< com.example:csv-bulk-commands >--------------------
[INFO] Building csv-bulk-commands 0.0.1-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] >>> spring-boot-maven-plugin:2.5.3:run (default-cli) > test-compile @ csv-bulk-commands >>>
[INFO] 
[INFO] --- maven-resources-plugin:3.2.0:resources (default-resources) @ csv-bulk-commands ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Using 'UTF-8' encoding to copy filtered properties files.
[INFO] Copying 1 resource
[INFO] Copying 0 resource
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:compile (default-compile) @ csv-bulk-commands ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 6 source files to /Users/xxx/git-pub/csv-bulk-commands/target/classes
[INFO] 
[INFO] --- maven-resources-plugin:3.2.0:testResources (default-testResources) @ csv-bulk-commands ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Using 'UTF-8' encoding to copy filtered properties files.
[INFO] Copying 8 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ csv-bulk-commands ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 2 source files to /Users/xxx/git-pub/csv-bulk-commands/target/test-classes
[INFO] 
[INFO] <<< spring-boot-maven-plugin:2.5.3:run (default-cli) < test-compile @ csv-bulk-commands <<<
[INFO] 
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.5.3:run (default-cli) @ csv-bulk-commands ---
[INFO] Attaching agents: []

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v2.5.3)

2021-08-09 10:45:04.946  INFO 2798 --- [           main] c.e.tools.CsvBulkCommandsApplication     : Starting CsvBulkCommandsApplication using Java 11.0.1 on xxx with PID 2798 (/Users/xxx/git-pub/csv-bulk-commands/target/classes started by xxx in /Users/xxx/git-pub/csv-bulk-commands)
2021-08-09 10:45:04.949  INFO 2798 --- [           main] c.e.tools.CsvBulkCommandsApplication     : No active profile set, falling back to default profiles: default
2021-08-09 10:45:05.582  INFO 2798 --- [           main] c.e.tools.CsvBulkCommandsApplication     : Started CsvBulkCommandsApplication in 1.096 seconds (JVM running for 1.528)
2021-08-09 10:45:05.584  INFO 2798 --- [           main] HELP                                     : 
2021-08-09 10:45:05.584  INFO 2798 --- [           main] HELP                                     : [Arguments]
2021-08-09 10:45:05.584  INFO 2798 --- [           main] HELP                                     :   --command       : adding-columns, deleting-columns, updating-columns, ordering-columns
2021-08-09 10:45:05.584  INFO 2798 --- [           main] HELP                                     :   --dir           : target directory for apply command
2021-08-09 10:45:05.584  INFO 2798 --- [           main] HELP                                     :   --files         : target files for apply command
2021-08-09 10:45:05.584  INFO 2798 --- [           main] HELP                                     :   --column-names  : list of column name
2021-08-09 10:45:05.585  INFO 2798 --- [           main] HELP                                     :   --column-values : list of column value(can reference other column values using SpEL expression)
2021-08-09 10:45:05.585  INFO 2798 --- [           main] HELP                                     :   --encoding      : encoding for read/write file (default: UTF-8)
2021-08-09 10:45:05.585  INFO 2798 --- [           main] HELP                                     :   --h (--help)    : print help
2021-08-09 10:45:05.585  INFO 2798 --- [           main] HELP                                     : 
2021-08-09 10:45:05.585  INFO 2798 --- [           main] HELP                                     : [Usage: adding-columns]
2021-08-09 10:45:05.587  INFO 2798 --- [           main] HELP                                     :   Adding specified new column using column-names and column-values.
2021-08-09 10:45:05.587  INFO 2798 --- [           main] HELP                                     :   e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item10,item11 --column-values=1,NULL
2021-08-09 10:45:05.587  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.587  INFO 2798 --- [           main] HELP                                     :   item1,item2
2021-08-09 10:45:05.589  INFO 2798 --- [           main] HELP                                     :   001,test
2021-08-09 10:45:05.590  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.590  INFO 2798 --- [           main] HELP                                     :     ↓
2021-08-09 10:45:05.591  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.591  INFO 2798 --- [           main] HELP                                     :   item1,item2,item10,item11
2021-08-09 10:45:05.592  INFO 2798 --- [           main] HELP                                     :   001,test,1,NULL
2021-08-09 10:45:05.593  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.593  INFO 2798 --- [           main] HELP                                     : 
2021-08-09 10:45:05.593  INFO 2798 --- [           main] HELP                                     : [Usage: deleting-columns]
2021-08-09 10:45:05.593  INFO 2798 --- [           main] HELP                                     :   Deleting specified existing column using column-names.
2021-08-09 10:45:05.604  INFO 2798 --- [           main] HELP                                     :   e.g.) --command=deleting-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9
2021-08-09 10:45:05.604  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.604  INFO 2798 --- [           main] HELP                                     :   item1,item2,item8,item9
2021-08-09 10:45:05.604  INFO 2798 --- [           main] HELP                                     :   001,test,1,foo
2021-08-09 10:45:05.605  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.608  INFO 2798 --- [           main] HELP                                     :     ↓
2021-08-09 10:45:05.609  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.610  INFO 2798 --- [           main] HELP                                     :   item1,item8
2021-08-09 10:45:05.611  INFO 2798 --- [           main] HELP                                     :   001,1
2021-08-09 10:45:05.611  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.615  INFO 2798 --- [           main] HELP                                     : 
2021-08-09 10:45:05.615  INFO 2798 --- [           main] HELP                                     : [Usage: updating-columns]
2021-08-09 10:45:05.615  INFO 2798 --- [           main] HELP                                     :   Updating value specified existing column using column-names and column-values.
2021-08-09 10:45:05.615  INFO 2798 --- [           main] HELP                                     :   e.g.) --command=updating-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9 --column-values=test2,NULL
2021-08-09 10:45:05.619  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.619  INFO 2798 --- [           main] HELP                                     :   item1,item2,item8,item9
2021-08-09 10:45:05.619  INFO 2798 --- [           main] HELP                                     :   001,test,1,foo
2021-08-09 10:45:05.619  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.619  INFO 2798 --- [           main] HELP                                     :     ↓
2021-08-09 10:45:05.621  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.621  INFO 2798 --- [           main] HELP                                     :   item1,item2,item8,item9
2021-08-09 10:45:05.621  INFO 2798 --- [           main] HELP                                     :   001,test2,1,NULL
2021-08-09 10:45:05.621  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.621  INFO 2798 --- [           main] HELP                                     : 
2021-08-09 10:45:05.623  INFO 2798 --- [           main] HELP                                     : [Usage: ordering-columns]
2021-08-09 10:45:05.623  INFO 2798 --- [           main] HELP                                     :   Ordering column specified order using column-names.
2021-08-09 10:45:05.623  INFO 2798 --- [           main] HELP                                     :   e.g.) --command=ordering-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item9,item8,item2,item1
2021-08-09 10:45:05.623  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.625  INFO 2798 --- [           main] HELP                                     :   item1,item2,item8,item9
2021-08-09 10:45:05.625  INFO 2798 --- [           main] HELP                                     :   001,test,1,foo
2021-08-09 10:45:05.625  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.625  INFO 2798 --- [           main] HELP                                     :     ↓
2021-08-09 10:45:05.625  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.629  INFO 2798 --- [           main] HELP                                     :   item9,item8,item2,item1
2021-08-09 10:45:05.629  INFO 2798 --- [           main] HELP                                     :   foo,1,test,001
2021-08-09 10:45:05.629  INFO 2798 --- [           main] HELP                                     :   ------------------------
2021-08-09 10:45:05.629  INFO 2798 --- [           main] HELP                                     : 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.956 s
[INFO] Finished at: 2021-08-09T10:45:05+09:00
[INFO] ------------------------------------------------------------------------
```
