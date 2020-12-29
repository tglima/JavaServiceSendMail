package edu.tglima.jssm.dto;
import edu.tglima.jssm.model.Message;

import java.util.Map;

public class ResultDTO
{
    public boolean HasError;
    public Object Value;
    public String ErrorMessage;
    public Map<String, Message> MapMessage;

    public ResultDTO(){}

    public ResultDTO(boolean hasError, String errorMessage, Object value, Map<String, Message> mapMessage)
    {
        HasError = hasError;
        ErrorMessage = errorMessage;
        Value = value;
        MapMessage = mapMessage;
    }

    public ResultDTO(boolean hasError)
    {
        HasError = hasError;
        ErrorMessage = null;
        Value = null;
        MapMessage = null;
    }

}
