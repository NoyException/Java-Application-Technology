package cn.noy.expr.structure;

import cn.noy.expr.element.val.Value;

/**
 * 表达式节点<br>
 * 用于表示表达式树的节点<br>
 * 深度越低，优先级越低<br>
 * 例如：1+2*3，+为根节点，1为左子节点，*为右子节点，2为*的左子节点，3为*的右子节点<br>
 */
public abstract class ExpressionNode {
    private final ExpressionNode[] children;

    protected ExpressionNode(int childrenCnt) {
        this.children = new ExpressionNode[childrenCnt];
    }

    /**
     * 设置子节点
     * @param index 子节点索引
     * @param child 子节点
     */
    public void setChild(int index, ExpressionNode child) {
        this.children[index] = child;
    }

    /**
     * 获取子节点
     * @param index 子节点索引
     * @return 子节点
     */
    public ExpressionNode getChild(int index) {
        return this.children[index];
    }

    /**
     * 获取子节点个数
     * @return 子节点个数
     */
    public int getChildrenCount() {
        return this.children.length;
    }

    public abstract Value getValue();

    public String getStringValue(){
        return getValue().getStringValue();
    }

    public double getDoubleValue() {
        return getValue().getNumberValue().doubleValue();
    }

}
