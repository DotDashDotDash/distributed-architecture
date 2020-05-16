package org.codwh.redis.custom.model.ps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PsMenu implements Serializable {

    private static final long serialVersionUID = -598903822193151281L;

    /**
     * 主键id
     */
    private Integer id;

    /**
     * menu标题
     */
    private String title;

    /**
     * menu图像
     */
    private String icon;

    /**
     * menu链接
     */
    private String href;

    /**
     * 父级menu id
     */
    private Integer parentId;

    /**
     * 是否无法展开成子menu
     */
    private String isLeaf;

    /**
     * 是否展开
     */
    private Integer spread;

    /**
     * 子menu列表
     */
    private List<PsMenu> children = new ArrayList<PsMenu>();

    /**
     * 所属用户id
     */
    private Integer psUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(String isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getSpread() {
        return spread;
    }

    public void setSpread(Integer spread) {
        this.spread = spread;
    }

    public List<PsMenu> getChildren() {
        return children;
    }

    public void setChildren(List<PsMenu> children) {
        this.children = children;
    }

    public Integer getPsUserId() {
        return psUserId;
    }

    public void setPsUserId(Integer psUserId) {
        this.psUserId = psUserId;
    }
}
