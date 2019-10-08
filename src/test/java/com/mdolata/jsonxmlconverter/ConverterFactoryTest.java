package com.mdolata.jsonxmlconverter;

import org.junit.Assert;
import org.junit.Test;

public class ConverterFactoryTest {

    @Test
    public void shouldReturnXmlConverterForXmlInput() {
        String xml = "<test/>";
        Main.Converter converter = Main.ConverterFactory.getConverter(xml);

        Assert.assertEquals(Main.XmlToJsonConverter.class, converter.getClass());
    }

    @Test
    public void shouldReturnJsonConverterForJsonInput() {
        String json = "{\"test\":32}";
        Main.Converter converter = Main.ConverterFactory.getConverter(json);

        Assert.assertEquals(Main.JsonToXmlConverter.class, converter.getClass());
    }

    @Test
    public void shouldReturnDefaultConverterForNotMatchingInput() {
        String differentStructure = "test";
        Main.Converter converter = Main.ConverterFactory.getConverter(differentStructure);

        Assert.assertNotEquals(Main.XmlToJsonConverter.class, converter.getClass());
        Assert.assertNotEquals(Main.JsonToXmlConverter.class, converter.getClass());
    }
}
