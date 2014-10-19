package org.github.fscheffer.arras;

public enum IconEffect {

    SPIN("spin"),
    ROTATE90("rotate-90"),
    ROTATE180("rotate-180"),
    ROTATE270("rotate-270"),
    FLIP_HORIZONTAL("flip-horizontal"),
    FLIP_VERTICAL("flip-vertical");

    public final String value;

    IconEffect(String value) {
        this.value = value;
    }
}
