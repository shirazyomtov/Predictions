package DTO;

public class DTOPropertyInfo {
    private String name;

    private String type;

    private boolean isRandom;

    private DTORangeInfo range;

    public DTOPropertyInfo(String name, String type, boolean isRandom, DTORangeInfo range) {
        this.name = name;
        this.type = type;
        this.isRandom = isRandom;
        this.range = range;
    }

    public String getName() {
        return name;
    }

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
