package com.datastory.commons3.monitor.executor;

import com.datastory.commons3.monitor.executor.impl.HbaseMonitorExecutor;
import com.datastory.commons3.monitor.utils.MonitorConf;

import java.io.IOException;

/**
 * com.datastory.executor.MonitorExecutorFactory
 *
 * @since 2017/11/20
 */
public class MonitorExecutorFactory {
    /**
     * 以后可能会根据configuration来配置
     * @param monitorConf
     * @return
     * @throws IOException
     */
    public MonitorExecutor provide(MonitorConf monitorConf) throws IOException {
        return new HbaseMonitorExecutor();
    }
}
