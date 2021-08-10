package com.example.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import java.io.IOException;

class CsvBulkCommandsApplicationRunnerTests {

  private final CsvBulkCommandsApplicationRunner runner = new CsvBulkCommandsApplicationRunner();

  @Test
  void addingColumns() throws IOException {
    String[] args = {"--command=adding-columns", "--files=aaa.csv,bbb.csv", "--column-names=x,y", "--column-values=100*0.1", "--dir=src/test/resources/data"};
    runner.run(new DefaultApplicationArguments(args));
  }

  @Test
  void deletingColumns() throws IOException {
    String[] args = {"--command=deleting-columns", "--files=aaa.csv,bbb.csv", "--column-names=c", "--dir=src/test/resources/data"};
    runner.run(new DefaultApplicationArguments(args));
  }

  @Test
  void updatingColumns() throws IOException {
    String[] args = {"--command=updating-columns", "--files=aaa.csv,bbb.csv", "--column-names=y,x", "--column-values='NULL','CURRENT_TIMESTAMP'", "--dir=src/test/resources/data"};
    runner.run(new DefaultApplicationArguments(args));
  }


  @Test
  void orderingColumns() throws IOException {
    String[] args = {"--command=ordering-columns", "--files=aaa.csv,bbb.csv", "--column-names=a,b,c,y,x", "--dir=src/test/resources/data"};
    runner.run(new DefaultApplicationArguments(args));
  }

  @Test
  void noArgs() throws IOException {
    String[] args = {};
    runner.run(new DefaultApplicationArguments(args));
  }

  @Test
  void h() throws IOException {
    String[] args = {"--h"};
    runner.run(new DefaultApplicationArguments(args));
  }

  @Test
  void help() throws IOException {
    String[] args = {"--help"};
    runner.run(new DefaultApplicationArguments(args));
  }
}
