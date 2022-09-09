package com.grash.utils;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import java.util.UUID;

public class Helper {

    public String generateString() {
        return UUID.randomUUID().toString();
    }

    public HttpHeaders getPagingHeaders(Page<?> page, int size, String name){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Range", name+ (page.getNumber()-1)*size+"-"+page.getNumber()*size+"/"+page.getTotalElements());
        responseHeaders.set("Access-Control-Expose-Headers","Content-Range");
        return responseHeaders;
    }

}
