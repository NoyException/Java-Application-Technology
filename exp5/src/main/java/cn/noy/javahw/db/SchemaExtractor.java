package cn.noy.javahw.db;

import java.sql.*;

public class SchemaExtractor {
    private final DBConnector connector;

    public SchemaExtractor(DBConnector connector) {
        this.connector = connector;
    }

    public Schema extractSchema(String tableName) throws SQLException {
        DatabaseMetaData metaData = connector.getConnection().getMetaData();
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, "", tableName);
        String primaryKey = null;
        while (primaryKeys.next()){
            String tableCat = primaryKeys.getString("TABLE_CAT");
            if(!tableCat.equals(connector.getDatabaseName()))
                continue;
            primaryKey = primaryKeys.getString("COLUMN_NAME");
        }
        ResultSet columns = metaData.getColumns(null, "", tableName, null);

        Schema schema = new Schema(tableName);

        while (columns.next()) {
            String tableCat = columns.getString("TABLE_CAT");
            if(!tableCat.equals(connector.getDatabaseName()))
                continue;

            String type = columns.getString("TYPE_NAME");
            long size = columns.getLong("COLUMN_SIZE");
            String name = columns.getString("COLUMN_NAME");
            boolean nullable = columns.getInt("NULLABLE") == 1;
//            int decimalDigits = columns.getInt("DECIMAL_DIGITS");
            String remarks = columns.getString("REMARKS");

            Column column = new Column(name, DataType.getDataType(type), size, nullable, remarks);
            schema.addColumn(column);
            if(name.equals(primaryKey))
                schema.setPrimaryKey(column);
        }

        return schema;
    }
}
