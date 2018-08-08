package com.datastory.commons3.monitor.pojo;

/**
 * 指标信息描述
 */
public class TargetInfo {

    private String targetKey;
    private Class targetClass;
    private Object targetValue;

    public TargetInfo(){}

    public TargetInfo(String targetKey, Class targetClass) {
        this.targetKey = targetKey;
        this.targetClass = targetClass;
    }

    public TargetInfo(String targetKey, Class targetClass,Object targetValue) {
        this.targetKey = targetKey;
        this.targetClass = targetClass;
        this.targetValue = targetValue;
    }

    @Override
    public String toString() {
        return this.targetKey + ":" + targetValue;
    }

    public String getTargetKey() {
        return targetKey;
    }
    public void setTargetKey(String targetKey) {
        this.targetKey = targetKey;
    }
    public Class getTargetClass() {
        return targetClass;
    }
    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }
    public Object getTargetValue() {
        return targetValue;
    }
    public void setTargetValue(Object targetValue) {
        this.targetValue = targetValue;
    }

    public static void main(String[] args){
        Class a = TargetInfo.class;

        System.out.println(TargetInfo.class);
    }

}