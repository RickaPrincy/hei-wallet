package repository;

import java.util.List;
import java.util.stream.Collectors;

public class Query {
    public final static String ID_LABEL = "id";
    public static String toSqlName(String text){
        return "\"" + text +  "\"";
    }
    public static String mapKeys(List<String> keys, String separator, String suffix) {
        return keys.stream().map(el -> toSqlName(el) + suffix).collect(Collectors.joining(separator));
    }
    public static String transaction(List<String> queries){
        return "BEGIN;\n" + String.join("\n", queries) + "\nCOMMIT;";
    }

    public static String selectAll(String tableName, List<String> filters, String suffix){
        String query = "SELECT * FROM " + toSqlName(tableName);
        if(filters != null && !filters.isEmpty())
            query += " WHERE " + mapKeys(filters, " AND ", "=?");
        if(suffix != null){
            query += suffix;
        }
        return query + " ;";
    }

    public static String saveOrUpdate(String tableName, List<String> columns){
        String query;
        if(columns.contains(Query.ID_LABEL)){
            List<String> withoutId = columns.stream().filter(el -> !el.equals(ID_LABEL)).collect(Collectors.toList());
            query = "UPDATE " + toSqlName(tableName) + " SET " +
                    mapKeys(withoutId, " , ", "=?") +
                    " WHERE " + toSqlName(ID_LABEL) + " =?";
        }else{
            query = "INSERT INTO " + toSqlName(tableName) +  "("  +
                mapKeys(columns, ",", "") + ") VALUES ( ?" +
                " ,?".repeat(columns.size() - 1) + " )";
        }
        return query + " ;";
    }
}