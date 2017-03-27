package boot.quartz;

import lombok.Data;

/**
 * Created by nicaisheng on 17/3/23.
 */
@Data
public class TaskDefinition {

    private String taskName;

    private String taskClass;

    private String cron;

    /**
     * 是否停止调度该任务
     */
    private boolean remove = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDefinition that = (TaskDefinition) o;

        if (remove != that.remove) return false;
        if (taskName != null ? !taskName.equals(that.taskName) : that.taskName != null) return false;
        if (taskClass != null ? !taskClass.equals(that.taskClass) : that.taskClass != null) return false;
        return !(cron != null ? !cron.equals(that.cron) : that.cron != null);
    }

    @Override
    public int hashCode() {
        int result = taskName != null ? taskName.hashCode() : 0;
        result = 31 * result + (taskClass != null ? taskClass.hashCode() : 0);
        result = 31 * result + (cron != null ? cron.hashCode() : 0);
        result = 31 * result + (remove ? 1 : 0);
        return result;
    }

}
