package com.github.fscheffer.arras.demo;

import java.io.Serializable;

public class EditorContent implements Serializable {

    private static final long serialVersionUID = 9149600110360460433L;

    public String             value;

    public EditorContent() {}

    public EditorContent(String value) {
        this.value = value;
    }
}