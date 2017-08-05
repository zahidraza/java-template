package com.jazasoft.mtdbapp;

import com.jazasoft.mtdb.entity.UrlInterceptor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mdzahidraza on 06/08/17.
 */
public class UnitTest {

    @Test
    public void testGetPermissions() {
        List<UrlInterceptor> list = new ArrayList<>();
        list.add(new UrlInterceptor("/api/users","GET"));
        list.add(new UrlInterceptor("/api/roles","post"));
        list.add(new UrlInterceptor("/api/users","Put"));
        list.add(new UrlInterceptor("/api/roles","GET"));



    }
}
