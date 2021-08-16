package ask.me.again.SqlBridge;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.jdbi.v3.core.Jdbi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SqlController {

    private final Jdbi jdbi;

    @GetMapping("/test")
    public String executeSql(@RequestBody String sqlCommand) {
        return jdbi.withHandle(x -> x.createQuery(sqlCommand)
                .mapTo(String.class)
                .findFirst()
                .get());
    }
}
