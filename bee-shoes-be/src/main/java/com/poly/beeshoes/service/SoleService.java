package com.poly.beeshoes.service;

import com.poly.beeshoes.entity.Sole;
import com.poly.beeshoes.infrastructure.common.PageableObject;
import com.poly.beeshoes.infrastructure.request.SoleRequest;
import com.poly.beeshoes.infrastructure.response.SoleResponse;

public interface SoleService {
    PageableObject<SoleResponse> getAll(SoleRequest request);

    Sole getOne(Long id);

    Sole create(SoleRequest request);

    Sole update(Long id, SoleRequest request);

    Sole delete(Long id);
}
