package com.sailing.parallel.simple;

import com.sailing.parallel.basedto.BannerDTO;
import com.sailing.parallel.basedto.BaseResDTO;
import com.sailing.parallel.basedto.LabelDTO;
import com.sailing.parallel.basedto.UserInfoDTO;
import com.sailing.parallel.requestvo.*;
import com.sailing.parallel.service.IBannerService;
import com.sailing.parallel.service.ILabelService;
import com.sailing.parallel.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @program: spring-starter
 * @description: 统一调度类
 * @author: LIULEI-TGL[知行合一]
 * @create: 2022-05-29 16:49:
 **/
@Service
@Slf4j
public class DispatchIndex {

    @Autowired
    private IUserService userService;

    @Autowired
    private ILabelService labelService;

    @Autowired
    private IBannerService bannerService;

    /**
     * 首页中各个栏目的加载分别采用多线程模式并行获取结果数据
     * @param req
     * @return
     */
    public IndexHeadInfoResponse parallelQueryHeadPageInfo(InfoReq req){
        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 创建一个非阻塞线程服务对象CompletionService，用于提交与获取多线程并行执行结果
        CompletionService<BaseResDTO> completionService =
                new ExecutorCompletionService(executorService);

        // todo 需要考虑后期的扩展性，优化需要引入策略模式
        // 查询用户相关数据，callable带有返回值的调用方法
        Callable<BaseResDTO> userInfoDTOCallableTask = () -> {
            // 校验请求参数合法性
            UserInfoParam userInfoParam = buildUserParam(req);
            UserInfoDTO userInfoDTO = userService.queryUserInfo(userInfoParam);
            BaseResDTO baseResDTO = new BaseResDTO();
            baseResDTO.setKey("userInfoDTO");
            baseResDTO.setData(userInfoDTO);
            return baseResDTO;
        };
        // 查询横幅相关数据
        Callable<BaseResDTO> bannerDTOCallableTask = () -> {
            // 校验请求参数合法性
            BannerParam bannerParam = buildBannerParam(req);
            BannerDTO bannerDTO = bannerService.queryBannerInfo(bannerParam);
            BaseResDTO baseResDTO = new BaseResDTO();
            baseResDTO.setKey("bannerDTO");
            baseResDTO.setData(bannerDTO);
            return baseResDTO;
        };
        // 查询标签相关数据
        Callable<BaseResDTO> labelDTOCallableTask = () -> {
            // 校验请求参数合法性
            LabelParam labelParam = buildLabelParam(req);
            LabelDTO labelDTO = labelService.queryLabelInfo(labelParam);
            BaseResDTO baseResDTO = new BaseResDTO();
            baseResDTO.setKey("labelDTO");
            baseResDTO.setData(labelDTO);
            return baseResDTO;
        };

        // todo 需要考虑后期的扩展性，优化点单任务改为任务组模式
        // 开始提交线程任务
        completionService.submit(userInfoDTOCallableTask);
        completionService.submit(bannerDTOCallableTask);
        completionService.submit(labelDTOCallableTask);

        // 定义三个DTO来分别接收三个任务的返回值对象
        UserInfoDTO userInfoDTO = null;
        LabelDTO labelDTO = null;
        BannerDTO bannerDTO = null;
        try {
            // todo 需要考虑后期的扩展性，优化点循环固定任务池数量改为任务组长度
            // 开始获取任务的结果集合
            for (int i = 0;i < 3;i++){
                // 请求超时时间为1秒
                Future<BaseResDTO> baseResDTOFuture = completionService.poll(1, TimeUnit.SECONDS);
                BaseResDTO resData = baseResDTOFuture.get();
                switch (resData.getKey()){
                    case "userInfoDTO":
                        userInfoDTO = (UserInfoDTO) resData.getData();
                        break;
                    case "bannerDTO":
                        bannerDTO = (BannerDTO) resData.getData();
                        break;
                    case "labelDTO":
                        labelDTO = (LabelDTO) resData.getData();
                        break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
        return buildReponse(userInfoDTO, labelDTO, bannerDTO);
    }

    public UserInfoParam buildUserParam(InfoReq req){
        log.info("开始校验userinfo的请求参数:>{}", req);
        return new UserInfoParam();
    }

    public BannerParam buildBannerParam(InfoReq req){
        log.info("开始校验BannerParam的请求参数:>{}", req);
        return new BannerParam();
    }

    public LabelParam buildLabelParam(InfoReq req){
        log.info("开始校验LabelParam的请求参数:>{}", req);
        return new LabelParam();
    }

    private IndexHeadInfoResponse buildReponse(UserInfoDTO userInfoDTO,
            LabelDTO labelDTO,
            BannerDTO bannerDTO){
        return new IndexHeadInfoResponse(userInfoDTO, labelDTO, bannerDTO);
    }

    private void getIndexData(InfoReq req){
        IndexHeadInfoResponse index = parallelQueryHeadPageInfo(req);
        index.getData();
    }
}
