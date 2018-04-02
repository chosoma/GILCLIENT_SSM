package domain;

import java.util.Date;
import java.util.Vector;

public class WarnBean {
    private int id;
    private StringBuilder info;
    private boolean handle;
    private Date date;
    private PointBean pointBean;
    private String xw;

    @Override
    public String toString() {
        return "WarnBean{" +
                "id=" + id +
                ", info=" + info +
                ", handle=" + handle +
                ", date=" + date +
                ", pointBean=" + pointBean +
                ", xw='" + xw + '\'' +
                '}';
    }

    public WarnBean() {
        info = new StringBuilder();
    }

    public PointBean getPointBean() {
        return pointBean;
    }

    public void setPointBean(PointBean pointBean) {
        this.pointBean = pointBean;
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

    public String getInfo() {
        return info.toString();
    }

    public void setInfo(String info) {
        this.info.append(info);
        this.info.append("\n");
    }

    public void clearInfo() {
        this.info.delete(0, info.length());
    }

    public boolean isHandle() {
        return handle;
    }

    public void setHandle(boolean handle) {
        this.handle = handle;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Vector<Object> getSqlData() {
        Vector<Object> data = new Vector<Object>();
        data.add(info.toString());
        data.add(handle);
        data.add(date);
        data.add(pointBean.getPoint());
        data.add(xw);
        return data;
    }

    public Vector<Object> getSqlUpdate() {
        Vector<Object> data = new Vector<Object>();
        data.add(date);
        data.add(pointBean.getPoint());
        data.add(xw);
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WarnBean warnBean = (WarnBean) o;

        if (pointBean != null ? !pointBean.equals(warnBean.pointBean) : warnBean.pointBean != null) return false;
        return xw != null ? xw.equals(warnBean.xw) : warnBean.xw == null;
    }

    @Override
    public int hashCode() {
        int result = pointBean != null ? pointBean.hashCode() : 0;
        result = 31 * result + (xw != null ? xw.hashCode() : 0);
        return result;
    }
}
