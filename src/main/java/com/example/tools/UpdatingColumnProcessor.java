package com.example.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
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
import java.util.Objects;

public class UpdatingColumnProcessor {

  static final UpdatingColumnProcessor INSTANCE = new UpdatingColumnProcessor();
  private static final Logger LOGGER = LoggerFactory.getLogger(UpdatingColumnProcessor.class);
  private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

  private UpdatingColumnProcessor() {
    // NOP
  }

  void execute(List<String> columnNames, List<String> columnValues, Path file, Charset encoding, Map<String, Object> valueMappings) {
    try {
      List<String> lines = Files.readAllLines(file, encoding);
      if (lines.isEmpty()) {
        LOGGER.warn("Skip setting because file is empty. file:{}", file);
        return;
      }
      List<String> headerColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(lines.remove(0))));
      if (!headerColumns.containsAll(columnNames)) {
        LOGGER.warn("Skip setting because columns not exists. header-columns:{} target-columns:{} file:{}", headerColumns, columnNames, file);
        return;
      }
      Map<String, Integer> headerIndexMap = new LinkedHashMap<>();
      for (String column : headerColumns) {
        headerIndexMap.put(column, headerIndexMap.size());
      }
      Map<Integer, String> columnIndexValueMap = new LinkedHashMap<>();
      for (String column : columnNames) {
        columnIndexValueMap.put(headerColumns.indexOf(column), columnValues.get(columnIndexValueMap.size()));
      }
      List<String> saveLines = new ArrayList<>();
      saveLines.add(StringUtils.collectionToCommaDelimitedString(headerColumns));
      for (String line : lines) {
        List<String> valueColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(line)));
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("_valueMappings", valueMappings);
        headerIndexMap.forEach((name, index) -> context.setVariable(name, valueColumns.get(index)));
        for (Map.Entry<Integer, String> entry : columnIndexValueMap.entrySet()) {
          Expression expression = EXPRESSION_PARSER.parseExpression(entry.getValue());
          valueColumns.set(entry.getKey(), Objects.toString(expression.getValue(context)));
        }
        saveLines.add(StringUtils.collectionToCommaDelimitedString(valueColumns));
      }

      Files.write(file, saveLines, encoding);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
