package com.winds.bm.dto;

import java.io.Serializable;
import java.util.List;

public class MenuTree implements Serializable {
    private String path;
    private String name;
    private String label;
    private String icon;
    private String url;

    private List<MenuTree> children;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MenuTree> getChildren() {
        return children;
    }

    public void setChildren(List<MenuTree> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "MenuTree{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", label='" + label + '\'' +
                ", icon='" + icon + '\'' +
                ", url='" + url + '\'' +
                ", children=" + children +
                '}';
    }
}
