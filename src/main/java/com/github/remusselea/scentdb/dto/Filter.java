package com.github.remusselea.scentdb.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Filter {

  private String key;
  private Object value;

  public Filter() {
  }

  public Filter(String key, Object value) {
    this.key = key;
    this.value = value;
  }
}