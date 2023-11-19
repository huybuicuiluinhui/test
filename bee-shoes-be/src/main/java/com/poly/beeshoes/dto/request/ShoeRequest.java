package com.poly.beeshoes.dto.request;

import com.poly.beeshoes.infrastructure.common.PageableRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class ShoeRequest extends PageableRequest {
    private Long id;
    @NotEmpty(message = "Tên không được để trống!")
    private String name;
    @NotNull(message = "Thương hiệu không được để trống!")
    private Long brand;
    @NotNull(message = "Danh mục không được để trống!")
    private Long category;
    private Boolean status;

    private String color;
    private String size;
    private String sole;

    private List<String> colors;
    private List<String> sizes;
    private List<String> soles;

    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
