package com.example.retrofit2demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.example.retrofit2demo.api.GithubService;
import org.eclipse.egit.github.core.Repository;
import retrofit2.*;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.repo_list) ListView repoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Retrofit retro = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        GithubService github = retro.create(GithubService.class);

        github.listReposRx("octocat")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.immediate())
                .subscribe(list -> {
                    repoList.setAdapter(new RepoAdapter(MainActivity.this, list));
                }, Throwable::printStackTrace);

        /*
        Call<List<Repository>> reposCall = github.listRepos("octocat");
        reposCall.enqueue(new Callback<List<Repository>>() {
            @Override public void onResponse(Call<List<Repository>> call,
                                             Response<List<Repository>> response) {
                repoList.setAdapter(new RepoAdapter(MainActivity.this, response.body()));
            }

            @Override public void onFailure(Call<List<Repository>> call, Throwable t) {
                t.printStackTrace();
            }
        });//*/

    }

    public static class RepoAdapter extends ArrayAdapter<Repository> {

        public RepoAdapter(Context context, List<Repository> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
        }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            View v = super.getView(position, convertView, parent);
            Repository repo = getItem(position);
            ((TextView) v).setText(repo.getName());
            return v;
        }
    }
}
