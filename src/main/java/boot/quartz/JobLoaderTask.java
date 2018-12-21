package boot.quartz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态查询查询数据库任务，监控停止的任务和修改实时生效
 * 由于quartz每次都会读取detail 和 trigger表  所以直接在外部修改表内容也能实时生效
 * Created by nicaisheng on 17/3/23.
 */
@Slf4j
//@Component
public class JobLoaderTask {

    @Autowired
    private Environment env;

    private final Map<String, TaskDefinition> scheduleTaskMap = new HashMap<String, TaskDefinition>();

    @Autowired
    private QuartzSchedulerHelper schedulerHelper;

    @Resource
    private JdbcTemplate jdbcTemplate;

//    @Scheduled(cron = "*/3 * * * * *")
    public void run() {
        log.info("try schedule ...");

        List<TaskDefinition> taskDefinitionList = loadTaskDefinitions();
        if (taskDefinitionList.isEmpty()) {
            log.info("no task is required to be scheduled !!!");
            return;
        }

        for (TaskDefinition def : taskDefinitionList) {
            if (scheduleRequired(def))
                schedulerHelper.schedule(def);
        }
    }

    private List<TaskDefinition> loadTaskDefinitions() {
        List<TaskDefinition> definitions = Collections.EMPTY_LIST;
        String sql = "SELECT t.CRON_EXPRESSION,d.JOB_NAME,d.JOB_CLASS_NAME from QRTZ_CRON_TRIGGERS t, QRTZ_JOB_DETAILS d where t.TRIGGER_NAME=d.JOB_NAME";
        definitions = jdbcTemplate.query(sql, new RowMapper<TaskDefinition>() {
            @Override
            public TaskDefinition mapRow(ResultSet resultSet, int i) throws SQLException {
                TaskDefinition task = new TaskDefinition();
                task.setCron(resultSet.getString("CRON_EXPRESSION"));
                task.setTaskClass(resultSet.getString("JOB_CLASS_NAME"));
                task.setTaskName(resultSet.getString("JOB_NAME"));
                return task;
            }
        });

        return definitions;
    }

    private boolean scheduleRequired(TaskDefinition def) {
        TaskDefinition oldDef = scheduleTaskMap.get(def.getTaskName());
        //add or replace old task def
        scheduleTaskMap.put(def.getTaskName(), def);

        if (oldDef != null
                && oldDef.equals(def))
            return false;

        return true;
    }

}
