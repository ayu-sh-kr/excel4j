package dev.archimeded.excel4j.enums;

public enum FontFamily {

    ARIAL("Arial"), SANS_SERIF("Sans-Serif"), HELVETICA("Helvetica"), APTOS_NARROW("Aptos Narrow");

    private final String value;

    FontFamily(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
