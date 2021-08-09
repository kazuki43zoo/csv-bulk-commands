package com.example.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CsvBulkCommandsApplicationRunner implements ApplicationRunner {

  private static final Logger logger = LoggerFactory.getLogger(CsvBulkCommandsApplicationRunner.class);
  private static final Logger helpLogger = LoggerFactory.getLogger("HELP");

  @Override
  public void run(ApplicationArguments args) throws IOException {
    if (args.getSourceArgs().length == 0 || args.containsOption("h") || args.containsOption("help")) {
      helpLogger.info("");
      helpLogger.info("[Arguments]");
      helpLogger.info("  --command       : adding-columns, deleting-columns, updating-columns, ordering-columns");
      helpLogger.info("  --dir           : target directory for apply command");
      helpLogger.info("  --files         : target files for apply command");
      helpLogger.info("  --column-names  : list of column name");
      helpLogger.info("  --column-values : list of column value");
      helpLogger.info("  --encoding      : encoding for read/write file (default: UTF-8)");
      helpLogger.info("  --h (--help)    : print help");
      helpLogger.info("");
      helpLogger.info("[Usage: adding-columns]");
      helpLogger.info("  Adding specified new column using column-names and column-values.");
      helpLogger.info("  e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item10,item11 --column-values=1,NULL");
      helpLogger.info("  ------------------------");
      helpLogger.info("  item1,item2");
      helpLogger.info("  001,test");
      helpLogger.info("  ------------------------");
      helpLogger.info("    ↓");
      helpLogger.info("  ------------------------");
      helpLogger.info("  item1,item2,item10,item11");
      helpLogger.info("  001,test,1,NULL");
      helpLogger.info("  ------------------------");
      helpLogger.info("");
      helpLogger.info("[Usage: deleting-columns]");
      helpLogger.info("  Deleting specified existing column using column-names.");
      helpLogger.info("  e.g.) --command=deleting-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9");
      helpLogger.info("  ------------------------");
      helpLogger.info("  item1,item2,item8,item9");
      helpLogger.info("  001,test,1,foo");
      helpLogger.info("  ------------------------");
      helpLogger.info("    ↓");
      helpLogger.info("  ------------------------");
      helpLogger.info("  item1,item8");
      helpLogger.info("  001,1");
      helpLogger.info("  ------------------------");
      helpLogger.info("");
      helpLogger.info("[Usage: updating-columns]");
      helpLogger.info("  Updating value specified existing column using column-names and column-values.");
      helpLogger.info("  e.g.) --command=updating-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9 --column-values=test2,NULL");
      helpLogger.info("  ------------------------");
      helpLogger.info("  item1,item2,item8,item9");
      helpLogger.info("  001,test,1,foo");
      helpLogger.info("  ------------------------");
      helpLogger.info("    ↓");
      helpLogger.info("  ------------------------");
      helpLogger.info("  item1,item2,item8,item9");
      helpLogger.info("  001,test2,1,NULL");
      helpLogger.info("  ------------------------");
      helpLogger.info("");
      helpLogger.info("[Usage: ordering-columns]");
      helpLogger.info("  Ordering column specified order using column-names.");
      helpLogger.info("  e.g.) --command=ordering-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item9,item8,item2,item1");
      helpLogger.info("  ------------------------");
      helpLogger.info("  item1,item2,item8,item9");
      helpLogger.info("  001,test,1,foo");
      helpLogger.info("  ------------------------");
      helpLogger.info("    ↓");
      helpLogger.info("  ------------------------");
      helpLogger.info("  item9,item8,item2,item1");
      helpLogger.info("  foo,1,test,001");
      helpLogger.info("  ------------------------");
      helpLogger.info("");
      return;
    }
    String command;
    if (args.containsOption("command")) {
      command = args.getOptionValues("command").stream().findFirst()
          .orElseThrow(() -> new IllegalArgumentException("'command' value is required. valid-commands:[adding-columns, deleting-columns, updating-columns, ordering-columns]"));
    } else {
      throw new IllegalArgumentException("'command' is required. valid-commands:[adding-columns, deleting-columns, updating-columns, ordering-columns]");
    }

    String dir;
    if (args.containsOption("dir")) {
      dir = args.getOptionValues("dir").stream().findFirst()
          .orElseThrow(() -> new IllegalArgumentException("'dir' value is required."));
    } else {
      throw new IllegalArgumentException("'dir' is required.");
    }

    List<String> files;
    if (args.containsOption("files")) {
      files = args.getOptionValues("files").stream().flatMap(x -> StringUtils.commaDelimitedListToSet(x).stream()).distinct().collect(Collectors.toList());
    } else {
      throw new IllegalArgumentException("'files' is required.");
    }

    List<String> columnNames = args.containsOption("column-names") ?
        args.getOptionValues("column-names").stream().flatMap(x -> Stream.of(StringUtils.commaDelimitedListToStringArray(x))).collect(Collectors.toList()) :
        Collections.emptyList();

    List<String> columnValues = args.containsOption("column-values") ?
        args.getOptionValues("column-values").stream().flatMap(x -> Stream.of(StringUtils.commaDelimitedListToStringArray(x))).collect(Collectors.toList()) :
        Collections.emptyList();

    Charset encoding = Charset.forName(args.getOptionValues("encoding").stream().findFirst().orElse(StandardCharsets.UTF_8.name()));

    logger.info("Start. command:{} dir:{} files:{} column-names:{} column-values:{} encoding:{}", command, dir, files, columnNames, columnValues, encoding);

    Files.walk(Paths.get(dir))
        .filter(Files::isRegularFile)
        .filter(file -> files.stream().anyMatch(x -> file.toString().replace('\\', '/').endsWith(x)))
        .sorted().forEach(file -> execute(command, columnNames, columnValues, file, encoding));

    logger.info("End.");
  }

  private void execute(String command, List<String> columnNames, List<String> columnValues, Path file, Charset encoding) {
    logger.info("processing file:{}", file);
    switch (command) {
      case "adding-columns":
        AddingColumnProcessor.INSTANCE.execute(columnNames, columnValues, file, encoding);
        break;
      case "deleting-columns":
        DeletingColumnProcessor.INSTANCE.execute(columnNames, file, encoding);
        break;
      case "updating-columns":
        UpdatingColumnProcessor.INSTANCE.execute(columnNames, columnValues, file, encoding);
        break;
      case "ordering-columns":
        OrderingColumnProcessor.INSTANCE.execute(columnNames, file, encoding);
        break;
      default:
        throw new UnsupportedOperationException(String.format("'%s' command not support. valid-commands:%s", command, "[adding-columns, deleting-columns, updating-columns, ordering-columns]"));
    }
  }

}
