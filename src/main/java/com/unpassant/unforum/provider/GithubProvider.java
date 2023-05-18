package com.unpassant.unforum.provider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unpassant.unforum.dto.AccessTokenDTO;
import com.unpassant.unforum.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;


@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

            //JACKSON把JSON对象转成string对象
            ObjectMapper mapper = new ObjectMapper();
            String JSON = null;
            try {
                JSON = mapper.writeValueAsString(accessTokenDTO);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        RequestBody body = RequestBody.create(mediaType, JSON);
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                 //System.out.println("2"+string);

                //解析得到的string 得到token
                String accessToken = string.split("&")[0].split("=")[1];
                 //System.out.println("3"+accessToken);
                return accessToken;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }

    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .addHeader("Authorization",Credentials.basic("Bearer OAUTH-TOKEN",accessToken))
                .build();
        try {
            Response response = client.newCall(request).execute();
            String userString = response.body().string();
             //System.out.println("5"+userString);

            //JACKSON把string对象转成GithubUser类对象
            ObjectMapper mapper = new ObjectMapper();
            GithubUser githubUser = mapper.readValue(userString, GithubUser.class);

            return githubUser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
