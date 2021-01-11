package com.net.system.service.sysmange;

import java.util.List;

import com.net.system.model.Menu;
import com.net.system.model.Operator;


public interface OperatorService {


    public void deleteByPrimaryKey(Integer operatorId);

    public int add(Operator operator);

    public Operator selectByPrimaryKey(Integer operatorId);

    public int updateByPrimaryKey(Operator operator);

    public List<Operator> selectByMenuId(Integer menuId);

    public List<Operator> selectAll();

    public List<Menu> getALLMenuAndOperatorTree();
}
