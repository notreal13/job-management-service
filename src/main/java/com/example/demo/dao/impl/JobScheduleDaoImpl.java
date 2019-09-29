package com.example.demo.dao.impl;

import com.example.demo.bean.JobSchedule;
import com.example.demo.dao.JobScheduleDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jobScheduleDao")
public class JobScheduleDaoImpl extends AbstractDao implements JobScheduleDao {
    private static final RowMapper<JobSchedule> jobScheduleRowMapper = (rs, i) -> {
        JobSchedule jobSchedule = new JobSchedule();
        jobSchedule.setId(rs.getLong("ID"));
        jobSchedule.setPriority(rs.getLong("PRIORITY"));
        jobSchedule.setJobType(rs.getString("JOB_TYPE"));
        jobSchedule.setCronExpression(rs.getString("CRON_EXPRESSION"));
        return jobSchedule;
    };

    @Override
    public JobSchedule selectJobSchedule(Long id) {
        String sql = "" +
                " select id, priority, job_type, cron_expression " +
                " from job_schedule " +
                " where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", id);

        List<JobSchedule> list = jdbcTemplate.query(sql, source, jobScheduleRowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<JobSchedule> selectJobSchedules() {
        String sql = "" +
                " select id, priority, job_type, cron_expression " +
                " from job_schedule";

        return jdbcTemplate.query(sql, new MapSqlParameterSource(), jobScheduleRowMapper);
    }

    @Override
    public Long insertJobSchedule(JobSchedule jobSchedule) {
        String sql = "" +
                " insert into job_schedule (id, priority, job_type, cron_expression) " +
                " values (" +
                "   job_schedule_seq.nextval, " +
                "   :priority, " +
                "   :job_type, " +
                "   :cron_expression)";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("priority", jobSchedule.getPriority())
                .addValue("job_type", jobSchedule.getJobType())
                .addValue("cron_expression", jobSchedule.getCronExpression());

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, source, keyHolder, new String[]{"id"});

        return keyHolder.getKey() == null ? null : keyHolder.getKey().longValue();

    }

    @Override
    public void deleteJobSchedule(Long id) {
        String sql = "" +
                " delete " +
                " from job_schedule " +
                " where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", id);


        jdbcTemplate.update(sql, source);
    }
}
