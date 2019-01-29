package zesley.sergey.myapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAPIforUser {
    @GET("users/{user}")
    Call<RetrofitModel> loadUsers(@Path("user") String user);
}
