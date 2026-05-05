package com.gotrack.user_branch_service.mappers;

public interface BranchMapper<A,B> {

    B mapTo (A a);

    A mapFrom(B b);

}
