package com.datastory.commons3.monitor.executor;

import java.util.List;
import java.util.Set;

/**
 * 具体的业务指标处理方法
 */
public interface MonitorExecutor {

    /**
     * 执行所有的url的方法
     * @param requestUrls
     */
    public void executor(List<String> requestUrls);
}
