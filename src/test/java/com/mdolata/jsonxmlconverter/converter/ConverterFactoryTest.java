package com.mdolata.jsonxmlconverter.converter;

import com.mdolata.jsonxmlconverter.converter.Converter;
import com.mdolata.jsonxmlconverter.converter.ConverterFactory;
import com.mdolata.jsonxmlconverter.converter.JsonToXmlConverter;
import com.mdolata.jsonxmlconverter.converter.XmlToJsonConverter;
import org.junit.Assert;
import org.junit.Test;

public class ConverterFactoryTest {

    @Test
    public void shouldReturnXmlConverterForXmlInput() {
        String xml = "<test/>";
        Converter converter = ConverterFactory.getConverter(xml);

        Assert.assertEquals(XmlToJsonConverter.class, converter.getClass());
    }

    @Test
    public void shouldReturnJsonConverterForJsonInput() {
        String json = "{\"test\":32}";
        Converter converter = ConverterFactory.getConverter(json);

        Assert.assertEquals(JsonToXmlConverter.class, converter.getClass());
    }

    @Test
    public void shouldReturnDefaultConverterForNotMatchingInput() {
        String differentStructure = "test";
        Converter converter = ConverterFactory.getConverter(differentStructure);

        Assert.assertNotEquals(XmlToJsonConverter.class, converter.getClass());
        Assert.assertNotEquals(JsonToXmlConverter.class, converter.getClass());
    }
}
