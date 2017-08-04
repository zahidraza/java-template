package com.jazasoft.mtdbapp.restcontroller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jazasoft.mtdb.ApiUrls;
import com.jazasoft.mtdbapp.dto.OauthResponse;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mdzahidraza on 23/07/17.
 */
public class BaseTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper mapper;

    protected final String contentType = "application/hal+json;charset=UTF-8";

    protected String accessToken1 = "";
    protected String accessToken2 = "";

    @Before
    public void setUp() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "zahid7292");
        params.add("password", "admin");
        MvcResult mvcResult = mvc.perform(post(ApiUrls.OAUTH_URL)
                .params(params)
                .header("Authorization", "Basic Y2xpZW50OnNlY3JldA==")
        )
                .andExpect(status().isOk())
                .andReturn();
        String resp = mvcResult.getResponse().getContentAsString();
        OauthResponse oauthResponse = mapper.readValue(resp, OauthResponse.class);
        accessToken1 = oauthResponse.getAccess_token();

        params.clear();
        params.add("grant_type", "password");
        params.add("username", "taufeeque8");
        params.add("password", "admin");
        mvcResult = mvc
                .perform(post(ApiUrls.OAUTH_URL)
                        .params(params)
                        .header("Authorization", "Basic Y2xpZW50OnNlY3JldA==")
                )
                .andExpect(status().isOk())
                .andReturn();
        resp = mvcResult.getResponse().getContentAsString();
        oauthResponse = mapper.readValue(resp, OauthResponse.class);
        accessToken2 = oauthResponse.getAccess_token();
    }
}
