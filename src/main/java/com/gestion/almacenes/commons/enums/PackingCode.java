package com.gestion.almacenes.commons.enums;

public enum PackingCode {
  NA("n/a");
  private final String code;
  PackingCode(String code) {
    this.code = code;
  }
  public String getCode() {
    return code;
  }
}
