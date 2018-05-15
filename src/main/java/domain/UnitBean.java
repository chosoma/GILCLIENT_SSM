package domain;

import java.util.ArrayList;
import java.util.List;

public class UnitBean implements Comparable<UnitBean> {


    private byte gatewaytype, gatewaynumber, type, number, period;
    private float initvari, x, y;
    private Float maxden, minden, maxper, minper, SF6Temp;
    private Float warnTemp;
    private boolean isinit,tempflag;
    private Float minvari, maxvari;
    private byte vollevel;
    private Float volwarn;
    private int point;
    private String xw, place;


    public boolean isTempflag() {
		return tempflag;
	}

	public void setTempflag(boolean tempflag) {
		this.tempflag = tempflag;
	}

	@Override
    public String toString() {
        return "UnitBean{" +
                "gatewaytype=" + gatewaytype +
                ", gatewaynumber=" + gatewaynumber +
                ", type=" + type +
                ", number=" + number +
                ", period=" + period +
                ", initvari=" + initvari +
                ", x=" + x +
                ", y=" + y +
                ", maxden=" + maxden +
                ", minden=" + minden +
                ", maxper=" + maxper +
                ", minper=" + minper +
                ", SF6Temp=" + SF6Temp +
                ", warnTemp=" + warnTemp +
                ", isinit=" + isinit +
                ", minvari=" + minvari +
                ", maxvari=" + maxvari +
                ", vollevel=" + vollevel +
                ", volwarn=" + volwarn +
                ", point=" + point +
                ", xw='" + xw + '\'' +
                ", place='" + place + '\'' +
                '}';
    }

    public UnitBean() {
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

    public Float getSF6Temp() {
        return SF6Temp;
    }

    public void setSF6Temp(Float SF6Temp) {
        this.SF6Temp = SF6Temp;
    }

    public Float getWarnTemp() {
        return warnTemp;
    }

    public void setWarnTemp(Float warnTemp) {
        this.warnTemp = warnTemp;
    }

    public boolean isIsinit() {
        return isinit;
    }

    public void setIsinit(boolean isinit) {
        this.isinit = isinit;
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

    public byte getVollevel() {
        return vollevel;
    }

    public void setVollevel(byte vollevel) {
        this.vollevel = vollevel;
    }

    public Float getVolwarn() {
        return volwarn;
    }

    public void setVolwarn(Float volwarn) {
        this.volwarn = volwarn;
    }

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

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
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

    public List<Object> getProper() {
        List<Object> p = new ArrayList<>();
        switch (type) {
            case 1:
                p.add(maxden);
                p.add(minden);
                p.add(maxper);
                p.add(minper);
                p.add(SF6Temp);
                break;
            case 2:
                p.add(maxvari);
                p.add(minvari);
                break;
            case 3:
                p.add(warnTemp);
                break;
            case 4:
                p.add(vollevel);
                p.add(volwarn);
                break;
        }
        p.add(number);
        return p;
    }


}
