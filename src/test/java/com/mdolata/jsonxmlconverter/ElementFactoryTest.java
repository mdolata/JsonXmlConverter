package com.mdolata.jsonxmlconverter;

import com.mdolata.jsonxmlconverter.model.Attribute;
import com.mdolata.jsonxmlconverter.model.Element;
import com.mdolata.jsonxmlconverter.model.ElementFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class ElementFactoryTest {

    @Test
    public void shouldReturnElementWithKeyOnly() {
        Element element = ElementFactory.fromPath("Key");

        Assert.assertEquals("Key", element.key);
        Assert.assertEquals(Optional.empty(), element.value);
        Assert.assertEquals(List.of(), element.attributes);
    }

    @Test
    public void shouldReturnElementWithKeyAndValue() {
        Element element = ElementFactory.fromPathAndValue("Key", Optional.of("Value"));

        Assert.assertEquals("Key", element.key);
        Assert.assertEquals(Optional.of("Value"), element.value);
        Assert.assertEquals(List.of(), element.attributes);
    }

    @Test
    public void shouldReturnElementWithKeyAndAttributes() {
        Attribute e1 = new Attribute("name", "value");
        Element element = ElementFactory.fromPathAndAttributes("Key", List.of(e1));

        Assert.assertEquals("Key", element.key);
        Assert.assertEquals(Optional.empty(), element.value);
        Assert.assertEquals(List.of(e1), element.attributes);
    }

    @Test
    public void shouldReturnElementWithAllFields() {
        Attribute e1 = new Attribute("name", "value");
        Element element = ElementFactory.fromAll("Key", Optional.of("Value"), List.of(e1));

        Assert.assertEquals("Key", element.key);
        Assert.assertEquals(Optional.of("Value"), element.value);
        Assert.assertEquals(List.of(e1), element.attributes);
    }
}
