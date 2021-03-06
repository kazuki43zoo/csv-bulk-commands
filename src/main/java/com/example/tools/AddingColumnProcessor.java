package com.example.tools;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class AddingColumnProcessor extends AbstractColumnProcessor {

  static final AddingColumnProcessor INSTANCE = new AddingColumnProcessor();

  private AddingColumnProcessor() {
    // NOP
  }

  void execute(String method, String target, List<String> columnNames, List<String> columnValues, Path file, Charset encoding, Map<String, Object> valueMappings, String delimiter, Boolean ignoreEscapedEnclosure) {
    try {
      final List<String> originalHeaderColumns = readHeaderColumns(file, encoding, delimiter);
      if (originalHeaderColumns == null) {
        logger.warn("Skip adding because file is empty. file:{}", file);
        return;
      }
      if ("after".equalsIgnoreCase(method) && (originalHeaderColumns.isEmpty() || Stream.of(target, target.toLowerCase(),
              target.toUpperCase()).map(x -> originalHeaderColumns.indexOf(target))
          .noneMatch(x -> x != -1))) {
        throw new IllegalArgumentException(
            "Cannot use the 'after' option because no header name in csv file header.");
      }
      final List<String> headerColumns = new ArrayList<>(originalHeaderColumns);
      Map<String, Integer> headerIndexMap = new LinkedHashMap<>();
      for (String column : headerColumns) {
        headerIndexMap.put(column, headerIndexMap.size());
      }

      int addingStartIndex;
      if ("first".equalsIgnoreCase(method)) {
        addingStartIndex = 0;
      } else if ("after".equalsIgnoreCase(method)) {
        addingStartIndex =
            Stream.of(target, target.toLowerCase(), target.toUpperCase()).map(x -> headerColumns.indexOf(target))
                .filter(x -> x != -1).findAny().orElse(0) + 1;
      } else {
        addingStartIndex = headerColumns.size();
      }

      Map<Integer, Boolean> containsColumnMap = new LinkedHashMap<>();
      int baseHeaderColumnSize = headerColumns.size();
      for (String columnName : columnNames) {
        boolean contains = headerColumns.contains(columnName);
        containsColumnMap.put(containsColumnMap.size(), contains);
        if (!contains) {
          headerColumns.add(addingStartIndex + (headerColumns.size() - baseHeaderColumnSize), columnName);
        }
      }
      List<String> validColumnValues = new ArrayList<>();
      for (Map.Entry<Integer, Boolean> entry : containsColumnMap.entrySet()) {
        if (!entry.getValue()) {
          validColumnValues.add(columnValues.get(entry.getKey()));
        }
      }
      List<String[]> lines = readDataColumns(originalHeaderColumns.toArray(new String[0]), file, encoding, delimiter);
      List<String[]> saveLines = new ArrayList<>();
      saveLines.add(headerColumns.toArray(new String[0]));
      for (String[] items : lines) {
        List<String> valueColumns = new ArrayList<>(Arrays.asList(items));
        if ("last".equalsIgnoreCase(method)) {
          addingStartIndex = valueColumns.size();
        }
        int baseValueColumnSize = valueColumns.size();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("_valueMappings", valueMappings);
        headerIndexMap.forEach((name, index) -> context.setVariable(name, valueColumns.get(index)));
        for (String value : validColumnValues) {
          Expression expression = expressionParser.parseExpression(value);
          valueColumns.add(addingStartIndex + (valueColumns.size() - baseValueColumnSize), Objects.toString(expression.getValue(context)));
        }
        saveLines.add(valueColumns.toArray(new String[0]));
      }
      writeLines(saveLines, file, encoding, delimiter, ignoreEscapedEnclosure);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}
