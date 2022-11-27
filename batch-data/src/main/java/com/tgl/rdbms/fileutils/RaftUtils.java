package com.tgl.rdbms.fileutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @program: spring-starter
 * @description: 定义程序中所需要的各种公共方法、变量等
 * @author: LIULEI-TGL
 * @create: 2021-05-23 15:02:
 **/
public class RaftUtils {

    private RaftUtils(){}

    private volatile static RaftUtils RAFTUTILS;

    /**
     * 创建单例对象
     * @return
     */
    public static RaftUtils getInstance(){
        if (RAFTUTILS == null){
            synchronized (RaftUtils.class){
                if (RAFTUTILS == null){
                    RAFTUTILS = new RaftUtils();
                }
            }
        }
        return RAFTUTILS;
    }

    /**
     * 客户端发送消息至服务器链路超时时间。默认：3分钟
     */
    public final int TIMEOUT = 200000;

    /**
     * 用于在日志下标转换时使用
     * @param l
     * @return
     */
    public final long convert(Long l){
        if (l == null){
            return 0;
        }
        return 1;
    }

    /**
     * 获取db中最后下标值的key 字节数据
     * @return
     */
    public final byte[] getLastIndexKey(){
        return "LAST_INDEX_KEY".getBytes();
    }

    /**
     * 获取0下标值的字节数据
     * @return
     */
    public final byte[] getZeroIndexKey(){
        return "0".getBytes();
    }

    public final byte[] getInvalidIndexKey(){
        return "-1".getBytes();
    }

    public final byte[] getIndexConvert(Long index){
        return index.toString().getBytes();
    }

    /**
     * 获得当前时间，格式 yyyy-MM-dd HH:mm:ss
     * return Date
     *
     */
    public static Date getDate() {
        Date now = new Date();
        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            now = dtf.parse(dtf.format(System.currentTimeMillis()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 获得当前时间，格式 yyyy-MM-dd HH:mm:ss
     * return String
     */
    public static String getCurrentDate() {
        return getCurrentDate("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获得当前时间，格式自定义
     *
     */
    public static String getCurrentDate(String format) {
        Calendar day = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(day.getTime());
    }
}
