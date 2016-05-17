package com.fachrur_122.rxdabinmodule;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.fachrur_122.rxdabinmodule.Adapter.PlacesAdapter;
import com.fachrur_122.rxdabinmodule.Interface.GooglePlacesClient;
import com.fachrur_122.rxdabinmodule.databinding.ActivityMainBinding;
import com.google.gson.GsonBuilder;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity {

    private static final String GOOGLE_API_BASE_URL = "https://maps.googleapis.com";
    private static final int DEBOUNCE_IN_MS = 500;
    private GooglePlacesClient mGooglePlacesClient;
    private Subscription mEditTextSubscription;
    private PlacesAdapter mPlacesAdapter = new PlacesAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (APIManage.API_KEY.length() < 10) {
            Toast.makeText(this, "API KEY is unset!", Toast.LENGTH_LONG).show();
            return;
        }

        if (mGooglePlacesClient == null) {
            OkHttpClient client = new OkHttpClient();

            // workaround for SPDY HTTP/2 and OkHTTP
            client.setProtocols(Collections.singletonList(Protocol.HTTP_1_1));

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.interceptors().add(interceptor);

            mGooglePlacesClient = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(
                            new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                    .baseUrl(GOOGLE_API_BASE_URL)
                    .client(client)
                    .build()
                    .create(GooglePlacesClient.class);
        }

        ActivityMainBinding mainBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);
        mainBinding.suggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mainBinding.suggestionsRecyclerView.setAdapter(mPlacesAdapter);

        mEditTextSubscription =
                RxTextView.textChangeEvents(mainBinding.editText1)
                        .filter(event -> !event.text().toString().equals(""))
                        .doOnNext(textViewTextChangeEvent -> mPlacesAdapter.clear())
                        .debounce(DEBOUNCE_IN_MS, TimeUnit.MILLISECONDS)
                        .map(editText1 -> editText1.text().toString())
                        .flatMap(searchTerm -> {
                            try {
                                return mGooglePlacesClient.autocomplete(
                                        APIManage.API_KEY,
                                        URLEncoder.encode(searchTerm, "utf8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            return Observable.empty();
                        })
                        .flatMap(result -> Observable.from(result.predictions))
                        .flatMap(prediction -> {
                            try {
                                return mGooglePlacesClient.getDetail(
                                        APIManage.API_KEY,
                                        URLEncoder.encode(prediction.place_id, "utf8")
                                );
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            return Observable.empty();
                        })
                        .concatWith(Observable.never())
                        .onErrorResumeNext(throwable -> {
                            // This prevents errors from causing
                            // the observable to end.

                            // Put a handler here if you do this
                            return Observable.empty();
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {
                            mPlacesAdapter.addItem(result);
                        }, Throwable::printStackTrace);


    }

    @Override
    protected void onDestroy() {
        mEditTextSubscription.unsubscribe();
        super.onDestroy();
    }
}
