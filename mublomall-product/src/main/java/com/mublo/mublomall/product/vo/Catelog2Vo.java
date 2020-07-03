package com.mublo.mublomall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Catelog2Vo {
    private Long id;
    private String name;
    private Long prantenId;
    private List<Catelog2Vo> catelog2Vos;
}
