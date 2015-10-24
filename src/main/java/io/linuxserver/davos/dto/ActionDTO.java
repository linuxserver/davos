package io.linuxserver.davos.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ActionDTO {

    public Long id;

    public String actionType;
    public String f1;
    public String f2;
    public String f3;
    public String f4;
    
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
