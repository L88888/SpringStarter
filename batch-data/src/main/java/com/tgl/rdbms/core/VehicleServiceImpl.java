package com.tgl.rdbms.core;

import com.tgl.rdbms.fileutils.RaftUtils;
import com.tgl.rdbms.concurrent.TglRaftThreadHelper;
import com.tgl.rdbms.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @program: spring-starter
 * @description:
 * @author: LIULEI-TGL
 * @create: 2021-06-27 17:42:
 **/
@Service
public class VehicleServiceImpl {

    @Autowired
    private DbHelp dbHelp;

    public int insertVehicleinfo() {
        List<String> province = Arrays.asList("京","津","沪","渝","冀","豫","云","辽","黑","湘","皖","鲁","新","苏","浙","赣","鄂","桂","甘","晋","蒙","陕","吉","闽","贵","粤","青","藏","川","宁","琼","使","领");
        Random random = new Random();
        Integer [] colors = {1,2,5,52,99,9,6};
        Map colorsMap = new HashMap();
        colorsMap.put(1,"黑色");
        colorsMap.put(2,"白色");
        colorsMap.put(5,"蓝色");
        colorsMap.put(52,"黄绿");
        colorsMap.put(99,"其它");
        colorsMap.put(9,"绿色");
        colorsMap.put(6,"黄色");

        //固定设备数量(20)
        List<String> deviceIds = new ArrayList<>();
        deviceIds.add("52030200051190000001");
        deviceIds.add("52030200051190000002");
        deviceIds.add("52030200051190000003");
        deviceIds.add("52030200051190000004");
        deviceIds.add("52030200051190000005");
        deviceIds.add("52030200051190000006");
        deviceIds.add("52030200051190000007");
        deviceIds.add("52030200051190000008");
        deviceIds.add("52030200051190000009");
        deviceIds.add("52030200051190000010");
        deviceIds.add("52030200051190000011");
        deviceIds.add("52030200051190000012");
        deviceIds.add("52030200051190000013");
        deviceIds.add("52030200051190000014");
        deviceIds.add("52030200051190000015");
        deviceIds.add("52030200051190000016");
        deviceIds.add("52030200051190000017");
        deviceIds.add("52030200051190000018");
        deviceIds.add("52030200051190000019");
        deviceIds.add("52030200051190000020");
        List insertInfo = new CopyOnWriteArrayList();
        // TODO 需要构造一辆重复的车牌号在不同的设备底下进行抓拍
        //获取到固定车牌号
        List<String> plateNos = getPlateNos(20);
        if (CollectionUtils.isEmpty(plateNos)){
            return 0;
        }
        Boolean flag = true;
        while(flag){
            int randomPlatNo = random.nextInt(20);
            String plateNo = plateNos.get(randomPlatNo);
            int randomColor = random.nextInt(6);
//            int randomDispositionCount = random.nextInt(2);
            //设备
            int randomDevice = random.nextInt(deviceIds.size());
//        List dispositionList = new ArrayList();
//        for (int k = 0;k <= randomDispositionCount;k++){
//            dispositionList.add("DE0006000000012020122220020940000" + j + "_" + k);
//        }
            int plateColor = colors[randomColor];
            Vehicle vehicleEntity = new Vehicle();
            vehicleEntity.setId(UUID.randomUUID().toString());
            vehicleEntity.setPlate_type((String) colorsMap.get(plateColor));
            vehicleEntity.setPlate_no(plateNo);
            String speed = String.valueOf(random.nextInt(120));
            vehicleEntity.setSpeed(speed);
            vehicleEntity.setAppear_time(RaftUtils.getCurrentDate());
            vehicleEntity.setMark_time(RaftUtils.getCurrentDate());

            // TODO 需要构造多个设备编号至少20个
            vehicleEntity.setDevice_id(deviceIds.get(randomDevice));
            vehicleEntity.setVehicle_image("http://172.20.32.191:2089/down/vehicle/bigimage.jpg");
            vehicleEntity.setScene_image("http://172.20.32.191:2089//down/vehicle/smallimage.jpg");
            vehicleEntity.setVehicle_color((String) colorsMap.get(colors[random.nextInt(6)]));
            vehicleEntity.setArea_code("520302");
            vehicleEntity.setLine_no(String.valueOf(random.nextInt(8)));
            vehicleEntity.setPass_time(RaftUtils.getCurrentDate());
            vehicleEntity.setPlate_color(String.valueOf(plateColor));
            vehicleEntity.setPlate_describe("车辆于"+RaftUtils.getCurrentDate() + "以车速" + speed + "通过");
            vehicleEntity.setDisappear_time(RaftUtils.getCurrentDate());
            vehicleEntity.setVehicle_class("1");
            insertInfo.add(vehicleEntity);
            if(insertInfo.size() % 10000 == 0){
                dbHelp.addBatchVehiclesblacklibDetailedInfo(insertInfo);
                insertInfo = new CopyOnWriteArrayList();
                TglRaftThreadHelper.sleep(1000);
            }
        }
        return 1;
    }

    /**
     * 生成车牌号
     * @return
     */
    public static List<String> getPlateNos(int size){
        if(size <= 0){
            return null;
        }
        List <String>province = Arrays.asList("京","津","沪","渝","冀","豫","云","辽","黑","湘","皖","鲁","新","苏","浙","赣","鄂","桂","甘","晋","蒙","陕","吉","闽","贵","粤","青","藏","川","宁","琼","使","领");
        List<String> plateNos = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Random random = new Random();
            int randomProvice = random.nextInt(6);
            int randomNum = random.nextInt(89999)+10000;
            char ran = (char)(random.nextInt(25)+65);
            String  str = province.get(randomProvice) + ran + randomNum;
            plateNos.add(str);
        }
        return plateNos;
    }
}
