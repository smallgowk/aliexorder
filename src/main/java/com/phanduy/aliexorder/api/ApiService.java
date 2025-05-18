package com.phanduy.aliexorder.api;

import com.phanduy.aliexorder.model.request.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("getStoreInfos")
    Call<ApiResponse> checkTool(@Body CheckTool request);
}
