package com.cbse.flighthub.base.entity;

import com.cbse.flighthub.base.enums.MembershipEnum;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

@Data
@Component
@Document("memberships")
public class Membership {
    private String id;
    private MembershipEnum membership;
    private int minPoints;
}
