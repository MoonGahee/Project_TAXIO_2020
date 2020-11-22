package com.example.project_taxio_2020;

public class GeneralNumberSingletone{
    private static GeneralNumberSingletone generalNum = null;
    private String data = "";
    public static GeneralNumberSingletone getInstance()
    {
        if(generalNum == null)
        {
            generalNum = new GeneralNumberSingletone();
        }
        return generalNum;
    }
    public String getGeneralNum()
    {
        return this.data;
    }
    public void setGeneralNum(String data)
    {
        this.data = data;
    }
}
