package model;

import java.io.Serializable;
import java.util.Objects;

abstract class Base implements Serializable {
    private int id;

    public Base(){}

    public Base(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public void SetId(int id)
    {
        this.id = id;
    }
}
