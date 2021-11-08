package com.gliese.noteedit.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Back {
    public Integer code;
    public String message;
    public Object obj;

    public Back(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
