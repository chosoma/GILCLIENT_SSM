package domain;

import data.FormatTransfer;

public class HitchUnitBean {
    private byte type = 4;
    private int point;
    private String xw;
    private byte number;
    private byte gatewaytype;
    private byte gatewaynumber;
    private String place;
    private float x;
    private float y;

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getXw() {
        return xw;
    }

    public void setXw(String xw) {
        this.xw = xw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HitchUnitBean that = (HitchUnitBean) o;
        return type == that.type && number == that.number;
    }

    @Override
    public int hashCode() {
        int result = (int) type;
        result = 31 * result + (int) number;
        return result;
    }

    @Override
    public String toString() {
        return "HitchUnitBean{" +
                "type=" + type +
                ", number=" + number +
                ", gatewaytype=" + gatewaytype +
                ", gatewaynumber=" + gatewaynumber +
                ", place='" + place + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", vollevel=" + vollevel +
                ", volwarn=" + volwarn +
                '}';
    }

    private byte vollevel;
    private float volwarn;

    public void setVollevel(byte vollevel) {
        this.vollevel = vollevel;
    }

    public byte getVollevel() {
        return vollevel;
    }

    public float getVolwarn() {
        return volwarn;
    }

    public void setVolwarn(float volwarn) {
        this.volwarn = volwarn;
    }

    public byte getType() {
        return type;
    }

    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    public byte getGatewaytype() {
        return gatewaytype;
    }

    public void setGatewaytype(byte gatewaytype) {
        this.gatewaytype = gatewaytype;
    }

    public byte getGatewaynumber() {
        return gatewaynumber;
    }

    public void setGatewaynumber(byte gatewaynumber) {
        this.gatewaynumber = gatewaynumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
