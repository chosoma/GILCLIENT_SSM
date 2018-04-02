package util;

import java.util.ArrayList;
import java.util.List;

import protocol.Protocol;

public class MyDecodeUitl {
    /**
     * 逆转义
     */
    public static byte[] Decrypt(byte[] source) {
        List<Byte> btlist = new ArrayList<>();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == Protocol.TURN) {
                switch (source[i + 1]) {
                    case (Protocol.HEADT): {
                        btlist.add(Protocol.HEAD);
                        i++;
                        break;
                    }
                    case (Protocol.TAILT): {
                        btlist.add(Protocol.TAIL);
                        i++;
                        break;
                    }
                    case (Protocol.TURNT): {
                        btlist.add(Protocol.TURN);
                        i++;
                        break;
                    }
                    case (Protocol.IST1T): {
                        btlist.add(Protocol.IST1);
                        i++;
                        break;
                    }
                    case (Protocol.IST2T): {
                        btlist.add(Protocol.IST2);
                        i++;
                        break;
                    }
                    default: {
                        btlist.add(Protocol.TURN);
                    }

                }

            } else {
                btlist.add(source[i]);
            }

        }
        byte[] ret = new byte[btlist.size()];
        int i = 0;
        for (byte data : btlist) {
            ret[i] = data;
            i++;
        }
        return ret;
    }

    /**
     * 转义
     */
    public static byte[] Encryption(byte[] source) {
        List<Byte> btlist = new ArrayList<>();
//        btlist.add(Protocol.TAIL);// 头
        btlist.add(Protocol.HEAD);// 头
        for (byte aSource : source) {
            switch (aSource) {
                case (Protocol.HEAD): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.HEADT);
                    break;
                }
                case (Protocol.TAIL): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.TAILT);
                    break;
                }
                case (Protocol.TURN): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.TURNT);
                    break;
                }
                case (Protocol.IST1): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.IST1T);
                    break;
                }
                case (Protocol.IST2): {
                    btlist.add(Protocol.TURN);
                    btlist.add(Protocol.IST2T);
                    break;
                }
                default: {
                    btlist.add(aSource);
                }
            }
        }
//        btlist.add(Protocol.HEAD);// 尾
        btlist.add(Protocol.TAIL);
        byte[] ret = new byte[btlist.size()];
        int i = 0;
        for (byte data : btlist) {
            ret[i] = data;
            i++;
        }
        return ret;
    }
}
