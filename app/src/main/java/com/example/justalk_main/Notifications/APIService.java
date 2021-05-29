package com.example.justalk_main.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAACxvewUc:APA91bFXrrAlS769DbyJKHqN9RSNuHt0Sbvnbw_0KjD1qOOq7lN9iBQeYeGzpFL7jHL3IfI550Ef4gfszgLJh5Gn1N_k8Me4tjBoaSRdy6GbkTeSzOV7FruZ1alvnJxTJrZx3R2lovpL"

    })
    @POST("fcm/send")
    Call<Response> sendNotofication(@Body Sender body);




}
