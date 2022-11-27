//package com.sailing.tgl.tests.etcd;
//
//import com.tgl.designpattern.concurrent.TglRaftThread;
//import com.tgl.designpattern.concurrent.TglRaftThreadPool;
//import com.tgl.designpattern.etcd.EtcdInit;
//import com.tgl.designpattern.etcd.EtcdInitImpl;
//import io.netty.buffer.PooledByteBufAllocator;
//import org.junit.Test;
//import org.springframework.models.StopWatch;
//
//import java.models.ArrayList;
//import java.models.List;
//import java.models.concurrent.ConcurrentLinkedQueue;
//import java.models.concurrent.ExecutionException;
//
///**
// * @program: spring-starter
// * @description:
// * @author: LIULEI-TGL
// * @create: 2021-08-22 21:33:
// **/
//public class EtcdTest {
//
//    @Test
//    public void putVal(){
//        try {
//            EtcdInit etcdInit = new EtcdInitImpl();
//            etcdInit.put("123123","wqeqweqw");
//
//            ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue();
//            concurrentLinkedQueue.size();
//
//            concurrentLinkedQueue.poll();
//
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.toString();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testPj(){
//        List<Org> orgs = new ArrayList<>();
//        Org org = null;
//        for (int i =0; i < 10000;i++){
//            org = new Org();
//            org.setProvinceId(i + "");
//            orgs.add(org);
//        }
//
//        long start = System.currentTimeMillis();
//        this.buildProvince(orgs);
//        long end = System.currentTimeMillis();
//        System.out.println(end - start);
//    }
//
//     String buildProvince(List<Org> orgs){
//        StringBuilder sb = new StringBuilder();
//       for(Org org:orgs){
//           sb.append(org.getProvinceId());
//           sb.append(",");
//       }
//
////       orgs.stream().forEach(v->{
////           sb.append(v.getProvinceId());
////           sb.append(",");
//////           v.getProvinceId();
////         });
//
////       orgs.parallelStream().forEach(v->{
////           sb.append(v.getProvinceId());
////           sb.append(",");
////       });
//
//       sb = sb.delete(sb.lastIndexOf(","), sb.length());
//       return sb.toString();
//   }
//
//    public static void main(String[] ager){
//        try {
//            EtcdInit etcdInit = new EtcdInitImpl();
////            etcdInit.put("123123","wqeqweqw");
//
////            StopWatch stopWatch = new StopWatch();
////            stopWatch.start("开始etcd入库");
//            if (false){
//                for (int i =0;i < 10000;i++){
//                    final String key = i + "";
//                    TglRaftThreadPool.execute(()->{
//                        try {
//                            etcdInit.put(key,"wqeqweqw");
//                        } catch (ExecutionException e) {
//                            e.printStackTrace();
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    });
//                }
//            }
//
//            for (int i =0;i < 2000;i++){
//                final String key = i + "";
//                TglRaftThreadPool.execute(()->{
//                    try {
//                        long s1 = System.currentTimeMillis();
//                        etcdInit.get(key);
//                        System.out.println(System.currentTimeMillis() - s1);
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//
////            stopWatch.stop();
////            stopWatch.prettyPrint();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
//
//class Org{
//    private String provinceId;
//
//    public void setProvinceId(String provinceId){
//        this.provinceId = provinceId;
//    }
//
//    public String getProvinceId(){
//        return provinceId;
//    }
//}
