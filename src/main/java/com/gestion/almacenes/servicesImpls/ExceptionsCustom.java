package com.gestion.almacenes.servicesImpls;

import com.gestion.almacenes.commons.exception.EntityNotFound;
import com.gestion.almacenes.commons.exception.ValidationErrorException;

import java.util.function.Supplier;

public class ExceptionsCustom {


    public static void errorProcess(String msg) {
        throw new ValidationErrorException(msg);
    }



    public static Supplier<? extends RuntimeException> errorEntityNotFound(Class<?> data, Integer id) {
        return ()->new EntityNotFound(data.getSimpleName(), id);
    }

    public static Supplier<? extends RuntimeException> errorEntityNotFound(Class<?> data, String attribute, String value) {
        return ()->new EntityNotFound(data.getSimpleName(), attribute, value);
    }

}
