package com.evs.doctor.model;

import java.io.Serializable;

public interface Identify<PK> extends Serializable {

    public PK getId();
}
