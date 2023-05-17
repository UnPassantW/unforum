package com.unpassant.unforum.controller;

import com.unpassant.unforum.dto.AccessTokenDTO;
import com.unpassant.unforum.dto.GithubUser;
import com.unpassant.unforum.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.client.Redirect_uri}")
    private String clientRedirectUrl;

    @GetMapping("/callback")
    public String callback(@RequestParam(name="code") String code,
                           @RequestParam(name="state") String state){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(clientRedirectUrl);
        accessTokenDTO.setState(state);
        System.out.println("1"+accessTokenDTO);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println("4"+accessToken);
        GithubUser user = githubProvider.getUser(accessToken);
        System.out.println("6"+user);
        return "index";
    }
}
