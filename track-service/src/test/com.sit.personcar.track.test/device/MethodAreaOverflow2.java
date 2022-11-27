//package com.sit.personcar.track.test.device;
//
//import net.sf.cglib.beans.BeanGenerator;
//import org.springframework.cglib.proxy.Enhancer;
//import org.springframework.cglib.proxy.MethodInterceptor;
//import org.springframework.cglib.proxy.MethodProxy;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//public class MethodAreaOverflow2 {
//    static class OOMObject {
//    }
//
//    public static void main(String[] args) {
//        MethodAreaOverflow2 methodAreaOverflow2 = new MethodAreaOverflow2();
//        methodAreaOverflow2.testObject();
//    }
//
//    private void testMemOOM(){
//        while (true) {
//            Enhancer enhancer = new Enhancer();
//            enhancer.setSuperclass(OOMObject.class);
//            enhancer.setUseCache(false);
//            enhancer.setCallback(new MethodInterceptor() {
//                @Override
//                public Object intercept(Object obj, Method method,
//                                        Object[] args, MethodProxy proxy) throws Throwable {
//                    return method.invoke(obj, args);
//                }
//            });
//            OOMObject proxy = (OOMObject) enhancer.create();
//            System.out.println(proxy.getClass());
//        }
//    }
//
//    public void testObject(){
//        try {
//            BeanGenerator beanGenerator = new BeanGenerator();
//            beanGenerator.addProperty("name",String.class);
//            Object o1 = beanGenerator.create();
//
//            Method m1 = o1.getClass().getMethod("setName",String.class);
//            m1.invoke(o1,"zxx");
//
//            Method m2 = o1.getClass().getMethod("getName");
//            System.out.println(m2.invoke(o1));
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
//    }
//}
