package ru.geekbrains.evgeniy.itunesplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class MainActivity extends AppCompatActivity {

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
                    Toast.makeText(MainActivity.this, getString(R.string.error_5_min_characters), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    App.getApi().getData(URLEncoder.encode(keywords, "UTF-8")).enqueue(new Callback<ModelResponse>() {
                        @Override
                        public void onResponse(Call<ModelResponse> call, Response<ModelResponse> response) {
                            modelResponceResultList.clear();
                            modelResponceResultList.addAll(response.body().getResults());
                            recyclerView.getAdapter().notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Call<ModelResponse> call, Throwable t) {
                            Toast.makeText(MainActivity.this, getString(R.string.error_network), Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
