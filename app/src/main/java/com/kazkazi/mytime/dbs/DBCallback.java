package com.kazkazi.mytime.dbs;

public interface DBCallback<DATA> {
    void onSuccess(DATA response);
}
