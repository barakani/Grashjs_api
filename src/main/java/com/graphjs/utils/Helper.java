package com.graphjs.utils;


import campus.exception.CustomException;
import campus.model.User;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

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

    public void checkSchool(User user) throws CustomException{
        if(user.getSchool()==null){
            throw new CustomException("Needs approbation from admins", HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
