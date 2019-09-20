package com.chorifa.shorturl.dataobject;

public class IDDataObject {
    private Integer id;

    private String bizTag;

    private Integer step;

    private Long curVal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBizTag() {
        return bizTag;
    }

    public void setBizTag(String bizTag) {
        this.bizTag = bizTag;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public Long getCurVal() {
        return curVal;
    }

    public void setCurVal(Long curVal) {
        this.curVal = curVal;
    }
}