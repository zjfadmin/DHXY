package org.come.bean;

import org.come.entity.Fly;
import org.come.entity.Mount;

import java.util.List;

public class FlyResult {
    private List<Fly> flys;

    public List<Fly> gettFlys() {
        return flys;
    }

    public void setFlys(List<Fly> flys) {
        this.flys = flys;
    }
}
