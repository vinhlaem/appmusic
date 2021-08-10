package com.example.appmusic.ConectionApi;

public class APIService {
    private  static String basr_url ="https://vinhmp3zing.000webhostapp.com/Server/";

    public static Dataservice getService(){
        return APIRetrofitClient.getClient(basr_url).create(Dataservice.class);
    }
}
