package com.example.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DeletingColumnProcessor {

  private static final Logger logger = LoggerFactory.getLogger(DeletingColumnProcessor.class);

  static final DeletingColumnProcessor INSTANCE = new DeletingColumnProcessor();

  private DeletingColumnProcessor() {
    // NOP
  }

  void execute(List<String> columnNames, Path file) {
    try {
      List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
      if (lines.isEmpty()) {
        logger.warn("Skip deleting because file is empty. file:{}", file);
        return;
      }
      List<String> headerColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(lines.remove(0))));
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

      List<String> saveLines = new ArrayList<>();
      saveLines.add(StringUtils.collectionToCommaDelimitedString(validColumnNames));
      for (String line : lines) {
        List<String> valueColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(line)));
        List<String> validColumnValues = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : containsColumnMap.entrySet()) {
          if (!entry.getValue()) {
            validColumnValues.add(valueColumns.get(entry.getKey()));
          }
        }
        saveLines.add(StringUtils.collectionToCommaDelimitedString(validColumnValues));
      }

      Files.write(file, saveLines, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
