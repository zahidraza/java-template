package com.jazasoft.mtdbapp.util;

import com.jazasoft.mtdb.util.Utils;
import com.jazasoft.mtdb.util.YamlUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by mdzahidraza on 02/07/17.
 */
public class YamlUtilsTest {

    private YamlUtils yamlUtils;
    File testFile;

    @Before
    public void setUp() {
        yamlUtils = YamlUtils.getInstance();
        String filename = Utils.getAppHome() + File.separator + "conf" + File.separator + "test.yaml";
        testFile = new File(filename);
    }

    @Test
    public void testGetFileProprty() throws IOException{
        Assert.assertEquals("Md Zahid Raza",yamlUtils.getProperty(testFile, "name"));
        Assert.assertEquals("25",yamlUtils.getProperty(testFile, "age"));
        Assert.assertEquals("Bangalore",yamlUtils.getProperty(testFile, "address.city"));
        Assert.assertEquals("India",yamlUtils.getProperty(testFile, "address.country"));

        List list = (List)yamlUtils.getProperty(testFile, "phones");
        Assert.assertEquals(2, list.size());
        Assert.assertEquals("8987525008", ((Map)list.get(0)).get("number"));
        Assert.assertEquals("8904360418", ((Map)list.get(1)).get("number"));
    }


    @Test
    public void testNestedProperty() throws IOException{
        Map schema = (Map)yamlUtils.getProperty(testFile,"schema");
        Assert.assertEquals("schema-mysql.sql", yamlUtils.getNestedProperty(schema,"mysql.init.filename"));
        Assert.assertEquals("schema-postgresql.sql", yamlUtils.getNestedProperty(schema,"postgresql.init.filename"));
    }

}
