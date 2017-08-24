package com.portal.common.page;

/**
 * Created on 2015/8/24.
 */
public class PageReq {

    private int index;
    private int size;

    public PageReq(int index, int size){
        this.index = index;
        this.size = size;
    }


    public int getIndex() {
        return index;
    }

    public int getSize() {
        return size;
    }
}
