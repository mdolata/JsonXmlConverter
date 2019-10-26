package com.mdolata.jsonxmlconverter;

import com.mdolata.jsonxmlconverter.Main.Element;
import com.mdolata.jsonxmlconverter.model.Attribute;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(Parameterized.class)
public class NestedXmlToElementTest {

    private final String xml;
    private final List<Element> expectedElements;


    private final Main.Converter xmlConverter = new Main.XmlToJsonConverter();

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
                        Main.ElementFactory.fromPathAndValue("transaction", Main.Value.withXmlValue(Optional.of("\n" +
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
                                ""))),
                        Main.ElementFactory.fromPathAndValue("transaction, id", Main.Value.withRawValue(Optional.of("6753322"))),
                        Main.ElementFactory.fromAll("transaction, number", Main.Value.withRawValue(Optional.of("8-900-000-00-00")), List.of(new Attribute("region", "Russia"))),
                        Main.ElementFactory.fromPathAndValue("transaction, nonattr", Main.Value.withRawValue(Optional.empty())),
                        Main.ElementFactory.fromPathAndValue("transaction, nonattr", Main.Value.withRawValue(Optional.of(""))),
                        Main.ElementFactory.fromPathAndValue("transaction, nonattr", Main.Value.withRawValue(Optional.of("text"))),
                        Main.ElementFactory.fromAll("transaction, attr", Main.Value.withRawValue(Optional.empty()), List.of(new Attribute("id", "1"))),
                        Main.ElementFactory.fromAll("transaction, attr", Main.Value.withRawValue(Optional.of("")), List.of(new Attribute("id", "2"))),
                        Main.ElementFactory.fromAll("transaction, attr", Main.Value.withRawValue(Optional.of("text")), List.of(new Attribute("id", "3"))),
                        Main.ElementFactory.fromPathAndValue("transaction, email", Main.Value.withXmlValue(Optional.of("\n" +
                                        "        <to>to_example@gmail.com</to>\n" +
                                        "        <from>from_example@gmail.com</from>\n" +
                                        "        <subject>Project discussion</subject>\n" +
                                        "        <body font=\"Verdana\">Body message</body>\n" +
                                        "        <date day=\"12\" month=\"12\" year=\"2018\"/>\n" +
                                "    "
                        ))),
                        Main.ElementFactory.fromPathAndValue("transaction, email, to", Main.Value.withRawValue(Optional.of("to_example@gmail.com"))),
                        Main.ElementFactory.fromPathAndValue("transaction, email, from", Main.Value.withRawValue(Optional.of("from_example@gmail.com"))),
                        Main.ElementFactory.fromPathAndValue("transaction, email, subject", Main.Value.withRawValue(Optional.of("Project discussion"))),
                        Main.ElementFactory.fromAll("transaction, email, body", Main.Value.withRawValue(Optional.of("Body message")), List.of(new Attribute("font", "Verdana"))),
                        Main.ElementFactory.fromAll("transaction, email, date", Main.Value.withRawValue(Optional.empty()), List.of(new Attribute("day", "12"), new Attribute("month", "12"), new Attribute("year", "2018")))
                )},
                {"<Node/>",
                        List.of(
                                Main.ElementFactory.fromPath("Node")
                        )},
                {"<Node>" +
                        "<Child/>" +
                        "</Node>",
                        List.of(
                                Main.ElementFactory.fromPathAndValue("Node", Main.Value.withXmlValue(Optional.of("<Child/>"))),
                                Main.ElementFactory.fromPath("Node, Child")
                        )},
                {"<Node>" +
                        "<Child>value</Child>" +
                        "</Node>",
                        List.of(
                                Main.ElementFactory.fromPathAndValue("Node", Main.Value.withXmlValue(Optional.of("<Child>value</Child>"))),
                                Main.ElementFactory.fromPathAndValue("Node, Child", Main.Value.withRawValue(Optional.of("value")))
                        )},
                {"<Node>" +
                        "<Child id=\"a\"/>" +
                        "</Node>",
                        List.of(
                                Main.ElementFactory.fromPathAndValue("Node", Main.Value.withXmlValue(Optional.of("<Child id=\"a\"/>"))),
                                Main.ElementFactory.fromPathAndAttributes("Node, Child", List.of(new Attribute("id", "a")))
                        )},
                {"<Node>" +
                        "<Child id=\"a\">value</Child>" +
                        "</Node>",
                        List.of(
                                Main.ElementFactory.fromPathAndValue("Node", Main.Value.withXmlValue(Optional.of("<Child id=\"a\">value</Child>"))) ,
                                Main.ElementFactory.fromAll("Node, Child", Main.Value.withRawValue(Optional.of("value")), List.of(new Attribute("id", "a")))
                        )},
                {"<Node>" +
                        "<Child id=\"a\">value1</Child>" +
                        "<Child id=\"b\">value2</Child>" +
                        "</Node>",
                        List.of(
                                Main.ElementFactory.fromPathAndValue("Node", Main.Value.withXmlValue(Optional.of("<Child id=\"a\">value1</Child><Child id=\"b\">value2</Child>"))) ,
                                Main.ElementFactory.fromAll("Node, Child", Main.Value.withRawValue(Optional.of("value1")), List.of(new Attribute("id", "a"))),
                                Main.ElementFactory.fromAll("Node, Child", Main.Value.withRawValue(Optional.of("value2")), List.of(new Attribute("id", "b")))
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
