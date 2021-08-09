package com.example.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderingColumnProcessor {

  private static final Logger logger = LoggerFactory.getLogger(OrderingColumnProcessor.class);

  static final OrderingColumnProcessor INSTANCE = new OrderingColumnProcessor();

  private OrderingColumnProcessor() {
    // NOP
  }

  void execute(List<String> columnNames, Path file, Charset encoding) {
    try {
      List<String> lines = Files.readAllLines(file, encoding);
      if (lines.isEmpty()) {
        logger.warn("Skip ordering because file is empty. file:{}", file);
        return;
      }
      List<String> headerColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(lines.remove(0))));
      if (headerColumns.size() != columnNames.size()) {
        logger.warn("Skip ordering because column size not same. before:{} after:{} before-columns:{} after-columns:{} file:{}", headerColumns.size(), columnNames.size(), headerColumns, columnNames, file);
        return;
      }
      if (!headerColumns.containsAll(columnNames)) {
        logger.warn("Skip ordering because columns not same. before-columns:{} after-columns:{} file:{}", headerColumns, columnNames, file);
        return;
      }
      if (headerColumns.equals(columnNames)) {
        logger.info("Skip ordering because same ordering. file:{}", file);
        return;
      }
      List<Integer> columnIndexes = new ArrayList<>();
      for (String column : columnNames) {
        columnIndexes.add(headerColumns.indexOf(column));
      }
      List<String> saveLines = new ArrayList<>();
      saveLines.add(StringUtils.collectionToCommaDelimitedString(columnNames));
      for (String line : lines) {
        List<String> valueColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(line)));
        List<String> orderedColumnValues = new ArrayList<>();
        for (Integer index : columnIndexes) {
          orderedColumnValues.add(valueColumns.get(index));
        }
        saveLines.add(StringUtils.collectionToCommaDelimitedString(orderedColumnValues));
      }

      Files.write(file, saveLines, encoding);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
