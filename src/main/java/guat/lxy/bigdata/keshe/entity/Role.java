package guat.lxy.bigdata.keshe.entity;

import lombok.Data;
import java.io.Serializable;

@Data
public class Role implements Serializable {
    private Integer id;
    private String role;
}