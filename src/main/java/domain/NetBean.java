package domain;

import java.util.Vector;

/**
 * 网关实体类
 */
public class NetBean implements Comparable<NetBean>{

    private String sim;//类型,sim卡号
    private byte type;
    private byte number;//网关id
    private byte channel;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetBean netBean = (NetBean) o;

        return type == netBean.type && number == netBean.number;
    }

    @Override
    public int hashCode() {
        int result = (int) type;
        result = 31 * result + (int) number;
        return result;
    }

    @Override
    public int compareTo(NetBean o) {
        if(type!=o.type){
            return type-o.type;
        }
        int n1 = number&0xff;
        int n2 = o.number&0xff;
        return n1-n2;
    }
}
