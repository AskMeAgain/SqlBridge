package ask.me.again.SqlBridge;

import lombok.RequiredArgsConstructor;
import org.jdbi.v3.core.Jdbi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SqlController {

    private final Jdbi jdbi;

    @GetMapping("/query")
    public List<ArrayList<String>> query(@RequestBody String sqlCommand) {
        var columnNames = new ArrayList<String>();
        var index = new AtomicInteger();

        var results = jdbi.withHandle(handle -> handle.createQuery(sqlCommand)
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

        columnNames.add(0,"");
        results.add(0, columnNames);

        return results;
    }
}
