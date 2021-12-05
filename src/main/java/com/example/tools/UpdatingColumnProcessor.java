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

public class UpdatingColumnProcessor extends AbstractColumnProcessor {

  static final UpdatingColumnProcessor INSTANCE = new UpdatingColumnProcessor();

  private UpdatingColumnProcessor() {
    // NOP
  }

  void execute(List<String> columnNames, List<String> columnValues, Path file, Charset encoding, Map<String, Object> valueMappings, String delimiter, Boolean ignoreEscapedEnclosure) {
    try {
      final List<String> originalHeaderColumns = readHeaderColumns(file, encoding, delimiter);
      if (originalHeaderColumns == null) {
        logger.warn("Skip setting because file is empty. file:{}", file);
        return;
      }
      List<String> headerColumns = new ArrayList<>(originalHeaderColumns);
      if (!headerColumns.containsAll(columnNames)) {
        logger.warn("Skip setting because columns not exists. header-columns:{} target-columns:{} file:{}", headerColumns, columnNames, file);
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
      List<String[]> lines = readDataColumns(originalHeaderColumns.toArray(new String[0]), file, encoding, delimiter);
      List<String[]> saveLines = new ArrayList<>();
      saveLines.add(headerColumns.toArray(new String[0]));
      for (String[] items : lines) {
        List<String> valueColumns = new ArrayList<>(Arrays.asList(items));
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("_valueMappings", valueMappings);
        headerIndexMap.forEach((name, index) -> context.setVariable(name, valueColumns.get(index)));
        for (Map.Entry<Integer, String> entry : columnIndexValueMap.entrySet()) {
          Expression expression = expressionParser.parseExpression(entry.getValue());
          valueColumns.set(entry.getKey(), Objects.toString(expression.getValue(context)));
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
