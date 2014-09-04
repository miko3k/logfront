package org.deletethis.logfront.cfg;

public interface CfgValue {

    public boolean isMap();
    public boolean isList();
    public boolean isScalar();
    public boolean isDummy();

    public Integer getInteger(Integer def);
    public String getString(String def);
    public Float getFloat(Float def);
    public Boolean getBoolean(Boolean def);

    public CfgValue getMember(String name);
    public Iterable<String> getMemberNames();

    public CfgValue getListItem(int n);
    public int getListSize();
}
