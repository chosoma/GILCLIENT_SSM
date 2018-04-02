package domain;

public class UnitBean implements Comparable<UnitBean> {


    private byte gatewaytype, gatewaynumber, type, number, period;
    private float initvari;
    private Float maxden;
    private Float minden;
    private Float maxper;
    private Float minper;
    private Float warnTemp;
    private boolean inittemp;

    @Override
    public String toString() {
        return "UnitBean{" +
                " type=" + type +
                " number=" + number +
                '}';
    }

    private Float minvari;
    private Float maxvari;
    private int x, y, point;
    private String xw, place;

    public boolean isInittemp() {
        return inittemp;
    }

    public void setInittemp(boolean inittemp) {
        this.inittemp = inittemp;
    }

    public UnitBean() {
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public UnitBean(byte gatewaytype, byte gatewaynumber, byte type, byte number) {
        setGatewaytype(gatewaytype);
        setGatewaynumber(gatewaynumber);
        setType(type);
        setNumber(number);
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

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    public byte getPeriod() {
        return period;
    }

    public void setPeriod(byte period) {
        this.period = period;
    }

    public float getInitvari() {
        return initvari;
    }

    public void setInitvari(float initvari) {
        this.initvari = initvari;
    }

    public Float getMinvari() {
        return minvari;
    }

    public void setMinvari(Float minvari) {
        this.minvari = minvari;
    }

    public Float getMaxvari() {
        return maxvari;
    }

    public void setMaxvari(Float maxvari) {
        this.maxvari = maxvari;
    }

    public Float getMaxden() {
        return maxden;
    }

    public void setMaxden(Float maxden) {
        this.maxden = maxden;
    }

    public Float getMinden() {
        return minden;
    }

    public void setMinden(Float minden) {
        this.minden = minden;
    }

    public Float getMaxper() {
        return maxper;
    }

    public void setMaxper(Float maxper) {
        this.maxper = maxper;
    }

    public Float getMinper() {
        return minper;
    }

    public void setMinper(Float minper) {
        this.minper = minper;
    }

    public Float getWarnTemp() {
        return warnTemp;
    }

    public void setWarnTemp(Float warnTemp) {
        this.warnTemp = warnTemp;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getXw() {
        return xw;
    }

    public void setXw(String xw) {
        this.xw = xw;
    }

    @Override
    public int compareTo(UnitBean o) {
        if (type == o.type) {
            int n1 = number;
            int n2 = o.number;
            if (n1 < 0) {
                n1 += 256;
            }
            if (n2 < 0) {
                n2 += 256;
            }
            return n1 - n2;
        } else {
            return type - o.type;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnitBean unitBean = (UnitBean) o;

        return type == unitBean.type && number == unitBean.number;
    }

    @Override
    public int hashCode() {
        int result = (int) type;
        result = 31 * result + (int) number;
        return result;
    }
}
