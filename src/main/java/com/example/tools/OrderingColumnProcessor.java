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
      if (!columnNames.containsAll(originalHeaderColumns)) {
        logger.warn("Skip ordering because part of header columns are not included in ordering columns. header-columns:{} ordering-columns:{} file:{}",
            originalHeaderColumns, columnNames, file);
        return;
      }
      List<String> targetColumnNames = new ArrayList<>(columnNames);
      targetColumnNames.retainAll(originalHeaderColumns);
      if (originalHeaderColumns.equals(targetColumnNames)) {
        logger.info("Skip ordering because same ordering. file:{}", file);
        return;
      }
      List<Integer> columnIndexes = new ArrayList<>();
      for (String column : targetColumnNames) {
        columnIndexes.add(originalHeaderColumns.indexOf(column));
      }
      List<String[]> lines = readDataColumns(originalHeaderColumns.toArray(new String[0]), file, encoding, delimiter);
      List<String[]> saveLines = new ArrayList<>();
      saveLines.add(targetColumnNames.toArray(new String[0]));
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
