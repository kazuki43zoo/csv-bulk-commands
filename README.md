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
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-resources-plugin:3.2.0:testResources (default-testResources) @ csv-bulk-commands ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Using 'UTF-8' encoding to copy filtered properties files.
[INFO] Copying 8 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ csv-bulk-commands ---
[INFO] Nothing to compile - all classes are up to date
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

2021-08-08 23:44:09.228  INFO 1139 --- [           main] c.e.tools.CsvBulkCommandsApplication     : Starting CsvBulkCommandsApplication using Java 11.0.1 on xxx with PID 1139 (/Users/xxx/git/csv-bulk-commands/target/classes started by xxxx in /Users/xxx/git/csv-bulk-commands)
2021-08-08 23:44:09.231  INFO 1139 --- [           main] c.e.tools.CsvBulkCommandsApplication     : No active profile set, falling back to default profiles: default
2021-08-08 23:44:09.829  INFO 1139 --- [           main] c.e.tools.CsvBulkCommandsApplication     : Started CsvBulkCommandsApplication in 1.045 seconds (JVM running for 1.495)
2021-08-08 23:44:09.832  INFO 1139 --- [           main] HELP                                     : 
2021-08-08 23:44:09.832  INFO 1139 --- [           main] HELP                                     : [Option]
2021-08-08 23:44:09.832  INFO 1139 --- [           main] HELP                                     :   --command       : adding-columns, deleting-columns, setting-columns, ordering-columns
2021-08-08 23:44:09.833  INFO 1139 --- [           main] HELP                                     :   --dir           : target directory for apply command
2021-08-08 23:44:09.833  INFO 1139 --- [           main] HELP                                     :   --files         : target files for apply command
2021-08-08 23:44:09.833  INFO 1139 --- [           main] HELP                                     :   --column-names  : list of column name
2021-08-08 23:44:09.833  INFO 1139 --- [           main] HELP                                     :   --column-values : list of column value
2021-08-08 23:44:09.833  INFO 1139 --- [           main] HELP                                     :   --encoding      : encoding for read/write file (default: UTF-8)
2021-08-08 23:44:09.833  INFO 1139 --- [           main] HELP                                     :   --h (--help)    : print help
2021-08-08 23:44:09.833  INFO 1139 --- [           main] HELP                                     : 
2021-08-08 23:44:09.833  INFO 1139 --- [           main] HELP                                     : [Usage: adding-columns]
2021-08-08 23:44:09.834  INFO 1139 --- [           main] HELP                                     :   Adding specified new column using column-names and column-values.
2021-08-08 23:44:09.839  INFO 1139 --- [           main] HELP                                     :   e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item10,item11 --column-values=1,NULL
2021-08-08 23:44:09.839  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.839  INFO 1139 --- [           main] HELP                                     :   item1,item2
2021-08-08 23:44:09.839  INFO 1139 --- [           main] HELP                                     :   001,test
2021-08-08 23:44:09.842  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.842  INFO 1139 --- [           main] HELP                                     :     ↓
2021-08-08 23:44:09.842  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.843  INFO 1139 --- [           main] HELP                                     :   item1,item2,item10,item11
2021-08-08 23:44:09.843  INFO 1139 --- [           main] HELP                                     :   001,test,1,NULL
2021-08-08 23:44:09.844  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.845  INFO 1139 --- [           main] HELP                                     : 
2021-08-08 23:44:09.845  INFO 1139 --- [           main] HELP                                     : [Usage: deleting-columns]
2021-08-08 23:44:09.845  INFO 1139 --- [           main] HELP                                     :   Deleting specified existing column using column-names.
2021-08-08 23:44:09.845  INFO 1139 --- [           main] HELP                                     :   e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9
2021-08-08 23:44:09.847  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.847  INFO 1139 --- [           main] HELP                                     :   item1,item2,item8,item9
2021-08-08 23:44:09.847  INFO 1139 --- [           main] HELP                                     :   001,test,1,foo
2021-08-08 23:44:09.847  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.847  INFO 1139 --- [           main] HELP                                     :     ↓
2021-08-08 23:44:09.849  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.850  INFO 1139 --- [           main] HELP                                     :   item1,item8
2021-08-08 23:44:09.850  INFO 1139 --- [           main] HELP                                     :   001,1
2021-08-08 23:44:09.850  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.850  INFO 1139 --- [           main] HELP                                     : 
2021-08-08 23:44:09.852  INFO 1139 --- [           main] HELP                                     : [Usage: setting-columns]
2021-08-08 23:44:09.853  INFO 1139 --- [           main] HELP                                     :   Setting(Overriding) value specified existing column using column-names and column-values.
2021-08-08 23:44:09.853  INFO 1139 --- [           main] HELP                                     :   e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9 --column-values=test2,NULL
2021-08-08 23:44:09.853  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.861  INFO 1139 --- [           main] HELP                                     :   item1,item2,item8,item9
2021-08-08 23:44:09.861  INFO 1139 --- [           main] HELP                                     :   001,test,1,foo
2021-08-08 23:44:09.861  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.861  INFO 1139 --- [           main] HELP                                     :     ↓
2021-08-08 23:44:09.861  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.862  INFO 1139 --- [           main] HELP                                     :   item1,item2,item8,item9
2021-08-08 23:44:09.863  INFO 1139 --- [           main] HELP                                     :   001,test2,1,NULL
2021-08-08 23:44:09.863  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.863  INFO 1139 --- [           main] HELP                                     : 
2021-08-08 23:44:09.863  INFO 1139 --- [           main] HELP                                     : [Usage: ordering-columns]
2021-08-08 23:44:09.864  INFO 1139 --- [           main] HELP                                     :   Ordering column specified order using column-names.
2021-08-08 23:44:09.864  INFO 1139 --- [           main] HELP                                     :   e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item9,item8,item2,item1
2021-08-08 23:44:09.864  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.864  INFO 1139 --- [           main] HELP                                     :   item1,item2,item8,item9
2021-08-08 23:44:09.865  INFO 1139 --- [           main] HELP                                     :   001,test,1,foo
2021-08-08 23:44:09.867  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.867  INFO 1139 --- [           main] HELP                                     :     ↓
2021-08-08 23:44:09.867  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.867  INFO 1139 --- [           main] HELP                                     :   item9,item8,item2,item1
2021-08-08 23:44:09.867  INFO 1139 --- [           main] HELP                                     :   foo,1,test,001
2021-08-08 23:44:09.869  INFO 1139 --- [           main] HELP                                     :   ------------------------
2021-08-08 23:44:09.869  INFO 1139 --- [           main] HELP                                     : 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  3.776 s
[INFO] Finished at: 2021-08-08T23:44:09+09:00
[INFO] ------------------------------------------------------------------------
```