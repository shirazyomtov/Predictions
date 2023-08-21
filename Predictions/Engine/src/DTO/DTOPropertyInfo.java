package DTO;

public class DTOPropertyInfo {
    private String name;

//    private String value;

    private String type;

    private boolean isRandom;

    private DTORangeInfo range;

    public DTOPropertyInfo(String name, String type, boolean isRandom, DTORangeInfo range) {
        this.name = name;
//        this.value = value;
        this.type = type;
        this.isRandom = isRandom;
        this.range = range;
    }

    public String getName() {
        return name;
    }

//    public String getValue() {
//        return value;
//    }

    public String getType() {
        return type;
    }

    public boolean getIsRandom() {
        return isRandom;
    }

    public DTORangeInfo getRange() {
        return range;
    }
}
