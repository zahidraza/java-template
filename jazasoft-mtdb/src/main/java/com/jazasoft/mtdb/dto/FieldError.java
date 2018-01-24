package com.jazasoft.mtdb.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldError implements Error{
    private Integer row;
    private String field;
    private Object rejectedValue;
    private String message;

    public FieldError() {
    }

    public FieldError(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public FieldError(Integer row, String field, Object rejectedValue, String message) {
        this.row = row;
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}
