package by.vstu.electronicjournal.entity;

public enum SubGroupType {
    BY_GROUP("BY_GROUP"), BY_FOREIGN_LANGUAGE("BY_FOREIGN_LANGUAGE");

    String subGroupType;

    SubGroupType(String subGroupType) {
        this.subGroupType = subGroupType;
    }

    public String getSubGroupType(){
        return subGroupType;
    }
}
