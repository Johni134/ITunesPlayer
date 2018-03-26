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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String ITUNES_MEDIA_TYPE = "music";
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

                try {
                    App.getApi()
                            .getData(URLEncoder.encode(keywords, "UTF-8"), ITUNES_MEDIA_TYPE)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<ModelResponse>() {
                                @Override
                                public void call(ModelResponse modelResponse) {
                                    modelResponceResultList.clear();
                                    modelResponceResultList.addAll(modelResponse.getResults());
                                    recyclerView.scrollToPosition(0);
                                    recyclerView.getAdapter().notifyDataSetChanged();
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
}
