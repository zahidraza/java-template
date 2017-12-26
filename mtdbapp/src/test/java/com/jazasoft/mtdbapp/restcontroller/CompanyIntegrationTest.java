package com.jazasoft.mtdbapp.restcontroller;

/**
 * Created by mdzahidraza on 23/06/17.
 */

import com.jazasoft.mtdb.IApiUrls;
import com.jazasoft.mtdb.entity.Company;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
//@Ignore
public class CompanyIntegrationTest extends BaseTest{

    @Test
    public void getAllCompany() throws Exception {
        this.mvc.perform(get(IApiUrls.ROOT_URL_COMPANIES).header("Authorization", "Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.companies", hasSize(2)))
                .andExpect(jsonPath("$._embedded.companies[0].name", is("Laguna Clothing Pvt. Ltd.")))
                .andExpect(jsonPath("$._embedded.companies[0]._links.self").exists());

        this.mvc.perform(get(IApiUrls.ROOT_URL_COMPANIES).header("Authorization", "Bearer " + accessToken2))
                .andExpect(status().isForbidden());
    }


    @Test
    public void getCompany() throws Exception {
        this.mvc.perform(get(IApiUrls.ROOT_URL_COMPANIES + "/1").header("Authorization", "Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Laguna Clothing Pvt. Ltd.")))
                .andExpect(jsonPath("$._links.self").exists());

        this.mvc.perform(get(IApiUrls.ROOT_URL_COMPANIES + "/10").header("Authorization", "Bearer " + accessToken1))
                .andExpect(status().isNotFound());
    }


    @Test
    public void createCompany() throws Exception {
        Company company = new Company("Test Company", "test company description", "Test Address", "tnt_db_test");
        System.out.println("-$$$-" + mapper.writeValueAsString(company));
        MvcResult mvcResult = mvc.perform(post(IApiUrls.ROOT_URL_COMPANIES)
                .content(mapper.writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken1)
        )
                .andExpect(status().isCreated())
                .andReturn();
        String locationUri = mvcResult.getResponse().getHeader("Location");
        assertTrue(locationUri.contains(IApiUrls.ROOT_URL_COMPANIES));

        int idx = locationUri.lastIndexOf('/');
        String id = locationUri.substring(idx + 1);

        this.mvc.perform(get(IApiUrls.ROOT_URL_COMPANIES + "/{id}", id).header("Authorization", "Bearer " + accessToken1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.name", is("Test Company")));

    }

    @Test
    public void createCompanyBadRequest() throws Exception {
        Company company = new Company();
        this.mvc.perform(post(IApiUrls.ROOT_URL_COMPANIES)
                .content(mapper.writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization", "Bearer " + accessToken1)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(3)));

        //Test each fields one by one
        company = new Company("", "test description", "Bangalore", "tnd_db_test");
        this.mvc
                .perform(post(IApiUrls.ROOT_URL_COMPANIES)
                        .content(mapper.writeValueAsString(company))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization", "Bearer " + accessToken1)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("name")))
                .andExpect(jsonPath("$[0].message", containsString("length must be between 3")));

        company = new Company("Test Company", "test description", "", "tnd_db_test");
        this.mvc
                .perform(post(IApiUrls.ROOT_URL_COMPANIES)
                        .content(mapper.writeValueAsString(company))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization", "Bearer " + accessToken1)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("address")));

        company = new Company("Test Company", "test description", "Bangalore", "");
        this.mvc
                .perform(post(IApiUrls.ROOT_URL_COMPANIES)
                        .content(mapper.writeValueAsString(company))
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .header("Authorization", "Bearer " + accessToken1)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].field", is("dbName")));


    }

}

