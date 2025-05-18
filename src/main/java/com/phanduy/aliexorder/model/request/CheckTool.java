package com.phanduy.aliexorder.model.request;


public class CheckTool extends BaseReqV3 {
    public String toolName;

    public CheckTool(
            String diskSerialNumber,
            String toolName
    ) {
        this.diskSerialNumber = diskSerialNumber;
        this.toolName = toolName;
    }
}
