package com.example.lhycommonservice.eneity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lhy
 * @date 2024/9/6
 * @apiNote 类
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("product")
public class Product {
    @TableId
    private int id;

    private String name;

    private BigDecimal price;

    private Date date;

    private String remark;

}
