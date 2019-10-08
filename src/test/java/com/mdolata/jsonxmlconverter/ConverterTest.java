package com.mdolata.jsonxmlconverter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ConverterTest {

    private final String xml;
    private final String json;

    private final Main.Converter xmlConverter = new Main.XmlToJsonConverter();
    private final Main.Converter jsonConverter = new Main.JsonToXmlConverter();

    public ConverterTest(String xml, String json) {
        this.xml = xml;
        this.json = json;
    }

    @Parameterized.Parameters
    public static Collection fields(){
        return Arrays.asList(new Object[][]{
                {"<host>127.0.0.1</host>", "{\"host\":\"127.0.0.1\"}"},
                {"<jdk>1.8.9</jdk>", "{\"jdk\" : \"1.8.9\"}"},
                {"<success/>", "{\"success\": null }"},
                {"<storage/>", "{ \"storage\" : null }"},
                {"<employee department = \"manager\">Garry Smith</employee>", "{\n" +
                        "    \"employee\" : {\n" +
                        "        \"@department\" : \"manager\",\n" +
                        "        \"#employee\" : \"Garry Smith\"\n" +
                        "    }\n" +
                        "}"},
                {"<person rate = \"1\" name = \"Torvalds\" />", "{\n" +
                        "    \"person\" : {\n" +
                        "        \"@rate\" : \"1\",\n" +
                        "        \"@name\" : \"Torvalds\",\n" +
                        "        \"#person\" : null\n" +
                        "    }\n" +
                        "}"},
                {"<employee department = \"manager\">Garry Smith</employee>", "{\n" +
                        "    \"employee\" : {\n" +
                        "        \"@department\" : \"manager\",\n" +
                        "        \"#employee\" : \"Garry Smith\"\n" +
                        "    }\n" +
                        "}"},
                {"<person rate = \"1\" name = \"Torvalds\" />", "{\n" +
                        "    \"person\" : {\n" +
                        "        \"@rate\" : 1,\n" +
                        "        \"@name\" : \"Torvalds\",\n" +
                        "        \"#person\" : null\n" +
                        "    }\n" +
                        "}"}
        });
    }

    @Test
    public void shouldConvertXmlToJson() {
        String resultJson = xmlConverter.convert(xml);

        Assert.assertEquals(json.replaceAll(" ", ""), resultJson);
    }

    @Test
    public void shouldConvertJsonToXml() {
        String resultXml = jsonConverter.convert(json);

        Assert.assertEquals(xml, resultXml);
    }
}
