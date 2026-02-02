package com.Project.LinkedIn.Connection_Service.Event;

import lombok.Builder;
import lombok.Data;

@Data
public class sentConnectionRequestEvent {
    private Long senderId;
    private Long receiverId;
}
