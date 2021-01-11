package com.net.system.mapper.sysmange;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.net.system.model.BusinessAddress;

@Mapper
public interface BusinessAddressMapper {

    int updateDynamic(BusinessAddress businessAddress);
    
    BusinessAddress selectInfo();

   
}