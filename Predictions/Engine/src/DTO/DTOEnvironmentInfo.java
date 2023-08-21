package DTO;

public class DTOEnvironmentInfo {
    private final String name;

    private String type;

    private DTORangeInfo range;

    private String value;

    public DTOEnvironmentInfo(String name, String type, DTORangeInfo range) {
        this.name = name;
        this.type = type;
        this.range = range;
    }

    public DTOEnvironmentInfo(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public DTORangeInfo getRange() {
        return range;
    }

    public String getValue() {
        return value;
    }
}
