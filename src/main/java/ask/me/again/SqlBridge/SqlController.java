package ask.me.again.SqlBridge;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SqlController {

  private final Jdbi jdbi;

  @ExceptionHandler({Exception.class})
  public ResponseEntity<List<List<String>>> handleException(Exception e) {
    var response = List.of(List.of("Error"), List.of(e.getMessage()));
    return ResponseEntity.badRequest().body(response);
  }

  @GetMapping("/execute")
  public ResponseEntity<List<List<String>>> execute(@RequestBody String sqlCommand) {
    jdbi.withHandle(handle -> handle.execute(sqlCommand));

    var response = List.of(List.of("Ok"));
    return ResponseEntity.badRequest().body(response);
  }

  @GetMapping("/query")
  public List<ArrayList<String>> query(@RequestParam Map<String, String> allRequestParams, @RequestBody String sqlCommand) {
    var columnNames = new ArrayList<String>();
    var index = new AtomicInteger();

    var results = jdbi.withHandle(handle -> createHandle(sqlCommand, handle, allRequestParams)
      .map((rs, ctx) -> {
        var firstIteration = columnNames.isEmpty();
        var meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        var list = new ArrayList<String>(columnCount);
        list.add(index.incrementAndGet() + "");
        for (int i = 1; i <= columnCount; i++) {
          list.add(rs.getString(i));

          if (firstIteration) {
            columnNames.add(meta.getColumnName(i));
          }
        }

        return list;
      }).stream()
      .collect(Collectors.toList()));

    columnNames.add(0, "#");
    results.add(0, columnNames);

    return results;
  }

  private Query createHandle(String sqlCommand, Handle handle, Map<String, String> allRequestParams) {
    var query = handle.createQuery(sqlCommand);

    for (var kv : allRequestParams.entrySet()) {
      query = query.define(kv.getKey(), kv.getValue());
    }

    return query;
  }
}
