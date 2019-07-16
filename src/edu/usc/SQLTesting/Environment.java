package edu.usc.SQLTesting;

public class Environment
{
    public int rowSize, fieldSize, querySize;
    public boolean isFixed;
    public String className;
    public String methodName;
    public Environment(String className, String methodName, int rowSize, int fieldSize, int querySize, boolean isFixed)
    {
        this.className = className;
        this.methodName = methodName;
        this.rowSize = rowSize;
        this.fieldSize = fieldSize;
        this.querySize = querySize;
        this.isFixed = isFixed;
    }
}