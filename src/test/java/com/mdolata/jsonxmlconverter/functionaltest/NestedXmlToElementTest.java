package com.mdolata.jsonxmlconverter.functionaltest;

import com.mdolata.jsonxmlconverter.converter.Converter;
import com.mdolata.jsonxmlconverter.converter.XmlToJsonConverter;
import com.mdolata.jsonxmlconverter.model.Attribute;
import com.mdolata.jsonxmlconverter.model.Element;
import com.mdolata.jsonxmlconverter.model.ElementFactory;
import com.mdolata.jsonxmlconverter.model.Value;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class NestedXmlToElementTest {

    private final String xml;
    private final List<Element> expectedElements;


    private final Converter xmlConverter = new XmlToJsonConverter();

    public NestedXmlToElementTest(String xml, List<Element> expectedElements) {
        this.xml = xml;
        this.expectedElements = expectedElements;
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
                        "</transaction>", List.of(
                        ElementFactory.fromPathAndValue("transaction", Value.withXmlValue("\n" +
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
                                "")),
                        ElementFactory.fromPathAndValue("transaction, id", Value.withRawValue("6753322")),
                        ElementFactory.fromAll("transaction, number", Value.withRawValue("8-900-000-00-00"), List.of(new Attribute("region", "Russia"))),
                        ElementFactory.fromPathAndValue("transaction, nonattr", Value.withRawValue(null)),
                        ElementFactory.fromPathAndValue("transaction, nonattr", Value.withRawValue("")),
                        ElementFactory.fromPathAndValue("transaction, nonattr", Value.withRawValue("text")),
                        ElementFactory.fromAll("transaction, attr", Value.withRawValue(null), List.of(new Attribute("id", "1"))),
                        ElementFactory.fromAll("transaction, attr", Value.withRawValue(""), List.of(new Attribute("id", "2"))),
                        ElementFactory.fromAll("transaction, attr", Value.withRawValue("text"), List.of(new Attribute("id", "3"))),
                        ElementFactory.fromPathAndValue("transaction, email", Value.withXmlValue("\n" +
                                        "        <to>to_example@gmail.com</to>\n" +
                                        "        <from>from_example@gmail.com</from>\n" +
                                        "        <subject>Project discussion</subject>\n" +
                                        "        <body font=\"Verdana\">Body message</body>\n" +
                                        "        <date day=\"12\" month=\"12\" year=\"2018\"/>\n" +
                                "    "
                        )),
                        ElementFactory.fromPathAndValue("transaction, email, to", Value.withRawValue("to_example@gmail.com")),
                        ElementFactory.fromPathAndValue("transaction, email, from", Value.withRawValue("from_example@gmail.com")),
                        ElementFactory.fromPathAndValue("transaction, email, subject", Value.withRawValue("Project discussion")),
                        ElementFactory.fromAll("transaction, email, body", Value.withRawValue("Body message"), List.of(new Attribute("font", "Verdana"))),
                        ElementFactory.fromAll("transaction, email, date", Value.withRawValue(null), List.of(new Attribute("day", "12"), new Attribute("month", "12"), new Attribute("year", "2018")))
                )},
                {"<Node/>",
                        List.of(
                                ElementFactory.fromPath("Node")
                        )},
                {"<Node>" +
                        "<Child/>" +
                        "</Node>",
                        List.of(
                                ElementFactory.fromPathAndValue("Node", Value.withXmlValue("<Child/>")),
                                ElementFactory.fromPath("Node, Child")
                        )},
                {"<Node>" +
                        "<Child>value</Child>" +
                        "</Node>",
                        List.of(
                                ElementFactory.fromPathAndValue("Node", Value.withXmlValue("<Child>value</Child>")),
                                ElementFactory.fromPathAndValue("Node, Child", Value.withRawValue("value"))
                        )},
                {"<Node>" +
                        "<Child id=\"a\"/>" +
                        "</Node>",
                        List.of(
                                ElementFactory.fromPathAndValue("Node", Value.withXmlValue("<Child id=\"a\"/>")),
                                ElementFactory.fromPathAndAttributes("Node, Child", List.of(new Attribute("id", "a")))
                        )},
                {"<Node>" +
                        "<Child id=\"a\">value</Child>" +
                        "</Node>",
                        List.of(
                                ElementFactory.fromPathAndValue("Node", Value.withXmlValue("<Child id=\"a\">value</Child>")) ,
                                ElementFactory.fromAll("Node, Child", Value.withRawValue("value"), List.of(new Attribute("id", "a")))
                        )},
                {"<Node>" +
                        "<Child id=\"a\">value1</Child>" +
                        "<Child id=\"b\">value2</Child>" +
                        "</Node>",
                        List.of(
                                ElementFactory.fromPathAndValue("Node", Value.withXmlValue("<Child id=\"a\">value1</Child><Child id=\"b\">value2</Child>")) ,
                                ElementFactory.fromAll("Node, Child", Value.withRawValue("value1"), List.of(new Attribute("id", "a"))),
                                ElementFactory.fromAll("Node, Child", Value.withRawValue("value2"), List.of(new Attribute("id", "b")))
                        )}


        });
    }

    @Test
    public void shouldConvertXmlToJson() {
        List<Element> resultJson = xmlConverter.convert2Elements(xml, "");

        Assert.assertEquals(expectedElements.size(), resultJson.size());
        for (int i = 0; i < resultJson.size(); i++) {
            Element expected = expectedElements.get(i);
            Element actual = resultJson.get(i);

            Assert.assertEquals(expected.key, actual.key);
            Assert.assertEquals(expected.value, actual.value);
            Assert.assertEquals(expected.newValue, actual.newValue);
            Assert.assertEquals(expected.attributes, actual.attributes);

        }
    }
}
