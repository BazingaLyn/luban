package org.luban.transports.protocol;

/**
 * @author liguolin
 * 2019-03-28 18:13:43
 */
public class LubanRpcProtocol {

    private byte type;

    private int length;

    private byte[] contents;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
    }
}
