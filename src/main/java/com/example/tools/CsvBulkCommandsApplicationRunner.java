package com.example.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CsvBulkCommandsApplicationRunner implements ApplicationRunner {

  private static final Logger LOGGER = LoggerFactory.getLogger(CsvBulkCommandsApplicationRunner.class);

  @Override
  public void run(ApplicationArguments args) throws IOException {
    if (args.getSourceArgs().length == 0 || args.containsOption("h") || args.containsOption("help")) {
      System.out.println("");
      System.out.println("[Arguments]");
      System.out.println("  --command");
      System.out.println("       adding-columns, deleting-columns, updating-columns, ordering-columns");
      System.out.println("  --dir");
      System.out.println("       target directory for apply command(can search target files on specified directory)");
      System.out.println("  --files");
      System.out.println("       target files for apply command(can filter that ending with specified file name)");
      System.out.println("  --column-names");
      System.out.println("       list of column name");
      System.out.println("  --column-values");
      System.out.println("       list of column value(can reference other column values using SpEL expression)");
      System.out.println("  --encoding");
      System.out.println("       encoding for read/write file (default: UTF-8)");
      System.out.println("  --value-mapping-files");
      System.out.println("       mapping yaml files for value converting");
      System.out.println("       can be accessed using an SpEL like as #_valueMappings[{value-name}][{value}] (e.g. --column-names=foo --column-values=#_valueMappings[foo][#foo]?:'0')");
      System.out.println("       e.g.) value mapping yaml file");
      System.out.println("       foo:");
      System.out.println("         \"10\": \"1\"");
      System.out.println("         \"20\": \"2\"");
      System.out.println("       bar:");
      System.out.println("         \"10\": \"2\"");
      System.out.println("         \"20\": \"1\"");
      System.out.println("  --delimiter");
      System.out.println("       delimiter character (default: \",\")");
      System.out.println("  --ignore-escaped-enclosure");
      System.out.println("       whether ignore escape an enclosing character on writing (default: false)");
      System.out.println("  --first");
      System.out.println("       indicate that adding column at first position");
      System.out.println("  --after");
      System.out.println("       indicate that adding column at after position");
      System.out.println("  --h (--help)");
      System.out.println("       print help");
      System.out.println();
      System.out.println("[Usage: adding-columns]");
      System.out.println("  Adding specified new column using column-names and column-values.");
      System.out.println("  e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item10,item11 --column-values=1,'NULL'");
      System.out.println("  ------------------------");
      System.out.println("  item1,item2");
      System.out.println("  001,test");
      System.out.println("  ------------------------");
      System.out.println("    ↓");
      System.out.println("  ------------------------");
      System.out.println("  item1,item2,item10,item11");
      System.out.println("  001,test,1,NULL");
      System.out.println("  ------------------------");
      System.out.println();
      System.out.println("  e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item10,item11 --column-values=1,'NULL' --first");
      System.out.println("  ------------------------");
      System.out.println("  item1,item2");
      System.out.println("  001,test");
      System.out.println("  ------------------------");
      System.out.println("    ↓");
      System.out.println("  ------------------------");
      System.out.println("  item10,item11,item1,item2");
      System.out.println("  1,NULL,001,test");
      System.out.println("  ------------------------");
      System.out.println();
      System.out.println("  e.g.) --command=adding-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item10,item11 --column-values=1,'NULL' --after=item1");
      System.out.println("  ------------------------");
      System.out.println("  item1,item2");
      System.out.println("  001,test");
      System.out.println("  ------------------------");
      System.out.println("    ↓");
      System.out.println("  ------------------------");
      System.out.println("  item1,item10,item11,item2");
      System.out.println("  001,1,NULL,test");
      System.out.println("  ------------------------");
      System.out.println();
      System.out.println("[Usage: deleting-columns]");
      System.out.println("  Deleting specified existing column using column-names.");
      System.out.println("  e.g.) --command=deleting-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9");
      System.out.println("  ------------------------");
      System.out.println("  item1,item2,item8,item9");
      System.out.println("  001,test,1,foo");
      System.out.println("  ------------------------");
      System.out.println("    ↓");
      System.out.println("  ------------------------");
      System.out.println("  item1,item8");
      System.out.println("  001,1");
      System.out.println("  ------------------------");
      System.out.println();
      System.out.println("[Usage: updating-columns]");
      System.out.println("  Updating value specified existing column using column-names and column-values.");
      System.out.println("  e.g.) --command=updating-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item2,item9 --column-values='test2','NULL'");
      System.out.println("  ------------------------");
      System.out.println("  item1,item2,item8,item9");
      System.out.println("  001,test,1,foo");
      System.out.println("  ------------------------");
      System.out.println("    ↓");
      System.out.println("  ------------------------");
      System.out.println("  item1,item2,item8,item9");
      System.out.println("  001,test2,1,NULL");
      System.out.println("  ------------------------");
      System.out.println();
      System.out.println("[Usage: ordering-columns]");
      System.out.println("  Ordering column specified order using column-names.");
      System.out.println("  e.g.) --command=ordering-columns --dir=src/test/resources/data --files=xxx.csv,yyy.csv --column-names=item9,item8,item2,item1");
      System.out.println("  ------------------------");
      System.out.println("  item1,item2,item8,item9");
      System.out.println("  001,test,1,foo");
      System.out.println("  ------------------------");
      System.out.println("    ↓");
      System.out.println("  ------------------------");
      System.out.println("  item9,item8,item2,item1");
      System.out.println("  foo,1,test,001");
      System.out.println("  ------------------------");
      System.out.println();
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

    Charset encoding = args.containsOption("encoding") ?
        Charset.forName(args.getOptionValues("encoding").stream().findFirst().orElse(StandardCharsets.UTF_8.name())) :
        StandardCharsets.UTF_8;

    final Map<String, Object> valueMappings;
    if (args.containsOption("value-mapping-files")) {
      YamlMapFactoryBean yamlMapFactoryBean = new YamlMapFactoryBean();
      yamlMapFactoryBean.setResources(args.getOptionValues("value-mapping-files").stream().map(FileSystemResource::new).toArray(Resource[]::new));
      valueMappings = yamlMapFactoryBean.getObject();
    } else {
      valueMappings = Collections.emptyMap();
    }

    String delimiter = args.containsOption("delimiter") ?
        args.getOptionValues("delimiter").stream().findFirst().orElse(",") :
        ",";

    Boolean ignoreEscapedEnclosure = args.containsOption("ignore-escaped-enclosure") &&
        Boolean.parseBoolean(args.getOptionValues("ignore-escaped-enclosure").stream().findFirst().orElse(null));

    final String addingMethod;
    final String addingTarget;
    if (args.containsOption("first")) {
      addingMethod = "first";
      addingTarget = null;
    } else if (args.containsOption("after")) {
      addingMethod = "after";
      addingTarget = args.getOptionValues("after").stream().findFirst().orElse(null);
      if (addingTarget == null) {
        throw new IllegalArgumentException("'after' is required.");
      }
    } else {
      addingMethod = "last";
      addingTarget = null;
    }

    LOGGER.info("Start. command:{} dir:{} files:{} column-names:{} column-values:{} encoding:{} value-mappings:{} delimiter:{} ignore-escaped-enclosure:{} adding-method:{} adding-target:{}",
        command, dir, files, columnNames, columnValues, encoding, valueMappings, delimiter, ignoreEscapedEnclosure, addingMethod, addingTarget);

    Files.walk(Paths.get(dir))
        .filter(Files::isRegularFile)
        .filter(file -> files.stream().anyMatch(x -> file.toString().replace('\\', '/').endsWith(x)))
        .sorted().forEach(file -> execute(command, columnNames, columnValues, file, encoding, valueMappings, delimiter, ignoreEscapedEnclosure, addingMethod, addingTarget));

    LOGGER.info("End.");
  }

  private void execute(String command, List<String> columnNames, List<String> columnValues, Path file, Charset encoding, Map<String, Object> valueMappings, String delimiter, Boolean ignoreEscapedEnclosure, String addingMethod, String addingTarget) {
    LOGGER.info("processing file:{}", file);
    switch (command) {
      case "adding-columns":
        AddingColumnProcessor.INSTANCE.execute(addingMethod, addingTarget, columnNames, columnValues, file, encoding, valueMappings, delimiter, ignoreEscapedEnclosure);
        break;
      case "deleting-columns":
        DeletingColumnProcessor.INSTANCE.execute(columnNames, file, encoding, delimiter, ignoreEscapedEnclosure);
        break;
      case "updating-columns":
        UpdatingColumnProcessor.INSTANCE.execute(columnNames, columnValues, file, encoding, valueMappings, delimiter, ignoreEscapedEnclosure);
        break;
      case "ordering-columns":
        OrderingColumnProcessor.INSTANCE.execute(columnNames, file, encoding, delimiter, ignoreEscapedEnclosure);
        break;
      default:
        throw new UnsupportedOperationException(String.format("'%s' command not support. valid-commands:%s", command, "[adding-columns, deleting-columns, updating-columns, ordering-columns]"));
    }
  }

}
