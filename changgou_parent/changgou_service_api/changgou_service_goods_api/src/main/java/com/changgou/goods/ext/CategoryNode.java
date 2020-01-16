package com.changgou.goods.ext;

import com.changgou.goods.pojo.Category;

import java.util.List;

public class CategoryNode extends Category {

    private List<CategoryNode> children;

    public List<CategoryNode> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryNode> children) {
        this.children = children;
    }
}
