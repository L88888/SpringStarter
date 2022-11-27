package com.tgl.raft.cluster;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

/**
 * @program: spring-starter
 * @description: 集群模式下的同伴节点,当前节点
 * @author: LIULEI-TGL
 * @create: 2021-05-18 21:47:
 **/
@Setter
@Getter
@ToString
public class Peer {

    /** self(ip) 自己的ip */
    private final String addr;

    public Peer(String addr){
        this.addr = addr;
    }

    @Override
    public boolean equals(Object o){
        if (this == null){
            return false;
        }

        if (o == null || this.getClass() != o.getClass()){
            return false;
        }

        Peer peer = (Peer) o;
        // 判断对象属性值，全系统保持一致
        return this.addr == peer.addr;
    }

    @Override
    public int hashCode(){
        return Objects.hash(this.addr);
    }
}
