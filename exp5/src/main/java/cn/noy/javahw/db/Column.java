package cn.noy.javahw.db;

import cn.noy.javahw.util.CaseConverter;

public record Column(String name, DataType type, long size, boolean nullable, String remark) {

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", size=" + size +
                ", nullable=" + nullable +
                ", remark='" + remark + '\'' +
                '}';
    }

    public String getJavaName(){
        return CaseConverter.toCamelCase(name, false);
    }

    public String generateJavaField() {
        String javaType = type.getJavaType().getSimpleName();
        String javaName = getJavaName();
        return String.format("private %s %s;", javaType, javaName);
    }
}
