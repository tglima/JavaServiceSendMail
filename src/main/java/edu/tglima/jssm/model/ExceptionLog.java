package edu.tglima.jssm.model;

import com.google.cloud.Timestamp;

public class ExceptionLog
{
    public Timestamp dateRegister;
    public String projectSource;
    public String nmMethod;
    public String exceptionMessage;
    public String stackTrace;

    public ExceptionLog(){}

    public ExceptionLog(String projectSource, String nmMethod, String exceptionMessage, String stackTrace)
    {
        this.projectSource = projectSource;
        this.nmMethod = nmMethod;
        this.exceptionMessage = exceptionMessage;
        this.stackTrace = stackTrace;
        this.dateRegister = Timestamp.now();
    }

}
