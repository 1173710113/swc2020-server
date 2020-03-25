package com.example.demo.utils;

import org.junit.jupiter.api.Test;

class LtpFetcherTest {

  @Test
  void testFetch() {
    LtpFetcher fetcher = new LtpFetcher("5e202b46", "e7629c16783704d67f2bf2343c73ec45");
    System.out.println(fetcher.fetch("cws", "请自班同学星期五在体育馆集合。"));
  }

}
