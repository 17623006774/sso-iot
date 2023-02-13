package com.example.iot.vo;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ResponseEntity {

    private Integer status;
    private  Object data;
    private String msg;

    public static ResponseEntity ok(String msg){
        return ResponseEntity.builder()
                .status(200)
                .msg(msg)
                .build();
    }

    /**
     * 默认
     * @return
     */
    public static ResponseEntity ok(){
        return ResponseEntity.builder()
                .status(200)
                .msg("success")
                .build();
    }

    public static ResponseEntity ok(Object data){

        return ResponseEntity.builder()
                .status(200)
                .msg("success")
                .data(data)
                .build();
    }


    public static  ResponseEntity error(String msg){
        return ResponseEntity.builder()
                .status(400)
                .msg("error").build();
    }
}
