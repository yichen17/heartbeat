package com.yichen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Qiuxinchao
 * @version 1.0
 * @date 2021/8/10 15:39
 * @describe
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnT<T> implements Serializable {
    public static final long serialVersionUID = 42L;

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;
    public static final int MISSING_PARAM = 600;
    private int code;
    private String msg;
    private T content;

    public ReturnT(T content){
        this.code = SUCCESS_CODE;
        this.content = content;
    }


}
