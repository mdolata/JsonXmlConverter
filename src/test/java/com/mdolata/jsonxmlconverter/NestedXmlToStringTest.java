package com.mdolata.jsonxmlconverter;

import com.mdolata.jsonxmlconverter.model.Element;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class NestedXmlToStringTest {


    private final String xml;
    private final String expected;


    private final Main.Converter xmlConverter = new Main.XmlToJsonConverter();

    public NestedXmlToStringTest(String xml, String expected) {
        this.xml = xml;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection fields() {
        return Arrays.asList(new Object[][]{
                {"<transaction>\n" +
                        "    <id>6753322</id>\n" +
                        "    <number region=\"Russia\">8-900-000-00-00</number>\n" +
                        "    <nonattr />\n" +
                        "    <nonattr></nonattr>\n" +
                        "    <nonattr>text</nonattr>\n" +
                        "    <attr id=\"1\" />\n" +
                        "    <attr id=\"2\"></attr>\n" +
                        "    <attr id=\"3\">text</attr>\n" +
                        "    <email>\n" +
                        "        <to>to_example@gmail.com</to>\n" +
                        "        <from>from_example@gmail.com</from>\n" +
                        "        <subject>Project discussion</subject>\n" +
                        "        <body font=\"Verdana\">Body message</body>\n" +
                        "        <date day=\"12\" month=\"12\" year=\"2018\"/>\n" +
                        "    </email>\n" +
                        "</transaction>", "Element:\n" +
                        "path = transaction\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, id\n" +
                        "value = \"6753322\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, number\n" +
                        "value = \"8-900-000-00-00\"\n" +
                        "attributes:\n" +
                        "region = \"Russia\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, nonattr\n" +
                        "value = null\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, nonattr\n" +
                        "value = \"\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, nonattr\n" +
                        "value = \"text\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, attr\n" +
                        "value = null\n" +
                        "attributes:\n" +
                        "id = \"1\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, attr\n" +
                        "value = \"\"\n" +
                        "attributes:\n" +
                        "id = \"2\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, attr\n" +
                        "value = \"text\"\n" +
                        "attributes:\n" +
                        "id = \"3\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, email\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, email, to\n" +
                        "value = \"to_example@gmail.com\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, email, from\n" +
                        "value = \"from_example@gmail.com\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, email, subject\n" +
                        "value = \"Project discussion\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, email, body\n" +
                        "value = \"Body message\"\n" +
                        "attributes:\n" +
                        "font = \"Verdana\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = transaction, email, date\n" +
                        "value = null\n" +
                        "attributes:\n" +
                        "day = \"12\"\n" +
                        "month = \"12\"\n" +
                        "year = \"2018\""},

                {"<node>\n" +
                        "    <child name = \"child_name1\" type = \"child_type1\">\n" +
                        "        <subchild id = \"1\" auth=\"auth1\">Value1</subchild>\n" +
                        "    </child>\n" +
                        "    <child name = \"child_name2\" type = \"child_type2\">\n" +
                        "        <subchild id = \"2\" auth=\"auth1\">Value2</subchild>\n" +
                        "        <subchild id = \"3\" auth=\"auth2\">Value3</subchild>\n" +
                        "        <subchild id = \"4\" auth=\"auth3\"></subchild>\n" +
                        "        <subchild id = \"5\" auth=\"auth3\"/>\n" +
                        "    </child>\n" +
                        "</node>", "Element:\n" +
                        "path = node\n" +
                        " \n" +
                        "Element:\n" +
                        "path = node, child\n" +
                        "attributes:\n" +
                        "name = \"child_name1\"\n" +
                        "type = \"child_type1\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = node, child, subchild\n" +
                        "value = \"Value1\"\n" +
                        "attributes:\n" +
                        "id = \"1\"\n" +
                        "auth = \"auth1\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = node, child\n" +
                        "attributes:\n" +
                        "name = \"child_name2\"\n" +
                        "type = \"child_type2\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = node, child, subchild\n" +
                        "value = \"Value2\"\n" +
                        "attributes:\n" +
                        "id = \"2\"\n" +
                        "auth = \"auth1\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = node, child, subchild\n" +
                        "value = \"Value3\"\n" +
                        "attributes:\n" +
                        "id = \"3\"\n" +
                        "auth = \"auth2\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = node, child, subchild\n" +
                        "value = \"\"\n" +
                        "attributes:\n" +
                        "id = \"4\"\n" +
                        "auth = \"auth3\"\n" +
                        " \n" +
                        "Element:\n" +
                        "path = node, child, subchild\n" +
                        "value = null\n" +
                        "attributes:\n" +
                        "id = \"5\"\n" +
                        "auth = \"auth3\""},
                {"<node><child name=\"child_name1\" type=\"child_type1\"><subchild id=\"1\" auth=\"auth1\">Value1</subchild></child><child name=\"child_name2\" type=\"child_type2\"><subchild id=\"2\" auth=\"auth1\">Value2</subchild><subchild id=\"3\" auth=\"auth2\">Value3</subchild><subchild id=\"4\" auth=\"auth3\"></subchild><subchild id=\"5\" auth=\"auth3\"/></child></node>",
                        "Element:\n" +
                                "path = node\n" +
                                " \n" +
                                "Element:\n" +
                                "path = node, child\n" +
                                "attributes:\n" +
                                "name = \"child_name1\"\n" +
                                "type = \"child_type1\"\n" +
                                " \n" +
                                "Element:\n" +
                                "path = node, child, subchild\n" +
                                "value = \"Value1\"\n" +
                                "attributes:\n" +
                                "id = \"1\"\n" +
                                "auth = \"auth1\"\n" +
                                " \n" +
                                "Element:\n" +
                                "path = node, child\n" +
                                "attributes:\n" +
                                "name = \"child_name2\"\n" +
                                "type = \"child_type2\"\n" +
                                " \n" +
                                "Element:\n" +
                                "path = node, child, subchild\n" +
                                "value = \"Value2\"\n" +
                                "attributes:\n" +
                                "id = \"2\"\n" +
                                "auth = \"auth1\"\n" +
                                " \n" +
                                "Element:\n" +
                                "path = node, child, subchild\n" +
                                "value = \"Value3\"\n" +
                                "attributes:\n" +
                                "id = \"3\"\n" +
                                "auth = \"auth2\"\n" +
                                " \n" +
                                "Element:\n" +
                                "path = node, child, subchild\n" +
                                "value = \"\"\n" +
                                "attributes:\n" +
                                "id = \"4\"\n" +
                                "auth = \"auth3\"\n" +
                                " \n" +
                                "Element:\n" +
                                "path = node, child, subchild\n" +
                                "value = null\n" +
                                "attributes:\n" +
                                "id = \"5\"\n" +
                                "auth = \"auth3\""}
        });
    }

    @Test
    public void shouldConvertXmlToJson() {
        List<Element> resultJson = xmlConverter.convert2Elements(xml, "");

        String actual = resultJson.stream()
                .map(Element::toString)
                .collect(Collectors.joining(" \n"))
                .trim();

        Assert.assertEquals(expected, actual);
    }
}

