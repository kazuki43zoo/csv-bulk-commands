# csv-bulk-commands

Bulk operation utilities for csv file.

## Features

Support following features.

* Adding columns at the end
* Deleting columns
* Updating columns by specified expression(fixed value or dynamic value)
* Ordering columns by specified order

## Support CSV file specifications

* The first line is header
* The Record separator is LF or CRLF (Writing separator is OS dependent character)
* No support an enclosing character (no support include a comma and record separator in column value)
* The default encoding is UTF-8 (can change an any encoding using command line argument)

> **NOTE:**
> 
> Will have a plan to supporting various csv format at future.

## How to specify target files

Search files that matches conditions specified by `--dir` and `--files`.

* You need to specify a base directory using the `--dir`
* You need to specify a target file path suffix using the `--files`

## How to run

### Using Spring Boot Maven Plugin

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

[Arguments]
  --command
       adding-columns, deleting-columns, updating-columns, ordering-columns
  --dir
       target directory for apply command(can search target files on specified directory)
  --files
       target files for apply command(can filter that ending with specified file name)
  --column-names
       list of column name
  --column-values
       list of column value(can reference other column values using SpEL expression)
  --encoding
       encoding for read/write file (default: UTF-8)
  --h (--help)
       print help

[Usage: adding-columns]
  Adding specified new column using column-names and column-values.
  e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item10,item11 --column-values=1,'NULL'
  ------------------------
  item1,item2
  001,test
  ------------------------
    ↓
  ------------------------
  item1,item2,item10,item11
  001,test,1,NULL
  ------------------------

[Usage: deleting-columns]
  Deleting specified existing column using column-names.
  e.g.) --command=deleting-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9
  ------------------------
  item1,item2,item8,item9
  001,test,1,foo
  ------------------------
    ↓
  ------------------------
  item1,item8
  001,1
  ------------------------

[Usage: updating-columns]
  Updating value specified existing column using column-names and column-values.
  e.g.) --command=updating-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9 --column-values='test2','NULL'
  ------------------------
  item1,item2,item8,item9
  001,test,1,foo
  ------------------------
    ↓
  ------------------------
  item1,item2,item8,item9
  001,test2,1,NULL
  ------------------------

[Usage: ordering-columns]
  Ordering column specified order using column-names.
  e.g.) --command=ordering-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item9,item8,item2,item1
  ------------------------
  item1,item2,item8,item9
  001,test,1,foo
  ------------------------
    ↓
  ------------------------
  item9,item8,item2,item1
  foo,1,test,001
  ------------------------
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.956 s
[INFO] Finished at: 2021-08-09T10:45:05+09:00
[INFO] ------------------------------------------------------------------------
```

### Using standalone Java Application

```bash
$ ./mvnw clean verify -DskipTests
```

```
$ java -jar target/csv-bulk-commands.jar
```
