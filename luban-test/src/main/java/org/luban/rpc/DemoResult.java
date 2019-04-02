package org.luban.rpc;

import java.io.Serializable;
import java.util.Date;

/**
 *
 */
public class DemoResult implements Serializable {

    private String name;

    private int count;

    private Date time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
