package com.android.ubclaunchpad.driver;

/**
 * Base interface that every View interface should implement.
 * Can be extended to include common behaviours. 'T' is a
 * type-safe contract between the interfaces.
 */
public interface BaseView<T> {

    void setPresenter(T presenter);

}
