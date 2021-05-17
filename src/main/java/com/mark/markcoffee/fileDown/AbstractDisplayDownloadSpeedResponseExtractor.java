package com.mark.markcoffee.fileDown;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public abstract class AbstractDisplayDownloadSpeedResponseExtractor<T> implements ResponseExtractor<T>,DisplayDownSpeed {

    @Override
    public T extractData(ClientHttpResponse response) throws IOException {
        long contentLength = response.getHeaders().getContentLength();
        this.displaySpeed(Thread.currentThread().getName(), contentLength);
        return this.doExtractData(response);
    }

    protected abstract T doExtractData(ClientHttpResponse response) throws IOException;

    /**
     * 获取已经下载了多少字节
     * @return
     */
    protected abstract long getAlreadyDownloadLength();

    /**
     * 显示下载速度
     * @param task
     * @param contentLength
     */
    @Override
    public void displaySpeed(String task, long contentLength) {
        long totalSize = contentLength / 1024;
        CompletableFuture.runAsync(() -> {
            long tmp = 0, speed;
            while (contentLength - tmp > 0) {
                speed = getAlreadyDownloadLength() - tmp;
                tmp = getAlreadyDownloadLength();
                print(task, totalSize, tmp, speed);
                sleep();
            }
        });
    }

    private void print(String task, long totalSize, long tmp, long speed) {
        System.out.println(String.format("%s 文件总大小: %dKB,已下载: %dKB,下载速度: %dKB",task,totalSize,(tmp/1024),(speed/1000)));
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
