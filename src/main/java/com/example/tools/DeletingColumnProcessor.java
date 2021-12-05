package com.example.tools;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DeletingColumnProcessor extends AbstractColumnProcessor {

  static final DeletingColumnProcessor INSTANCE = new DeletingColumnProcessor();

  private DeletingColumnProcessor() {
    // NOP
  }

  void execute(List<String> columnNames, Path file, Charset encoding, String delimiter, Boolean ignoreEscapedEnclosure) {
    try {
      final List<String> originalHeaderColumns = readHeaderColumns(file, encoding, delimiter);
      if (originalHeaderColumns == null) {
        logger.warn("Skip deleting because file is empty. file:{}", file);
        return;
      }
      List<String> headerColumns = new ArrayList<>(originalHeaderColumns);
      Map<Integer, Boolean> containsColumnMap = new LinkedHashMap<>();
      for (String column : headerColumns) {
        containsColumnMap.put(containsColumnMap.size(), columnNames.contains(column));
      }
      List<String> validColumnNames = new ArrayList<>();
      for (Map.Entry<Integer, Boolean> entry : containsColumnMap.entrySet()) {
        if (!entry.getValue()) {
          validColumnNames.add(headerColumns.get(entry.getKey()));
        }
      }
      List<String[]> lines = readDataColumns(originalHeaderColumns.toArray(new String[0]), file, encoding, delimiter);
      List<String[]> saveLines = new ArrayList<>();
      saveLines.add(validColumnNames.toArray(new String[0]));
      for (String[] items : lines) {
        List<String> valueColumns = new ArrayList<>(Arrays.asList(items));
        List<String> validColumnValues = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : containsColumnMap.entrySet()) {
          if (!entry.getValue()) {
            validColumnValues.add(valueColumns.get(entry.getKey()));
          }
        }
        saveLines.add(validColumnValues.toArray(new String[0]));
      }

      writeLines(saveLines, file, encoding, delimiter, ignoreEscapedEnclosure);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}
