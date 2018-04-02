package domain;

import java.util.Date;
import java.util.List;

public class DataSearchPara {

    private String type, place, xw;//类型
    private Byte unitType, unitNumber;//编号
    private Date t1, t2;
    private List<UnitBean> units;
    private List<HitchUnitBean> hitchunits;

    public List<HitchUnitBean> getHitchunits() {
        return hitchunits;
    }

    public void setHitchunits(List<HitchUnitBean> hitchunits) {
        this.hitchunits = hitchunits;
    }

    public Byte getUnitType() {
        return unitType;
    }

    public List<UnitBean> getUnits() {
        return units;
    }

    public void setUnits(List<UnitBean> units) {
        this.units = units;
    }

    public void setUnitType(Byte unitType) {
        this.unitType = unitType;
    }

    public Byte getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(Byte unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getT1() {
        return t1;
    }

    public void setT1(Date t1) {
        this.t1 = t1;
    }

    public Date getT2() {
        return t2;
    }

    public void setT2(Date t2) {
        this.t2 = t2;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getXw() {
        return xw;
    }

    public void setXw(String xw) {
        this.xw = xw;
    }

    @Override
    public String toString() {
        return "DataSearchPara{" +
                "type='" + type + '\'' +
                ", place='" + place + '\'' +
                ", xw='" + xw + '\'' +
                ", unitType=" + unitType +
                ", unitNumber=" + unitNumber +
                ", t1=" + t1 +
                ", t2=" + t2 +
                '}';
    }
}
