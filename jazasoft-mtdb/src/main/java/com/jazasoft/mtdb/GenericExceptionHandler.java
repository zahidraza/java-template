package com.jazasoft.mtdb;

import com.jazasoft.mtdb.dto.FieldError;
import com.jazasoft.mtdb.dto.RestError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.List;
import java.util.stream.Collectors;

//@ControllerAdvice
public class GenericExceptionHandler {
    
    protected final Logger logger = LoggerFactory.getLogger(GenericExceptionHandler.class);

    @Autowired
    protected MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> processValidationError(MethodArgumentNotValidException ex) {
        logger.debug("processValidationError()");
        BindingResult result = ex.getBindingResult();
        return new ResponseEntity<>(processFieldError(result.getFieldErrors()), HttpStatus.BAD_REQUEST);
    }

    protected List<FieldError> processFieldError(List<org.springframework.validation.FieldError> fieldErrors) {
        List<FieldError> errors = fieldErrors.stream()
                .map(error -> {
                    if (error.getField().contains("list")) {
                        return new FieldError(error.getField(),
                                error.getRejectedValue(),
                                messageSource.getMessage(error.getCodes()[1], null, error.getDefaultMessage(), LocaleContextHolder.getLocale())
                        );
                    }else {
                        return new FieldError(error.getField(),
                                error.getRejectedValue(),
                                messageSource.getMessage(error.getCodes()[0], null, error.getDefaultMessage(), LocaleContextHolder.getLocale())
                        );
                    }

                })
                .collect(Collectors.toList());
        return errors;
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMessageNotReadable(HttpMessageNotReadableException e) {
        return response(HttpStatus.BAD_REQUEST, 400, "Request body cannot be empty.", e.getMessage(), "");
    }

    @ExceptionHandler
    public ResponseEntity<?> handleConflict(DataIntegrityViolationException e) {
    	String cause = e.getRootCause().getMessage();
        if(cause.toLowerCase().contains("cannot delete")){
    		return response(HttpStatus.CONFLICT, 40903, "Deletion restricted to prevent content inconsistency.", e.getRootCause().getMessage(), "");
    	}
        return response(HttpStatus.CONFLICT, 40900, "Operation cannot be performed. Integrity Constraint violated.", e.getRootCause().getMessage(), "");
    }

//    @ExceptionHandler
//    ResponseEntity<?> handleRestTemplateHttpStatusCodeException(HttpStatusCodeException e) {
//        e.printStackTrace();
//        return response(e.getStatusCode(), e.getStatusCode().value(), e.getResponseBodyAsString());
//    }

    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception e) {
        logger.debug("handleException: {}",e.getMessage());
        e.printStackTrace();
        return response(HttpStatus.INTERNAL_SERVER_ERROR, 500, e.getMessage(), e.getMessage(), "");
    }

    protected ResponseEntity<RestError> response(HttpStatus status, int code, String msg) {
        return response(status, code, msg, "", "");
    }

    protected ResponseEntity<RestError> response(HttpStatus status, int code, String msg, String devMsg, String moreInfo) {
        return new ResponseEntity<>(new RestError(status.value(), code, msg, devMsg, moreInfo), status);
    }

}
