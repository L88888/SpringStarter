package com.tgl.raft.cluster;

import lombok.Getter;

/**
 * @program: spring-starter
 * @description: 节点的三种状态枚举，跟随（follower）、候选人（candiddate）、领导（leader）
 * @author: LIULEI-TGL
 * @create: 2021-05-19 15:54:
 **/
public interface NodeStatus {

    // 跟随者节点状态
    int FOLLOWER = 0;
    // 候选者节点状态
    int CANDIDDATE = 1;
    // 领导人节点状态
    int LEADER = 2;

    /**
     * 节点状态枚举函数
     */
    @Getter
    public enum NodeStatusEnum{
        FOLLOWER(0),CANDIDDATE(1),LEADER(2),INVALID(-1);
        int code;
        NodeStatusEnum(int code){
            this.code = code;
        }

        public static NodeStatusEnum enumValue(int val){
            for (NodeStatusEnum v : NodeStatusEnum.values()){
                if (v.code == val){
                    return v;
                }
            }
            return INVALID;
        }
    }
}
