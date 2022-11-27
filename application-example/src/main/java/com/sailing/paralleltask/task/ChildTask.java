package com.sailing.paralleltask.task;

import com.google.common.collect.Lists;
import com.sailing.paralleltask.poolutil.SitTaskProcess;
import com.sailing.paralleltask.service.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @ClassName ChildTask
 * @Description 创建子任务对象，获取业务数据，消费解析业务数据，停止某个任务的线程池执行
 * @Author Liulei
 * @Date 2022/11/25 16:13
 * @Version 1.0
 **/
@Slf4j
public class ChildTask {
    // 定义线程池大小，默认3
    private final int POOL_SIZE = 3;
    // 定义数据拆解大小，默认是4
    private final int SPLIT_SIZE = 4;
    // 定义任务名称，用于优雅退出以及停止线程池
    private String taskName;
    // 接收jvm停机关闭信号，实现优雅退出停机
    private volatile boolean jvmTerminal = false;

    // 定义任务名称
    public ChildTask(String taskName){
        this.taskName = taskName;
    }

    /**
     * 永动执行：如果未收到停机命令、任务需要一直执行下去；
     */
    public void doExecutor(){
        List<Template> sourceDatas;

        int i = 0;
        while (true){
            log.info("任务:>{}开始执行,执行周期:>{}", this.taskName, i);
            // 生产数据
            sourceDatas = this.queryData();
            // 消费业务对象数据
            this.taskExecutor(sourceDatas);
            log.info("任务:>{}结束执行,执行周期:>{}", this.taskName, i);

            if (jvmTerminal){
                break;
            }
            i++;
        }

        // 删除任务以及退出线程池
        SitTaskProcess.recoveryExecutor(this.taskName);
    }

    /**
     * 接收外部的停机请求，开始停止执行线程池任务，线程池开始退出
     */
    public void jvmTerminal(){
        // 立刻开始停机
        this.jvmTerminal = true;
        log.info("任务名称>{}, 任务状态:>{}", this.taskName, this.jvmTerminal);
    }

    /**
     * 处理单个任务数据
     * 多线程执行任务：需要把数据拆分为4份，然后分别由多线程并发执行，这里可以通过线程池来支持；
     * @param sourceDatas
     */
    private void taskExecutor(List<Template> sourceDatas){
        if (CollectionUtils.isEmpty(sourceDatas)){
            return;
        }

        // 开始对数据进行拆分
        List<List<Template>> sources = Lists.partition(sourceDatas, SPLIT_SIZE);
        // 多线程情况下防止任务不执行
        CountDownLatch downLatch = new CountDownLatch(sources.size());

        // 开启多线程支持
        for (final List<Template> templates : sources){
            ExecutorService executorService =
                    SitTaskProcess.getOrInitExecutor(this.taskName, POOL_SIZE);
            // submit() 该方法签名使得线程执行结束后返回指定的执行结果，可以接收线程内部业务过程执行的结果数据
            executorService.submit(()->{
                this.processBussData(templates, downLatch);
            });

            // execute() 该方法签名使得线程执行不会返回任何执行结果
            executorService.execute(()->{
                this.processBussData(templates, downLatch);
            });
        }

        try {
            downLatch.await();
        } catch (InterruptedException e) {
            log.debug("CountDownLatch 计数对象等待异常:>{}", e);
        }
    }

    /**
     * 消费处理得到的业务数据对象
     * @param templates 业务数据集合
     * @param downLatch 计数器递减
     */
    private void processBussData(List<Template> templates, CountDownLatch downLatch){
        if (Objects.isNull(templates)){
            log.info("无法处理未定义的业务对象集合:>{}", templates);
            return;
        }

        try {
            for (Template template: templates){
                log.info("模板名称:>{}, 任务名称:>{}, 线程名称和ID:>{}",
                        template.getLmName(),
                        this.taskName,
                        Thread.currentThread().getName() + Thread.currentThread().getId());

                // 处理完一个业务对象可以适当的停止下
                Thread.sleep(1000L);
            }
        } catch (Exception e) {
            log.debug("业务数据处理异常:>{}", e.fillInStackTrace());
        } finally {
            if (Objects.nonNull(downLatch)){
                downLatch.countDown();
            }
        }
    }

    /**
     * 获取模板基本信息集合
     * 获取永动任务的数据：一般都是扫描db或者其他api接口不断获取数据
     * @return
     */
    private List<Template> queryData(){
        // 自动生成5条模板对象数据
        List<Template> valData = new ArrayList<>(100);
        for (int i = 0; i < 10;i++){
            valData.add(new Template().setLmName("模板基本信息:" + i));
        }
        return valData;
    }
}
