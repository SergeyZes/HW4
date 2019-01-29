package zesley.sergey.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private TextView mInfoTextView;
    private ProgressBar progressBar;
    private EditText editText;
    private Button btnLoad;
    private Button btnLoadOne;
    private Button btnLoadReps;
    RestAPI restAPI;
    RestAPIforUser restAPIforUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeUi();
    }

    private void makeUi() {
        editText = findViewById(R.id.edit_text);
        mInfoTextView = findViewById(R.id.tv_load);
        progressBar = findViewById(R.id.progress_bar);
        btnLoad = findViewById(R.id.btn_load);
        btnLoad.setOnClickListener(v -> onClick());
        btnLoadOne = findViewById(R.id.btn_load_one);
        btnLoadOne.setOnClickListener(v -> onClickOne());
        btnLoadReps = findViewById(R.id.btn_load_reps);
        btnLoadReps.setOnClickListener(v -> onClickReps());
    }

    private void onClick() {
        mInfoTextView.setText("");
        Retrofit retrofit = null;
        try {
            retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(GsonConverterFactory.create())
                    .build();
            restAPI = retrofit.create(RestAPI.class);

        } catch (Exception io) {
            mInfoTextView.setText("no retrofit: " + io.getMessage());
            return;
        }

        Call<List<RetrofitModel>> call = restAPI.loadUsers();
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                progressBar.setVisibility(View.VISIBLE);
                downloadOneUrl(call);
            } catch (Exception e) {
                mInfoTextView.setText(e.getMessage());
            }
        } else Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();


    }

    private void onClickOne() {
        mInfoTextView.setText("");
        Retrofit retrofit = null;
        try {
            retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(GsonConverterFactory.create())
                    .build();
            restAPIforUser = retrofit.create(RestAPIforUser.class);

        } catch (Exception io) {
            mInfoTextView.setText("no retrofit: " + io.getMessage());
            return;
        }

        Call<RetrofitModel> call = restAPIforUser.loadUsers(editText.getText().toString());
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                progressBar.setVisibility(View.VISIBLE);
                downloadOneUser(call);
            } catch (Exception e) {
                mInfoTextView.setText(e.getMessage());
            }
        } else Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();


    }


    private void downloadOneUrl(Call<List<RetrofitModel>> call) throws IOException {
        call.enqueue(new Callback<List<RetrofitModel>>() {
            @Override
            public void onResponse(Call<List<RetrofitModel>> call, Response<List<RetrofitModel>> response) {
                if (response != null && response.isSuccessful()) {
                    RetrofitModel curRetrofitModel = null;
                    for (int i = 0; i < response.body().size(); i++) {
                        curRetrofitModel = response.body().get(i);
                        mInfoTextView.append("\nLogin=" + curRetrofitModel.getLogin() + "\nId=" + curRetrofitModel.getId() +
                                "\nURI=" + curRetrofitModel.getAvatarUrl() + "\n--------");
                    }
                } else if (response != null)
                    mInfoTextView.setText("onResponse error: " + response.code());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<RetrofitModel>> call, Throwable t) {
                mInfoTextView.setText("onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void downloadOneUser(Call<RetrofitModel> call) throws IOException {
        call.enqueue(new Callback<RetrofitModel>() {
            @Override
            public void onResponse(Call<RetrofitModel> call, Response<RetrofitModel> response) {
                if (response != null && response.isSuccessful()) {
                    RetrofitModel curRetrofitModel = null;
                    curRetrofitModel = response.body();
                    mInfoTextView.append("\nLogin=" + curRetrofitModel.getLogin() + "\nId=" + curRetrofitModel.getId() +
                            "\nURI=" + curRetrofitModel.getAvatarUrl() + "\n--------");

                } else if (response != null)
                    mInfoTextView.setText("onResponse error: " + response.code());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<RetrofitModel> call, Throwable t) {
                mInfoTextView.setText("onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    private void onClickReps() {
        mInfoTextView.setText("");
        Retrofit retrofit = null;
        try {
            retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").addConverterFactory(GsonConverterFactory.create())
                    .build();
            restAPIforUser = retrofit.create(RestAPIforUser.class);

        } catch (Exception io) {
            mInfoTextView.setText("no retrofit: " + io.getMessage());
            return;
        }

        Call<List<ReposModel>> call = restAPIforUser.loadRepositories(editText.getText().toString());
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                progressBar.setVisibility(View.VISIBLE);
                downloadOneUserReps(call);
            } catch (Exception e) {
                mInfoTextView.setText(e.getMessage());
            }
        } else Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();


    }

    private void downloadOneUserReps(Call<List<ReposModel>> call) throws IOException {
        call.enqueue(new Callback<List<ReposModel>>() {
            @Override
            public void onResponse(Call<List<ReposModel>> call, Response<List<ReposModel>> response) {
                if (response != null && response.isSuccessful()) {
                    ReposModel curRetrofitModel = null;
                    for (int i = 0; i < response.body().size(); i++) {
                        curRetrofitModel = response.body().get(i);
                        mInfoTextView.append("\nName=" + curRetrofitModel.getName() + "\nId=" + curRetrofitModel.getId() +
                                "\nFullName=" + curRetrofitModel.getFull_name() + "\n--------");
                    }
                } else if (response != null)
                    mInfoTextView.setText("onResponse error: " + response.code());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<ReposModel>> call, Throwable t) {
                mInfoTextView.setText("onFailure " + t.getMessage());
                progressBar.setVisibility(View.GONE);

            }
        });

    }


}
