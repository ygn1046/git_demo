package com.ygn.springboot.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class R {
    private  int Code;
    private String message;

    private Object data;

    public static  R ok(){
        return  new R(200,"成功",null);
    }
    public static  R ok(Object data){
        return  new R(200,"成功",data);
    }
    public static  R fail(){
        return  new R(500,"成功",null);
    }
    public static  R fail(Object data){
        return  new R(500,"失败",data);
    }
}
