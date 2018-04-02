package domain;

public class HitchVolLevelBean {
    private byte level ;
    private float vol;

    public HitchVolLevelBean() {
    }

    public HitchVolLevelBean(byte level, float vol) {
        this.level = level;
        this.vol = vol;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public float getVol() {
        return vol;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }

    @Override
    public String toString() {
        return "HitchVolLevelBean{" +
                "level=" + level +
                ", vol=" + vol +
                '}';
    }
}
