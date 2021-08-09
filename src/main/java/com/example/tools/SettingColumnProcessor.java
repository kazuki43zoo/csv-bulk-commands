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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SettingColumnProcessor {

  private static final Logger logger = LoggerFactory.getLogger(SettingColumnProcessor.class);

  static final SettingColumnProcessor INSTANCE = new SettingColumnProcessor();

  private SettingColumnProcessor() {
    // NOP
  }

  void execute(List<String> columnNames, List<String> columnValues, Path file, Charset encoding) {
    try {
      List<String> lines = Files.readAllLines(file, encoding);
      if (lines.isEmpty()) {
        logger.warn("Skip setting because file is empty. file:{}", file);
        return;
      }
      List<String> headerColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(lines.remove(0))));
      if (!headerColumns.containsAll(columnNames)) {
        logger.warn("Skip setting because columns not exists. header-columns:{} target-columns:{} file:{}", headerColumns, columnNames, file);
        return;
      }
      Map<Integer, String> columnIndexValueMap = new LinkedHashMap<>();
      for (String column : columnNames) {
        columnIndexValueMap.put(headerColumns.indexOf(column), columnValues.get(columnIndexValueMap.size()));
      }
      List<String> saveLines = new ArrayList<>();
      saveLines.add(StringUtils.collectionToCommaDelimitedString(headerColumns));
      for (String line : lines) {
        List<String> valueColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(line)));
        for (Map.Entry<Integer, String> entry : columnIndexValueMap.entrySet()) {
          valueColumns.set(entry.getKey(), entry.getValue());
        }
        saveLines.add(StringUtils.collectionToCommaDelimitedString(valueColumns));
      }

      Files.write(file, saveLines, encoding);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
