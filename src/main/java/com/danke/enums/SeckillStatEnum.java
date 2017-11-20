package com.danke.enums;

/**
 * 使用枚举表示常量数据字典
 * Created by xinxin on 2017/11/4.
 */
public enum SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");

    private int state;
    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStatEnum statOf(int index) {
        for (SeckillStatEnum seckillStatEnum : values()) {
            if (seckillStatEnum.getState() == index) {
                return seckillStatEnum;
            }
        }
        return null;
    }
}
