package com.bobo.dependencies;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class ParserUtilTest {

    @Test
    public void testReadInputString() throws IOException {
        String filePath = getFilePath("input.txt");

        Map<String, List<String>> input = ParserUtil.readInput(filePath);
        Assert.assertNotNull(input);
        Assert.assertEquals(6, input.size());

        System.out.println("Input:");
        ParserUtil.printData(input);
    }

    @Test
    public void testReadInputReader() throws IOException {
        try (InputStream inpStream = DependencyAnalyzer.class.getClassLoader().getResourceAsStream("input.txt")) {
            Map<String, List<String>> input = ParserUtil.readInput(new InputStreamReader(inpStream));
            Assert.assertNotNull(input);
            Assert.assertEquals(6, input.size());

            System.out.println("Input:");
            ParserUtil.printData(input);
        }
    }

    public static String getFilePath(String inpFileName) throws FileNotFoundException {
        URL resource = DependencyAnalyzer.class.getClassLoader().getResource(inpFileName);
        if (resource == null) {
            throw new FileNotFoundException("The file '" + inpFileName + "' cannot be found.");
        }
        String filePath = resource.getFile();
        return filePath;
    }

    @Test
    public void testReadData_ValidateContent() throws IOException {
        String filePath = getFilePath("input_with_cycle.txt");

        Map<String, List<String>> input = ParserUtil.readInput(filePath);
        Assert.assertNotNull(input);
        Assert.assertEquals(2, input.size());

        Assert.assertTrue(input.containsKey("a"));
        List<String> list = input.get("a");
        Assert.assertEquals(1, list.size());
        Assert.assertTrue(list.contains("b"));

        list = input.get("b");
        Assert.assertEquals(2, list.size());
        Assert.assertTrue(list.contains("a"));
        Assert.assertTrue(list.contains("c"));
    }

}
