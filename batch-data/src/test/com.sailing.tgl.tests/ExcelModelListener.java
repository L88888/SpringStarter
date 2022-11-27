package com.sailing.tgl.tests;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.tgl.rdbms.ConnPoolConfig.TglPoolConfig;
import com.tgl.rdbms.concurrent.SpringBean;
import com.tgl.rdbms.concurrent.TglRaftThreadHelper;
import com.tgl.rdbms.concurrent.TglRaftThreadPool;
import com.tgl.rdbms.concurrent.TglThreadDataCache;
import com.tgl.rdbms.core.CommodityOrder;
import com.tgl.rdbms.entity.CheckTransInfoDto;
import com.tgl.rdbms.entity.CommodityOrderQueue;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @version V1.0
 * @author: hqk
 * @date: 2020/12/30 10:52
 * @Description: 对账excel读取， 用于大数据量 excel 导入监听器， 用于循环处理excel 保存数据
 * 此类不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
 * <p>
 * <p>
 * https://blog.csdn.net/huqiankunlol/article/details/112654766
 */
@Slf4j
public class ExcelModelListener implements ReadListener<CheckTransInfoDto> {

    /**
     * 每隔 1000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 10000;

    List tempData = new Vector();

    AtomicInteger indexNum = new AtomicInteger(1);

    AtomicInteger indexNumPt = new AtomicInteger(1);

    CountDownLatch countDownLatch = new CountDownLatch(11);

    boolean flage = false;

    CommodityOrder commodityOrder;
    private TglPoolConfig tglPoolConfig;
    Connection connection;

    public ExcelModelListener() {
        commodityOrder = new CommodityOrder();
    }


    public void onException(Exception e, AnalysisContext analysisContext) {
        log.info("文件操作异常，异常信息为:{}", e);
    }

    public void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext) {
        log.info("========================从这里开始消费队列中的业务数据===========================");
        TglRaftThreadPool.execute(()-> {
            commodityOrder.saveData();
        });
    }

    /**
     * 这个每一条数据解析都会来调用
     */
    public void invoke(CheckTransInfoDto data, AnalysisContext analysisContext) {
        // (Boolean) TglThreadDataCache.getDataCache()
        if (false){
            tempData.add(data);
            if (indexNumPt.incrementAndGet() % 10000 == 0){
                log.info("read Excel Data is value:>>>>>{}" + tempData.size() + "====输出线程对象::{}"
                        + Thread.currentThread().getName() + Thread.currentThread().getId());
                commodityOrder.addBatchCommodityOrders(tempData);
                tempData.clear();
            }
        } else {
            try {
                // todo 充分自测下
                CommodityOrderQueue.TEMPDATATO.put(data);
//                int tempIndex = indexNum.incrementAndGet();
//                if (tempIndex % 10000 == 0){
//                    log.info("read Excel Data is value:>>>>>{}tempIndex data value:{}", tempIndex, CommodityOrderQueue.TEMPDATATO.size(),
//                            ";输出线程对象::{}", Thread.currentThread().getName() + Thread.currentThread().getId());
//                    TglRaftThreadPool.execute(()-> {
//                        try {
//                            commodityOrder.addBatchCommodityOrder(connection,countDownLatch);
//                            countDownLatch.wait();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param analysisContext
     */
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        log.info("所有数据解析完成!==>{}", CommodityOrderQueue.TEMPDATATO.size());
        log.info("所有数据解析完成!==>{}", tempData.size());

        TglRaftThreadHelper.sleep(5000);
    }


    @Override
    public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {

    }


    public boolean hasNext(AnalysisContext analysisContext) {
        return true;
    }
}
