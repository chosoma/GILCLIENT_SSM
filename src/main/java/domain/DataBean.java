package domain;

import java.util.Date;
import java.util.Vector;

public class DataBean {
    private int id;
    private String place, xw;
    private byte gatewayType, gatewayNumber, unitType, unitNumber;
    private float pres, temp, den, vari, batlv, hitchvol;
    private Date date;
    private int point;
    private boolean lowPres, lowLock;

    public float getHitchvol() {
        return hitchvol;
    }

    public void setHitchvol(float hitchvol) {
        this.hitchvol = hitchvol;
    }

    public String getXw() {
        return xw;
    }

    public void setXw(String xw) {
        this.xw = xw;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(byte gatewayType) {
        this.gatewayType = gatewayType;
    }

    public byte getGatewayNumber() {
        return gatewayNumber;
    }

    public void setGatewayNumber(byte gatewayNumber) {
        this.gatewayNumber = gatewayNumber;
    }

    public byte getUnitType() {
        return unitType;
    }

    public void setUnitType(byte unitType) {
        this.unitType = unitType;
    }

    public byte getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(byte unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public float getPres() {
        return pres;
    }

    public void setPres(float pres) {
        this.pres = pres;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getDen() {
        return den;
    }

    public void setDen(float den) {
        this.den = den;
    }

    public float getVari() {
        return vari;
    }

    public void setVari(float vari) {
        this.vari = vari;
    }

    public float getBatlv() {
        return batlv;
    }

    public void setBatlv(float batlv) {
        this.batlv = batlv;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isLowPres() {
        return lowPres;
    }

    public void setLowPres(boolean lowPres) {
        this.lowPres = lowPres;
    }

    public boolean isLowLock() {
        return lowLock;
    }

    public void setLowLock(boolean lowLock) {
        this.lowLock = lowLock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataBean dataBean = (DataBean) o;

        if (unitType != dataBean.unitType) return false;
        if (unitNumber != dataBean.unitNumber) return false;
        return date != null ? date.equals(dataBean.date) : dataBean.date == null;
    }

    @Override
    public int hashCode() {
        int result = (int) unitType;
        result = 31 * result + (int) unitNumber;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public Vector<Object> getSqlData() {
        Vector<Object> data = new Vector<Object>();
        data.add(unitType);
        data.add(unitNumber);
        data.add(den);
        data.add(pres);
        data.add(temp);
        data.add(vari);
        data.add(hitchvol);
        data.add(batlv);
        data.add(date);
        return data;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "unitType=" + unitType +
                ", unitNumber=" + unitNumber +
                ", date=" + date +
                ", lowPres=" + lowPres +
                ", lowLock=" + lowLock +
                '}';
    }
}
