package org.easybatch.xml;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Foo {

    private String name;

    public Foo() {
    }

    public Foo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
