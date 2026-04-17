package com.gotrack.branch_service.mappers;

public interface Mapper<A,B> {

    B mapTo (A a);

    A mapFrom(B b);

}
