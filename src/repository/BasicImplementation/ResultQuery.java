package repository.BasicImplementation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.ResultSet;

@Getter
@AllArgsConstructor
public class ResultQuery {
    private ResultSet resultSet;
    private int columnCount;
}