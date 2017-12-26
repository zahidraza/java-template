package com.jazasoft.mtdbapp.restcontroller;

/**
 * Created by mdzahidraza on 23/06/17.
 */

import com.jazasoft.mtdb.IApiUrls;
import com.jazasoft.mtdb.dto.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
//@Ignore
public class UserIntegrationTest extends BaseTest{


    @Test
    public void getAllUser() throws Exception{
        this.mvc.perform(get(IApiUrls.ROOT_URL_USERS).header("Authorization","Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.users",hasSize(3)))
                .andExpect(jsonPath("$._embedded.users[0].name",is("Md Zahid Raza")))
                .andExpect(jsonPath("$._embedded.users[0]._links.self").exists());

        this.mvc.perform(get(IApiUrls.ROOT_URL_USERS).header("Authorization","Bearer " + accessToken2))
                .andExpect(status().isForbidden());
    }


    @Test
    public void getUser() throws Exception{
        this.mvc.perform(get(IApiUrls.ROOT_URL_USERS +"/1").header("Authorization","Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Md Zahid Raza")))
                .andExpect(jsonPath("$._links.self").exists());

        this.mvc.perform(get(IApiUrls.ROOT_URL_USERS +"/10").header("Authorization","Bearer " + accessToken1))
                .andExpect(status().isNotFound());
    }


    @Test
    public void createAndDeleteUser() throws Exception{
        UserDto user = new UserDto("Test UserDto","test_user", "test@gmail.com","8987525008");
        System.out.println("-$$$-" +mapper.writeValueAsString(user));
        MvcResult mvcResult = mvc.perform(post(IApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isCreated())
                .andReturn();
        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains(IApiUrls.ROOT_URL_USERS));

        int idx = locationUri.lastIndexOf('/');
        String id = locationUri.substring(idx+1);

        this.mvc.perform(get( IApiUrls.ROOT_URL_USERS +"/{id}",id).header("Authorization","Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Test UserDto")));

        this.mvc.perform(delete(IApiUrls.ROOT_URL_USERS + "/{id}", id).header("Authorization","Bearer " + accessToken1))
                .andExpect(status().isNoContent());

        this.mvc.perform(get(IApiUrls.ROOT_URL_USERS + "/{id}", id).header("Authorization","Bearer " + accessToken1))
                .andExpect(jsonPath("$.enabled", is(false)));
    }

    @Test
    public void createUserBadRequest() throws Exception{
        UserDto user = new UserDto();
        this.mvc.perform(post(IApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(4)));

        //Test each fields one by one
        user = new UserDto("","test_user", "test@gmail.com", "8987525008");
        this.mvc.perform(post(IApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", containsString("length must be between 3")));

        user = new UserDto("Md Zahid Raza","test user", "test@gmail.com", "8987525008");
        this.mvc.perform(post(IApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("username")));

        user = new UserDto("Md Zahid Raza","test_user", "test", "8987525008");
        this.mvc.perform(post(IApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("email")))
                .andExpect(jsonPath("$[0].message", containsString("Incorrect email")));


        user = new UserDto("Md Zahid Raza","test_user", "test@gmail.com", "8987525");
        this.mvc.perform(post(IApiUrls.ROOT_URL_USERS)
                .content(mapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("mobile")))
                .andExpect(jsonPath("$[0].message", containsString("mobile number should be 10 digit long")));

    }

}

