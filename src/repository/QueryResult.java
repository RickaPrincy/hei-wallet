package repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class QueryResult {
    ResultSet result, generatedKeys;
}
