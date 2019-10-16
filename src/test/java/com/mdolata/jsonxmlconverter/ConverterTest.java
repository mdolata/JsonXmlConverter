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
    public static Collection fields() {
        return Arrays.asList(new Object[][]{
                {"<host>127.0.0.1</host>", "{" +
                        " \"host\" : { " +
                        "       \"#host\" : \"127.0.0.1\" " +
                        "   }" +
                        "}"},
                {"<jdk>1.8.9</jdk>", "{" +
                        " \"jdk\" : { " +
                        "       \"#jdk\" : \"1.8.9\" " +
                        "   }" +
                        "}"},
                {"<storage/>", "{" +
                        " \"storage\" : { " +
                        "       \"#storage\" : null " +
                        "   }" +
                        "}"},
                {"<employee department = \"manager\">Garry Smith</employee>", "{" +
                        "    \"employee\" : {" +
                        "        \"@department\" : \"manager\"," +
                        "        \"#employee\" : \"Garry Smith\"" +
                        "    }" +
                        "}"},
                {"<person rate = \"1\" name = \"Torvalds\" />", "{" +
                        "    \"person\" : {" +
                        "        \"@rate\" : \"1\"," +
                        "        \"@name\" : \"Torvalds\"," +
                        "        \"#person\" : null" +
                        "    }" +
                        "}"},
                {"<employee department = \"manager\">Garry Smith</employee>", "{" +
                        "    \"employee\" : {" +
                        "        \"@department\" : \"manager\"," +
                        "        \"#employee\" : \"Garry Smith\"" +
                        "    }" +
                        "}"},
               // json to xml only
                {"<person rate = \"1\" name = \"Torvalds\" />", "{" +
                        "    \"person\" : {" +
                        "        \"@rate\" : 1," +
                        "        \"@name\" : \"Torvalds\"," +
                        "        \"#person\" : null" +
                        "    }" +
                        "}"}
        });
    }

    @Test
    public void shouldConvertXmlToJson() {
        String resultJson = xmlConverter.convert(xml);


        Assert.assertEquals(replaceUnneededSpaces(json), resultJson);
    }

    @Test
    public void shouldConvertJsonToXml() {
        String resultXml = jsonConverter.convert(json);

        Assert.assertEquals(xml, resultXml);
    }

    private String replaceUnneededSpaces(String json) {
        StringBuilder result = new StringBuilder();
        boolean inQuotes = false;
        String[] jsonSplit = json.split("");
        for (int i = 0; i < jsonSplit.length; i++) {
            String currentValue = jsonSplit[i];
            if (currentValue.equals("\"")) {
                inQuotes = !inQuotes;
                result.append(currentValue);
            } else if (currentValue.equals(" ") && inQuotes) {
                result.append(currentValue);
            } else if (!currentValue.equals(" ")) {
                result.append(currentValue);
            }

        }
        return result.toString();
    }

}
