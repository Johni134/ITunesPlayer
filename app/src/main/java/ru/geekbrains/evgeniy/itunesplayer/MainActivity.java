package ru.geekbrains.evgeniy.itunesplayer;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String ITUNES_MEDIA_TYPE = "music";
    private Disposable disposable = null;
    RecyclerView recyclerView;
    List<ModelResponceResult> modelResponceResultList;
    EditText editTextKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modelResponceResultList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyvlerView_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        ResultAdapter resultAdapter = new ResultAdapter(modelResponceResultList);
        recyclerView.setAdapter(resultAdapter);

        editTextKeyword = findViewById(R.id.editText_keyword);

        Button buttonResult = findViewById(R.id.button_result);
        buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keywords = editTextKeyword.getText().toString();

                if(keywords.trim().length() < 5) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.error_5_min_characters), Toast.LENGTH_SHORT).show();
                    return;
                }

                tryToUnsubscribe();

                try {
                    disposable = App.getApi()
                            .getData(URLEncoder.encode(keywords, "UTF-8"), ITUNES_MEDIA_TYPE)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<ModelResponse>() {
                                @Override
                                public void accept(ModelResponse modelResponse) throws Exception {
                                    modelResponceResultList.clear();
                                    modelResponceResultList.addAll(modelResponse.getResults());
                                    recyclerView.scrollToPosition(0);
                                    recyclerView.getAdapter().notifyDataSetChanged();
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    if (throwable instanceof IOException) {
                                        Toast.makeText(MainActivity.this,
                                                getString(R.string.check_internet_connection),
                                                Toast.LENGTH_SHORT).show();
                                    } else if (throwable instanceof HttpException) {
                                        Toast.makeText(MainActivity.this,
                                                getString(R.string.server_error),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                getString(R.string.unknown_error),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                // hide keyboard
                InputMethodManager inputManager =
                        (InputMethodManager) MainActivity.this.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        MainActivity.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
    }

    private void tryToUnsubscribe() {
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    protected void onDestroy() {
        tryToUnsubscribe();
        super.onDestroy();
    }
}
