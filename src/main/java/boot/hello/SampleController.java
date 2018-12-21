package boot.hello;

import boot.schedule.ScheduledTasks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicaisheng on 17/3/20.
 */
@Controller
public class SampleController {

    @Inject
    Environment env;

//    @Resource
//    private JdbcTemplate jdbcTemplate;

    @Value("${jdbc.driverClass}")
    public String driverclass;

    @Value("${java.vendor}")
    String vendor;

    @Value("${other}")
    String other;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        System.out.println(env);
        return driverclass;
    }

    @RequestMapping("/index")
    @ResponseBody
    String index() {
        System.out.println(env);
        return driverclass;
    }

//    @RequestMapping("/tasks")
//    @ResponseBody
//    Object tasks() {
//        String sql = "SELECT * from app_user";
//        return jdbcTemplate.query(sql, new RowMapper<Object>() {
//
//            @Override
//            public Object mapRow(ResultSet resultSet, int i) throws SQLException {
//                Map<String,Object> map = new HashMap<String,Object>();
//                map.put("app_id",resultSet.getString("app_id"));
//                map.put("app_secret",resultSet.getString("app_secret"));
//                return map;
//            }
//        });
//    }

}
