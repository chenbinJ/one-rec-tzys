package com.ztgeo.general.util;
import com.ztgeo.general.biz.GateLogBiz;
import com.ztgeo.general.vo.LogInfo;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 使用队列的方式开辟线程记录日志
 * @author weihao
 */
@Slf4j
public class DBLog extends Thread {
    private static DBLog dblog = null;
    private static BlockingQueue<LogInfo> logInfoQueue = new LinkedBlockingQueue<LogInfo>(1024);

    public GateLogBiz getLogService() {
        return logService;
    }

    public DBLog setLogService(GateLogBiz logService) {
        if(this.logService==null) {
            this.logService = logService;
        }
        return this;
    }

    private GateLogBiz logService;
    public static synchronized DBLog getInstance() {
        if (dblog == null) {
            dblog = new DBLog();
        }
        return dblog;
    }

    private DBLog() {
        super("CLogOracleWriterThread");
    }

    public void offerQueue(LogInfo logInfo) {
        try {
            logInfoQueue.offer(logInfo);
        } catch (Exception e) {
            log.error("日志写入失败", e);
        }
    }

    @Override
    public void run() {
        List<LogInfo> bufferedLogList = new ArrayList<LogInfo>(); // 缓冲队列
        while (true) {
            try {
                bufferedLogList.add(logInfoQueue.take());
                logInfoQueue.drainTo(bufferedLogList);
                if (bufferedLogList != null && bufferedLogList.size() > 0) {
                    // 写入日志
                    for(LogInfo log:bufferedLogList){
                        logService.saveLog(log);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 防止缓冲队列填充数据出现异常时不断刷屏
                try {
                    Thread.sleep(1000);
                } catch (Exception eee) {
                }
            } finally {
                if (bufferedLogList != null && bufferedLogList.size() > 0) {
                    try {
                        bufferedLogList.clear();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
