package cn.noy.javahw.db;

import lombok.Getter;

import java.util.function.Function;

public enum DataType {
    INT(Integer.class, Integer::parseInt),
    VARCHAR(String.class, s -> s),
    CHAR(String.class, s -> s),
    TEXT(String.class, s -> s),
    DATE(java.sql.Date.class, java.sql.Date::valueOf),
    DATETIME(java.sql.Timestamp.class, java.sql.Timestamp::valueOf),
    TIMESTAMP(java.sql.Timestamp.class, java.sql.Timestamp::valueOf),
    DECIMAL(Double.class, Double::parseDouble),
    FLOAT(Float.class, Float::parseFloat),
    DOUBLE(Double.class, Double::parseDouble),
    TINYINT(Integer.class, Integer::parseInt),
    SMALLINT(Integer.class, Integer::parseInt),
    MEDIUMINT(Integer.class, Integer::parseInt),
    BIGINT(Long.class, Long::parseLong),
    BIT(Boolean.class, s -> s.equals("1") || s.equalsIgnoreCase("true")),
    BLOB(String.class, s->s),
    LONGBLOB(String.class, s->s),
    ENUM(String.class, s->s),
    SET(String.class, s->s),
    JSON(String.class, s->s),
    TIME(java.sql.Time.class, java.sql.Time::valueOf),
    YEAR(Integer.class, Integer::parseInt),
    GEOMETRY(String.class, s->s),
    POINT(String.class, s->s),
    LINESTRING(String.class, s->s),
    POLYGON(String.class, s->s),
    BINARY(byte[].class, String::getBytes),
    VARBINARY(byte[].class, String::getBytes),
    ;

    private final Class<?> javaType;
    private final Function<String, ?> cast;

    <T> DataType(Class<T> javaType, Function<String, T> cast) {
        this.javaType = javaType;
        this.cast = cast;
    }

    public Object cast(String value) {
        if (value == null) {
            return null;
        }
        return cast.apply(value);
    }

    public String getStatementType() {
        if(javaType == Integer.class) {
            return "Int";
        }
        return javaType.getSimpleName();
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public static DataType getDataType(String type) {
        for (DataType dataType : DataType.values()) {
            if (dataType.name().equalsIgnoreCase(type)) {
                return dataType;
            }
        }
        return null;
    }

    public static Class<?> getJavaType(String type) {
        for (DataType dataType : DataType.values()) {
            if (dataType.name().equalsIgnoreCase(type)) {
                return dataType.javaType;
            }
        }
        return null;
    }

}
