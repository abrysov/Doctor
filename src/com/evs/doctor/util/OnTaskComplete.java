package com.evs.doctor.util;

public interface OnTaskComplete<Result> {
    void onTaskComplete(Result result);
}