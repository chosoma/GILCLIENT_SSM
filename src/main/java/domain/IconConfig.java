package domain;

public class IconConfig {
    private int point;
    private String xw, iconname;
    private float x, y, width, height;

    public IconConfig() {


    }


    public IconConfig(int point, String xw, float x, float y, float width, float height, String iconname) {
        this.point = point;
        this.xw = xw;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.iconname = iconname;
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

    @Override
    public String toString() {
        return "IconConfig{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
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

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getIconname() {
        return iconname;
    }

    public void setIconname(String iconname) {
        this.iconname = iconname;
    }
}
