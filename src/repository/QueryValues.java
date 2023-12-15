package repository;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class QueryValues {
    private String query;
    private List<Object> values;
}
