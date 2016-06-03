package com.android.ubclaunchpad.driver;

/**
 * Base interface that every View interface should implement.
 * Can be extended to include common behaviours. 'T' is a
 * type-safe contract between the interfaces.
 * <p/>
 * Created by Chris Li on 6/1/2016.
 */
public interface BaseView<T> {

    void setPresenter(T presenter);

}
