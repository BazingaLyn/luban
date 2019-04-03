package org.luban.transports.protocol;

/**
 * @author liguolin
 * 2019年04月03日15:48:08
 */
public enum Protocol {

    DEFAULT((byte)1);

    private byte code;

    Protocol(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }

}
