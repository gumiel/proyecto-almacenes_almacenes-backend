package com.gestion.almacenes.commons.exception;

import com.gestion.almacenes.commons.exception.response.FieldErrorModel;
import java.util.List;
import lombok.Getter;

@Getter
public class ErrorListException extends RuntimeException {

  private final List<FieldErrorModel> errorList;

  public ErrorListException(List<FieldErrorModel> errorList) {
    this.errorList = errorList;
  }
}

