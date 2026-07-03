package guat.lxy.bigdata.keshe.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class Product implements Serializable {
    private Integer id;
    private String name;
    private String photoUrl;
    private Double price;
    private String descp;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date releaseDate;

    private Integer catId;
    private String categoryName;
}