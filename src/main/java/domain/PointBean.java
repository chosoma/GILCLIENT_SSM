package domain;

public class PointBean implements Comparable<PointBean> {
    private int id;
    private int point;
    private String place;
    private byte unitType;
    private byte gatewayType, gatewayNumber;
    private float x, y;

    public byte getUnitType() {
        return unitType;
    }

    @Override
    public String toString() {
        return "PointBean{" +
                "id=" + id +
                ", point=" + point +
                ", place='" + place + '\'' +
                ", unitType=" + unitType +
                ", gatewayType=" + gatewayType +
                ", gatewayNumber=" + gatewayNumber +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public void setUnitType(byte unitType) {
        this.unitType = unitType;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
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

    public PointBean() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointBean pointBean = (PointBean) o;

        return point == pointBean.point;
    }

    @Override
    public int hashCode() {
        return point;
    }

    @Override
    public int compareTo(PointBean o) {
        return point - o.getPoint();
    }
}
