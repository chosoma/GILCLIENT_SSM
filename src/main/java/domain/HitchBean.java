package domain;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Vector;

public class HitchBean {
    private int id;
    private float vol;
    private Date date;
    private float batlv;
    private byte unittype;
    private byte unitnumber;
    private String place;


    public float getBatlv() {
        return batlv;
    }

    public void setBatlv(float batlv) {
        this.batlv = batlv;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getVol() {
        return vol;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public byte getUnittype() {
        return unittype;
    }

    public void setUnittype(byte unittype) {
        this.unittype = unittype;
    }

    public byte getUnitnumber() {
        return unitnumber;
    }

    public void setUnitnumber(byte unitnumber) {
        this.unitnumber = unitnumber;
    }

    @Override
    public String toString() {
        return "HitchBean{" +
                "id=" + id +
                ", vol=" + vol +
                ", date=" + date +
                ", unittype=" + unittype +
                ", unitnumber=" + unitnumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HitchBean hitchBean = (HitchBean) o;

        if (unittype != hitchBean.unittype) return false;
        if (unitnumber != hitchBean.unitnumber) return false;
        return date != null ? date.equals(hitchBean.date) : hitchBean.date == null;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (int) unittype;
        result = 31 * result + (int) unitnumber;
        return result;
    }

    public Vector<Object> getSqlData() {
        Vector<Object> vector = new Vector<>();
        vector.add(unittype);
        vector.add(unitnumber);
        vector.add(vol);
        vector.add(batlv);
        vector.add(date);
        return vector;
    }
}
