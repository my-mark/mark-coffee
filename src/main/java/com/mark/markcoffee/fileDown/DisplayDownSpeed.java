package com.mark.markcoffee.fileDown;

public interface DisplayDownSpeed {

    /**
     * 显示下载速度
     * @param task
     * @param contentLength
     */
    void displaySpeed(String task, long contentLength);
}
