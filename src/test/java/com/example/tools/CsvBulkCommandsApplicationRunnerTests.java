package com.example.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;

import java.io.IOException;

class CsvBulkCommandsApplicationRunnerTests {

  private final CsvBulkCommandsApplicationRunner runner = new CsvBulkCommandsApplicationRunner();

  @Test
  void addingColumns() throws IOException {
    String[] args = {"--command=adding-columns", "--files=aaa.csv,bbb.csv", "--column-names=x,y", "--column-values=100*0.1,'abc'", "--dir=target/test-classes/data"};
    runner.run(new DefaultApplicationArguments(args));
  }

  @Test
  void addingColumns2() throws IOException {
    String[] args = {"--command=adding-columns", "--files=fff.csv", "--column-names=x,y", "--column-values=100*0.1,'abc'", "--dir=target/test-classes/data", "--first"};
    runner.run(new DefaultApplicationArguments(args));
  }

  @Test
  void addingColumns3() throws IOException {
    String[] args = {"--command=adding-columns", "--files=ggg.csv", "--column-names=x,y", "--column-values=100*0.1,'abc'", "--dir=target/test-classes/data", "--after=a"};
    runner.run(new DefaultApplicationArguments(args));
  }

  @Test
  void deletingColumns() throws IOException {
    String[] args = {"--command=deleting-columns", "--files=ccc.csv", "--column-names=b", "--dir=target/test-classes/data"};
    runner.run(new DefaultApplicationArguments(args));
  }

  @Test
  void updatingColumns() throws IOException {
    String[] args = {"--command=updating-columns", "--files=ddd.csv", "--column-names=y,x", "--column-values='NULL','CURRENT_TIMESTAMP'", "--dir=target/test-classes/data"};
    runner.run(new DefaultApplicationArguments(args));
  }


  @Test
  void orderingColumns() throws IOException {
    String[] args = {"--command=ordering-columns", "--files=eee.csv", "--column-names=a,b,c,y,x", "--dir=target/test-classes/data"};
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
