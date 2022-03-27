package com.project.siihomework.translator;

import com.project.siihomework.dto.AuthDto;
import com.project.siihomework.model.Auth;

public class AuthTranslator {

    public void toDto (Auth source, AuthDto destination){
        destination.setId(source.getId());
        destination.setActor(source.getActor());
        destination.setType(source.getType());
        destination.setData(source.getData());
    }

    public void fromDto (AuthDto source, Auth destination){
        destination.setId(source.getId());
        destination.setActor(source.getActor());
        destination.setType(source.getType());
        destination.setData(source.getData());
    }
}
