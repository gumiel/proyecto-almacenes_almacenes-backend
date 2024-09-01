package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.exception.AlreadyDeletedException;
import com.gestion.almacenes.commons.exception.DuplicateException;
import com.gestion.almacenes.commons.exception.EntityNotFound;
import com.gestion.almacenes.commons.exception.ValidationErrorException;
import com.gestion.almacenes.entities.StorehouseType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ExceptionsCustom {


  public static void errorProcess(String msg) {
    throw new ValidationErrorException(msg);
  }


  public static void errorEntityNotFoundInLine(Class<?> data,
      Integer id) {
    throw new EntityNotFound(data.getSimpleName(), id);
  }

  public static Supplier<? extends RuntimeException> errorEntityNotFound(Class<?> data,
                                                                         Integer id) {
    return () -> new EntityNotFound(data.getSimpleName(), id);
  }

  public static Supplier<? extends RuntimeException> errorEntityNotFound(Class<?> data,
      String attribute, String value) {
    return () -> new EntityNotFound(data.getSimpleName(), attribute, value);
  }

  public static void errorDuplicate(Class<?> data, String nameAttribute, String value) {
    throw new DuplicateException(data.getSimpleName(), nameAttribute, value);
  }

  public static void errorAlreadyDeleted(Class<?> data, Integer id) {
    throw new AlreadyDeletedException(data.getSimpleName(), id);
  }

  public static void errorDuplicateInFieldCode(Class<?> data, String nameAttribute, String value) {
    // Crear un BindingResult vacío
    BindingResult bindingResult = new BeanPropertyBindingResult(null, data.getSimpleName());
    bindingResult.addError(new FieldError(data.getSimpleName(), nameAttribute, "El código con valor ("+value+") ya existe."));

    // Si hay errores, lanzar la excepción
    if (bindingResult.hasErrors()) {
      try {
        throw new MethodArgumentNotValidException(getMethodParameterFiled("errorDuplicateInFieldCode"), bindingResult);
      } catch (MethodArgumentNotValidException e) {
        throw new RuntimeException(e);
      }
    }

  }

  public static void errorDuplicateInField(Class<?> data, String nameAttribute, String value) {
    // Crear un BindingResult vacío
    BindingResult bindingResult = new BeanPropertyBindingResult(null, data.getSimpleName());
    bindingResult.addError(new FieldError(data.getSimpleName(), nameAttribute, value));

    // Si hay errores, lanzar la excepción
    if (bindingResult.hasErrors()) {
      try {
        throw new MethodArgumentNotValidException(getMethodParameterFiled("errorDuplicateInField"), bindingResult);
      } catch (MethodArgumentNotValidException e) {
        throw new RuntimeException(e);
      }
    }

  }

  public static void miError(Class<?> data) {
    // Crear un BindingResult vacío
    BindingResult bindingResult = new BeanPropertyBindingResult(null, "usuario");
    bindingResult.addError(new FieldError("usuario", "codigo", "El codigo esta mal"));

    // Si hay errores, lanzar la excepción
    if (bindingResult.hasErrors()) {
      try {
        throw new MethodArgumentNotValidException(getMethodParameter(), bindingResult);
      } catch (MethodArgumentNotValidException e) {
        throw new RuntimeException(e);
      }
    }

  }

  /**
   * Correccion para el throw en las clases
   * @return
   */
  private static MethodParameter getMethodParameter() {
    try {
      // Suponiendo que el método 'miError' está en esta misma clase
      Method method = ExceptionsCustom.class.getMethod("miError", Class.class);

      // Crear el MethodParameter para el primer parámetro del método 'miError'
      return new MethodParameter(method, 0);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("No se pudo obtener el MethodParameter", e);
    }
  }

  /**
   * Correccion para el throw en las clases
   * @return
   */
  private static MethodParameter getMethodParameterFiled(String nameFunction) {
    try {
      // Suponiendo que el método 'miError' está en esta misma clase
      Method method = ExceptionsCustom.class.getMethod(nameFunction, Class.class, String.class, String.class);

      // Crear el MethodParameter para el primer parámetro del método 'miError'
      return new MethodParameter(method, 0);
    } catch (NoSuchMethodException e) {
      throw new RuntimeException("No se pudo obtener el MethodParameter", e);
    }
  }


}
