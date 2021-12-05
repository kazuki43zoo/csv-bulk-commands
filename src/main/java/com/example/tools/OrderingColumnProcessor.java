package com.example.tools;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderingColumnProcessor extends AbstractColumnProcessor {

  static final OrderingColumnProcessor INSTANCE = new OrderingColumnProcessor();

  private OrderingColumnProcessor() {
    // NOP
  }

  void execute(List<String> columnNames, Path file, Charset encoding, String delimiter, Boolean ignoreEscapedEnclosure) {
    try {
      final List<String> originalHeaderColumns = readHeaderColumns(file, encoding, delimiter);
      if (originalHeaderColumns == null) {
        logger.warn("Skip ordering because file is empty. file:{}", file);
        return;
      }
      List<String> headerColumns = new ArrayList<>(originalHeaderColumns);
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
      List<String[]> lines = readDataColumns(originalHeaderColumns.toArray(new String[0]), file, encoding, delimiter);
      List<String[]> saveLines = new ArrayList<>();
      saveLines.add(columnNames.toArray(new String[0]));
      for (String[] items : lines) {
        List<String> valueColumns = new ArrayList<>(Arrays.asList(items));
        List<String> orderedColumnValues = new ArrayList<>();
        for (Integer index : columnIndexes) {
          orderedColumnValues.add(valueColumns.get(index));
        }
        saveLines.add(orderedColumnValues.toArray(new String[0]));
      }

      writeLines(saveLines, file, encoding, delimiter, ignoreEscapedEnclosure);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
