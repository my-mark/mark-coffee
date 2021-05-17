package com.mark.markcoffee.fileDown;

import org.springframework.http.client.ClientHttpResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileResponseExtractor extends AbstractDisplayDownloadSpeedResponseExtractor {

    //已下载的字节数
    private long byteCount;
    //文件的路径
    private String filePath;

    public FileResponseExtractor(String filePath) {
        this.filePath = filePath;
    }

    @Override
    protected File doExtractData(ClientHttpResponse response) throws IOException {
        InputStream in = response.getBody();
        File file = new File(filePath);
        FileOutputStream out = new FileOutputStream(file);
        int bytesRead;
        for (byte[] buffer = new byte[4096]; (bytesRead = in.read(buffer)) != -1; byteCount += bytesRead) {
            out.write(buffer, 0, bytesRead);
        }
        out.flush();
        out.close();
        return file;
    }

    @Override
    protected long getAlreadyDownloadLength() {
        return byteCount;
    }
}
