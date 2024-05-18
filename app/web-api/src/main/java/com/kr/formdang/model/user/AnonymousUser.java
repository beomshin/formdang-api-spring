package com.kr.formdang.model.user;

public class AnonymousUser implements FormUser{

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }

}
