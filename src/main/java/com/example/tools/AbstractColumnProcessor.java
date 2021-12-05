package com.example.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.ExtractorLineAggregator;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.util.StringUtils;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractColumnProcessor {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected final ExpressionParser expressionParser = new SpelExpressionParser();

  protected List<String> readHeaderColumns(Path file, Charset encoding, String delimiter) throws Exception {
    List<String> headerColumns = new ArrayList<>();
    ItemStreamReader<String[]> headerReader = new FlatFileItemReaderBuilder<String[]>()
        .recordSeparatorPolicy(new DefaultRecordSeparatorPolicy())
        .lineTokenizer(x -> new DefaultFieldSet(StringUtils.delimitedListToStringArray(x, Optional.ofNullable(delimiter).orElse(","))))
        .fieldSetMapper(FieldSet::getValues)
        .encoding(encoding.name())
        .resource(new FileSystemResource(file.toFile()))
        .name("default")
        .linesToSkip(0)
        .maxItemCount(1)
        .build();
    try {
      headerReader.open(new ExecutionContext());
      String[] line = headerReader.read();
      if (line == null) {
        return null;
      }
      headerColumns.addAll(Arrays.asList(line));
    } finally {
      headerReader.close();
    }
    return headerColumns;
  }

  protected List<String[]> readDataColumns(String[] columnNames, Path file, Charset encoding, String delimiter) throws Exception {
    ItemStreamReader<String[]> reader = new FlatFileItemReaderBuilder<String[]>()
        .delimited().delimiter(Optional.ofNullable(delimiter).orElse(","))
        .names(columnNames)
        .fieldSetMapper(FieldSet::getValues)
        .recordSeparatorPolicy(new DefaultRecordSeparatorPolicy())
        .encoding(encoding.name())
        .resource(new FileSystemResource(file.toFile()))
        .name("default")
        .linesToSkip(1).build();
    List<String[]> lines = new ArrayList<>();
    reader.open(new ExecutionContext());
    try {
      String[] items;
      while ((items = reader.read()) != null) {
        lines.add(items);
      }
    } finally {
      reader.close();
    }
    return lines;
  }

  protected void writeLines(List<String[]> lines, Path file, Charset encoding, String delimiter, Boolean ignoreEscapedEnclosure) throws Exception {
    EnclosableDelimitedLineAggregator<String[]> aggregator = new EnclosableDelimitedLineAggregator<>();
    aggregator.setIgnoreEscapedEnclosure(Optional.ofNullable(ignoreEscapedEnclosure).orElse(false));
    aggregator.setDelimiter(Optional.ofNullable(delimiter).orElse(",").toCharArray()[0]);
    aggregator.afterPropertiesSet();
    ItemStreamWriter<String[]> itemWriter = new FlatFileItemWriterBuilder<String[]>()
        .lineAggregator(aggregator)
        .encoding(encoding.name())
        .resource(new FileSystemResource(file.toFile()))
        .name("default")
        .shouldDeleteIfEmpty(false)
        .transactional(false)
        .build();
    itemWriter.open(new ExecutionContext());
    try {
      itemWriter.write(lines);
    } finally {
      itemWriter.close();
    }
  }

  private static class EnclosableDelimitedLineAggregator<T> extends ExtractorLineAggregator<T> implements InitializingBean {
    protected boolean ignoreEscapedEnclosure = false;
    private final String enclosure = "\"";
    private final String escapedEnclosure;
    private String delimiter;

    public EnclosableDelimitedLineAggregator() {
      this.escapedEnclosure = this.enclosure + this.enclosure;
      this.delimiter = ",";
    }

    public void setIgnoreEscapedEnclosure(boolean ignoreEscapedEnclosure) {
      this.ignoreEscapedEnclosure = ignoreEscapedEnclosure;
    }

    public void setDelimiter(char delimiter) {
      this.delimiter = String.valueOf(delimiter);
    }

    public void afterPropertiesSet() {
      if (this.enclosure.equals(this.delimiter)) {
        throw new IllegalStateException("the delimiter and enclosure must be different. [value:" + this.enclosure + "]");
      }
    }

    protected String doAggregate(Object[] fields) {
      return Arrays.stream(fields)
          .map(Object::toString)
          .map((field) -> this.hasTargetChar(field) ? this.encloseAndEscape(field) : field)
          .collect(Collectors.joining(this.delimiter));
    }

    private boolean hasTargetChar(String field) {
      return field.contains(this.delimiter) || field.contains(this.enclosure) || this.containsCrlf(field);
    }

    private boolean containsCrlf(String field) {
      return field.contains("\r") || field.contains("\n");
    }

    private String encloseAndEscape(String field) {
      return this.enclosure + (ignoreEscapedEnclosure ? field : field.replace(this.enclosure, this.escapedEnclosure)) + this.enclosure;
    }
  }

}