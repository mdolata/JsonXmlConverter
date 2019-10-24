package com.mdolata.jsonxmlconverter;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class ElementFactoryTest {

    @Test
    public void shouldReturnElementWithKeyOnly() {
        Main.Element element = Main.ElementFactory.fromPath("Key");

        Assert.assertEquals("Key", element.key);
        Assert.assertEquals(Optional.empty(), element.value);
        Assert.assertEquals(List.of(), element.attributes);
    }

    @Test
    public void shouldReturnElementWithKeyAndValue() {
        Main.Element element = Main.ElementFactory.fromPathAndValue("Key", Optional.of("Value"));

        Assert.assertEquals("Key", element.key);
        Assert.assertEquals(Optional.of("Value"), element.value);
        Assert.assertEquals(List.of(), element.attributes);
    }

    @Test
    public void shouldReturnElementWithKeyAndAttributes() {
        Main.Attribute e1 = new Main.Attribute("name", "value");
        Main.Element element = Main.ElementFactory.fromPathAndAttributes("Key", List.of(e1));

        Assert.assertEquals("Key", element.key);
        Assert.assertEquals(Optional.empty(), element.value);
        Assert.assertEquals(List.of(e1), element.attributes);
    }

    @Test
    public void shouldReturnElementWithAllFields() {
        Main.Attribute e1 = new Main.Attribute("name", "value");
        Main.Element element = Main.ElementFactory.fromAll("Key", Optional.of("Value"), List.of(e1));

        Assert.assertEquals("Key", element.key);
        Assert.assertEquals(Optional.of("Value"), element.value);
        Assert.assertEquals(List.of(e1), element.attributes);
    }
}
