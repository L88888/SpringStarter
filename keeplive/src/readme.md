一、keeplive 检测登录账户信息，判断用户是否在线

二、自定义线程池
TglRaftThread:>线程池统统一异常处理
TglRaftThreadHelper:>线程池基本配置
TglRaftThreadPool:>自定义线程池对象
    一、支持线程池对象：
    1、ThreadPoolExecutor；创建初始化的线程池对象，添加自定义的线程执行拒绝策略配置;
    2、ScheduledExecutorService，定时任务创建线程对象;
    二、支持多线程任务执行种类：
    1、submit；可接收每个线程执行完的结果情况；
    2、execute；不需要接收线程执行的结果；
TglRaftThreadPoolExecutor:>监控线程的执行情况,创建自定义线程池对象；
TglThreadDataCache:>threadlocal应用封装;

应用实例
图片下载：
com.tgl.rdbms.service.DownloadImagesImpl.getImages();

