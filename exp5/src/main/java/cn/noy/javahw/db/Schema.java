package cn.noy.javahw.db;

import cn.noy.javahw.util.CaseConverter;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class Schema {
    private final String name;
    private Column primaryKey;
    @Getter
    private final Map<String, Column> columns;

    public Schema(String name) {
        this.name = name;
        this.columns = new HashMap<>();
    }

    public void addColumn(Column column) {
        this.columns.put(column.name(), column);
    }

    public Column getColumn(String name) {
        return this.columns.get(name);
    }

    public String generateJavaClass(String packageName){
        String className = CaseConverter.toCamelCase(this.name, true);
        String instanceName = CaseConverter.toCamelCase(this.name, false);

        StringBuilder sb = new StringBuilder();
        sb.append("package ").append(packageName).append(";\n\n");
        sb.append("import lombok.Data;\n");
        sb.append("import java.sql.*;\n\n");

        sb.append("/**\n");
        sb.append(" * Auto generated from table `").append(this.name).append("`\n");
        sb.append(" * @author Noy(3210105713)\n");
        sb.append(" */\n");
        sb.append("@Data\n");
        sb.append("public class ").append(className).append(" {\n");
        //填充字段
        for (Column column : columns.values()) {
            sb.append("\t").append(column.generateJavaField()).append("\n");
        }
        sb.append("\n");
        //填充insert方法
        sb.append("\tpublic void insert(Connection connection){\n");
        sb.append("\t\ttry {\n");
        sb.append("\t\t\tPreparedStatement statement = connection.prepareStatement(\"INSERT INTO `").append(this.name).append("`(");

        int i = 1;
        for (Column column : columns.values()) {
            sb.append("`").append(column.name()).append("`");
            if(i != columns.size())
                sb.append(", ");
            i++;
        }
        sb.append(") VALUES (");
        i = 1;
        for (Column ignored : columns.values()) {
            sb.append("?");
            if(i != columns.size())
                sb.append(", ");
            i++;
        }
        sb.append(")\");\n");
        i = 1;
        for (Column column : columns.values()) {
            sb.append("\t\t\tstatement.set").append(column.type().getStatementType()).append("(").append(i++).append(", ").append(column.getJavaName()).append(");\n");
        }
        sb.append("\t\t\tstatement.executeUpdate();\n");
        sb.append("\t\t} catch (SQLException e) {\n");
        sb.append("\t\t\tthrow new RuntimeException(e);\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n\n");

        //填充update方法
        sb.append("\tpublic void update(Connection connection){\n");
        sb.append("\t\ttry {\n");
        sb.append("\t\t\tPreparedStatement statement = connection.prepareStatement(\"UPDATE `").append(this.name).append("` SET ");
        i = 1;
        for (Column column : columns.values()) {
            sb.append("`").append(column.name()).append("` = ?");
            if(i != columns.size())
                sb.append(", ");
            i++;
        }
        sb.append(" WHERE `").append(primaryKey.name()).append("` = ?\");\n");
        i = 1;
        for (Column column : columns.values()) {
            sb.append("\t\t\tstatement.set").append(column.type().getStatementType()).append("(").append(i++).append(", ").append(column.getJavaName()).append(");\n");
        }
        sb.append("\t\t\tstatement.set").append(primaryKey.type().getStatementType()).append("(").append(i).append(", ").append(primaryKey.getJavaName()).append(");\n");
        sb.append("\t\t\tstatement.executeUpdate();\n");
        sb.append("\t\t} catch (SQLException e) {\n");
        sb.append("\t\t\tthrow new RuntimeException(e);\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n\n");

        //填充delete方法
        sb.append("\tpublic void delete(Connection connection){\n");
        sb.append("\t\ttry {\n");
        sb.append("\t\t\tPreparedStatement statement = connection.prepareStatement(\"DELETE FROM `").append(this.name).append("` WHERE `").append(primaryKey.name()).append("` = ?\");\n");
        sb.append("\t\t\tstatement.set").append(primaryKey.type().getStatementType()).append("(1, ").append(primaryKey.getJavaName()).append(");\n");
        sb.append("\t\t\tstatement.executeUpdate();\n");
        sb.append("\t\t} catch (SQLException e) {\n");
        sb.append("\t\t\tthrow new RuntimeException(e);\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n\n");

        //填充select方法
        sb.append("\tpublic static ").append(className).append(" select(Connection connection, ").append(primaryKey.type().getJavaType().getSimpleName()).append(" ").append(primaryKey.getJavaName()).append("){\n");
        sb.append("\t\ttry {\n");
        sb.append("\t\t\tPreparedStatement statement = connection.prepareStatement(\"SELECT * FROM `").append(this.name).append("` WHERE `").append(primaryKey.name()).append("` = ?\");\n");
        sb.append("\t\t\tstatement.set").append(primaryKey.type().getStatementType()).append("(1, ").append(primaryKey.getJavaName()).append(");\n");
        sb.append("\t\t\tResultSet resultSet = statement.executeQuery();\n");
        sb.append("\t\t\tif(resultSet.next()){\n");
        sb.append("\t\t\t\t").append(className).append(" ").append(instanceName).append(" = new ").append(className).append("();\n");
        for (Column column : columns.values()) {
            sb.append("\t\t\t\t").append(instanceName).append(".").append(column.getJavaName()).append(" = ");
            sb.append("resultSet.get").append(column.type().getStatementType()).append("(\"").append(column.name()).append("\")");
            sb.append(";\n");
        }
        sb.append("\t\t\t\treturn ").append(instanceName).append(";\n");
        sb.append("\t\t\t}\n");
        sb.append("\t\t\treturn null;\n");
        sb.append("\t\t} catch (SQLException e) {\n");
        sb.append("\t\t\tthrow new RuntimeException(e);\n");
        sb.append("\t\t}\n");
        sb.append("\t}\n");

        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Schema: ").append(this.name).append("\n");
        for (Column column : this.columns.values()) {
            sb.append(column).append("\n");
        }
        return sb.toString();
    }

    public void setPrimaryKey(Column primaryKey) {
        this.primaryKey = primaryKey;
    }
}
