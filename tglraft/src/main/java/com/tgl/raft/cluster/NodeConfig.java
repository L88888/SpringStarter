package com.tgl.raft.cluster;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @program: spring-starter
 * @description:  集群节点配置
 * @author: LIULEI-TGL
 * @create: 2021-05-19 12:58:
 **/
@Setter
@Getter
@ToString
public class NodeConfig {

    /** 自身端口selfPort */
    int selfPort;

    /** 所有节点地址集合,per每个节点信息 */
    List<String> perAddrs;
}
