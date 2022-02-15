package com.sourabh.demorxjava;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserAPI {
     @GET("users")
    Observable<List<UserInfo>> getUsers();
}
