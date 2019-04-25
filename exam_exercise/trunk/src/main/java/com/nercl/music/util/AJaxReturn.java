package com.nercl.music.util;

public class AJaxReturn<T>
{
    // 返回码
    private int returnCode;

    // 返回消息
    private String returnMsg;

    // 返回内容
    private T returnData;

    public AJaxReturn()
    {}

    public int getReturnCode()
    {
        return returnCode;
    }

    public void setReturnCode(int returnCode)
    {
        this.returnCode = returnCode;
    }

    public String getReturnMsg()
    {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg)
    {
        this.returnMsg = returnMsg;
    }

    public T getReturnData()
    {
        return returnData;
    }

    public void setReturnData(T returnData)
    {
        this.returnData = returnData;
    }

}
