package com.example.retrofit2demo.api;

import java.util.List;

import org.eclipse.egit.github.core.Repository;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GithubService {

    @GET("users/{user}/repos?sort=pushed")
    Call<List<Repository>> listRepos(@Path("user") String user);

    @GET("users/{user}/repos?sort=pushed")
    Observable<List<Repository>> listReposRx(@Path("user") String user);
}
