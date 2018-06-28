package org.litespring.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * 作用是获取资源文件，然后加载类
 */
public interface Resource {
    InputStream getInputStream() throws IOException;
    String getDescription();
}
