package com.whoamizq.codebbs.codebbs.provider;

import com.alibaba.fastjson.JSON;
import com.whoamizq.codebbs.codebbs.dto.AccessTokenDTO;
import com.whoamizq.codebbs.codebbs.dto.GithubUser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * GitHub授权信息
 */
@Component
@Slf4j
public class GithubProvider {
    /**
     * 使用OkHttp发送post请求到GitHub授权地址
     * @param accessTokenDTO
     * @return
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            String token = string.split("&")[0].split("=")[1];
//            System.out.println(token);
            return token;
        } catch (Exception e) {
            log.error("getAccessToken error,{}", accessTokenDTO, e);
        }
        return null;
    }

    /**
     * 通过GitHub返回的accessToken获取GitHub用户信息
     * @param accessToken
     * @return
     */
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();

//        Request request = new Request.Builder()
//                .header("Authorization","token"+accessToken)
//                .url("https://api.github.com/user")
//                .build();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try{
            Response response = client.newCall(request).execute();
            String str = response.body().string();
            GithubUser githubUser = JSON.parseObject(str, GithubUser.class);
            return githubUser;
        }catch (IOException e){
            log.error("getUser error,{}", accessToken, e);
        }
        return null;
    }
}
