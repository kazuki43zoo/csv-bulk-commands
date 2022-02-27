# csv-bulk-commands

Bulk operation utilities for csv file.

## Features

Support following features.

* Adding columns at the end
* Deleting columns
* Updating columns by specified expression(fixed value or dynamic value)
* Ordering columns by specified order
* Support adding column at any position

## Related libraries document

* [SpEL provided by Spring Framework](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#expressions)
* [FlatFileItemReader/FlatFileItemWriter provided by Spring Batch](https://docs.spring.io/spring-batch/docs/current/reference/html/readersAndWriters.html#flatFiles)

## Support CSV file specifications

* The first line is header
* The Record separator is LF or CRLF (Writing separator is OS dependent character)
* Support an enclosing character(`"`) (support include a record separator(`,`) in column value)
* Support custom delimiter character(e.g. `\t`)
* The default encoding is UTF-8 (can change an any encoding using command line argument)

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
[INFO] Building csv-bulk-commands 0.0.5-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] >>> spring-boot-maven-plugin:2.6.4:run (default-cli) > test-compile @ csv-bulk-commands >>>
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
[INFO] Copying 14 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.8.1:testCompile (default-testCompile) @ csv-bulk-commands ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] <<< spring-boot-maven-plugin:2.6.4:run (default-cli) < test-compile @ csv-bulk-commands <<<
[INFO] 
[INFO] 
[INFO] --- spring-boot-maven-plugin:2.6.4:run (default-cli) @ csv-bulk-commands ---
[INFO] Attaching agents: []

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
  --value-mapping-files
       mapping yaml files for value converting
       can be accessed using an SpEL like as #_valueMappings[{value-name}][{value}] (e.g. --column-names=foo --column-values=#_valueMappings[foo][#foo]?:'0')
       e.g.) value mapping yaml file
       foo:
         "10": "1"
         "20": "2"
       bar:
         "10": "2"
         "20": "1"
  --delimiter
       delimiter character (default: ",")
  --ignore-escaped-enclosure
       whether ignore escape an enclosing character on writing (default: false)
  --first
       indicate that adding column at first position
  --after
       indicate that adding column at after position
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

  e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item10,item11 --column-values=1,'NULL' --first
  ------------------------
  item1,item2
  001,test
  ------------------------
    ↓
  ------------------------
  item10,item11,item1,item2
  1,NULL,001,test
  ------------------------

  e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item10,item11 --column-values=1,'NULL' --after=item1
  ------------------------
  item1,item2
  001,test
  ------------------------
    ↓
  ------------------------
  item1,item10,item11,item2
  001,1,NULL,test
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
[INFO] Total time:  2.376 s
[INFO] Finished at: 2022-02-27T12:44:55+09:00
[INFO] ------------------------------------------------------------------------
```

### Using standalone Java Application

```bash
$ ./mvnw clean verify -DskipTests
```

```
$ java -jar target/csv-bulk-commands.jar
```

### How to specify Tab character on delimiter with bash

```
$ java -jar target/csv-bulk-commands.jar ... --delimiter=$'\t'
```
