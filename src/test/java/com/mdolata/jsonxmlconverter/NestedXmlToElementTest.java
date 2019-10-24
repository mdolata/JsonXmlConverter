package com.mdolata.jsonxmlconverter;

import com.mdolata.jsonxmlconverter.Main.Element;
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
                        Main.ElementFactory.fromPath("transaction"),
                        Main.ElementFactory.fromPathAndValue("transaction, id", Optional.of("6753322")),
                        Main.ElementFactory.fromAll(" transaction, number", Optional.of("8-900-000-00-00"), List.of(new Main.Attribute("region", "Russia"))),
                        Main.ElementFactory.fromPathAndValue("transaction, nonattr", Optional.empty()),
                        Main.ElementFactory.fromPathAndValue("transaction, nonattr", Optional.of("")),
                        Main.ElementFactory.fromPathAndValue("transaction, nonattr", Optional.of("text")),
                        Main.ElementFactory.fromAll("transaction, attr", Optional.empty(), List.of(new Main.Attribute("id", "1"))),
                        Main.ElementFactory.fromAll("transaction, attr", Optional.of(""), List.of(new Main.Attribute("id", "2"))),
                        Main.ElementFactory.fromAll("transaction, nonattr", Optional.of("text"), List.of(new Main.Attribute("id", "3"))),
                        Main.ElementFactory.fromPath("transaction, email"),
                        Main.ElementFactory.fromPathAndValue("transaction, email, to", Optional.of("to_example@gmail.com")),
                        Main.ElementFactory.fromPathAndValue("transaction, email, from", Optional.of("from_example@gmail.com")),
                        Main.ElementFactory.fromPathAndValue("transaction, email, subject", Optional.of("Project discussion")),
                        Main.ElementFactory.fromAll("transaction, email, body", Optional.of("Body message"), List.of(new Main.Attribute("font", "Verdana"))),
                        Main.ElementFactory.fromAll("transaction, email, date", Optional.empty(), List.of(new Main.Attribute("day", "12"), new Main.Attribute("month", "12"), new Main.Attribute("year", "2018")))
                )},
                {"<Node/>",
                        List.of(
                                Main.ElementFactory.fromPath("Node")
                        )},
                {"<Node>" +
                        "<Child/>" +
                        "</Node>",
                        List.of(
                                Main.ElementFactory.fromPath("Node"),
                                Main.ElementFactory.fromPath("Node, Child")
                        )},
                {"<Node>" +
                        "<Child>value</Child>" +
                        "</Node>",
                        List.of(
                                Main.ElementFactory.fromPath("Node"),
                                Main.ElementFactory.fromPathAndValue("Node, Child", Optional.of("value"))
                        )},
                {"<Node>" +
                        "<Child id=\"a\"/>" +
                        "</Node>",
                        List.of(
                                Main.ElementFactory.fromPath("Node"),
                                Main.ElementFactory.fromPathAndAttributes("Node, Child", List.of(new Main.Attribute("id", "a")))
                        )},
                {"<Node>" +
                        "<Child id=\"a\">value</Child>" +
                        "</Node>",
                        List.of(
                                Main.ElementFactory.fromPath("Node"),
                                Main.ElementFactory.fromAll("Node, Child", Optional.of("value"), List.of(new Main.Attribute("id", "a")))
                        )}


        });
    }

    @Test
    public void shouldConvertXmlToJson() {
        List<Element> resultJson = xmlConverter.convert2Elements(xml);
        Assert.assertEquals(expectedElements, resultJson);
    }
}
