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

public class AddingColumnProcessor {

  static final AddingColumnProcessor INSTANCE = new AddingColumnProcessor();
  private static final Logger LOGGER = LoggerFactory.getLogger(AddingColumnProcessor.class);
  private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

  private AddingColumnProcessor() {
    // NOP
  }

  void execute(List<String> columnNames, List<String> columnValues, Path file, Charset encoding) {
    try {
      List<String> lines = Files.readAllLines(file, encoding);
      if (lines.isEmpty()) {
        LOGGER.warn("Skip adding because file is empty. file:{}", file);
        return;
      }
      List<String> headerColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(lines.remove(0))));
      Map<String, Integer> headerIndexMap = new LinkedHashMap<>();
      for (String column : headerColumns) {
        headerIndexMap.put(column, headerIndexMap.size());
      }
      Map<Integer, Boolean> containsColumnMap = new LinkedHashMap<>();
      for (String columnName : columnNames) {
        boolean contains = headerColumns.contains(columnName);
        containsColumnMap.put(containsColumnMap.size(), contains);
        if (!contains) {
          headerColumns.add(columnName);
        }
      }
      List<String> validColumnValues = new ArrayList<>();
      for (Map.Entry<Integer, Boolean> entry : containsColumnMap.entrySet()) {
        if (!entry.getValue()) {
          validColumnValues.add(columnValues.get(entry.getKey()));
        }
      }
      List<String> saveLines = new ArrayList<>();
      saveLines.add(StringUtils.collectionToCommaDelimitedString(headerColumns));
      for (String line : lines) {
        List<String> valueColumns = new ArrayList<>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(line)));
        StandardEvaluationContext context = new StandardEvaluationContext();
        headerIndexMap.forEach((name, index) -> context.setVariable(name, valueColumns.get(index)));
        for (String value : validColumnValues) {
          Expression expression = EXPRESSION_PARSER.parseExpression(value);
          valueColumns.add(Objects.toString(expression.getValue(context)));
        }
        saveLines.add(StringUtils.collectionToCommaDelimitedString(valueColumns));
      }

      Files.write(file, saveLines, encoding);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
