package com.example.demo.dao.impl;

import com.example.demo.bean.Job;
import com.example.demo.bean.JobState;
import com.example.demo.dao.JobDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("jobDao")
public class JobDaoImpl extends AbstractDao implements JobDao {
    private static final RowMapper<Job> jobRowMapper = (rs, i) -> {
        Job job = new Job();
        job.setId(rs.getLong("ID"));
        job.setPriority(rs.getLong("PRIORITY"));
        job.setType(rs.getString("JOB_TYPE"));
        job.setMessage(rs.getString("MESSAGE"));
        job.setCreateTime(toLocalDateTime(rs.getTimestamp("CREATE_TIME")));
        job.setUpdateTime(toLocalDateTime(rs.getTimestamp("UPDATE_TIME")));
        job.setState(toEnum(rs.getString("JOB_STATE"), JobState.class));
        return job;
    };

    @Override
    public Job selectJob(Long id) {
        String sql = "" +
                " select id, priority, job_type, message, create_time, update_time, job_state " +
                " from job " +
                " where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("id", id);

        List<Job> list = jdbcTemplate.query(sql, source, jobRowMapper);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Long insertJob(Job job) {
        String sql = "" +
                " insert into job (id, priority, job_type, message, create_time, update_time, job_state) " +
                " values (" +
                "   job_seq.nextval, " +
                "   :priority, " +
                "   :job_type, " +
                "   :message, " +
                "   current_timestamp, " +
                "   current_timestamp, " +
                "   :job_state)";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("priority", job.getPriority())
                .addValue("job_type", job.getType())
                .addValue("message", job.getMessage())
                .addValue("job_state", toString(job.getState()));

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(sql, source, keyHolder, new String[]{"id"});

        return keyHolder.getKey() == null ? null : keyHolder.getKey().longValue();
    }

    @Override
    public void updateJob(Job job) {
        String sql = "" +
                " update job set" +
                "   message = :message," +
                "   update_time = current_timestamp," +
                "   job_state = :job_state" +
                " where id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("message", job.getMessage())
                .addValue("job_state", toString(job.getState()))
                .addValue("id", job.getId());

        jdbcTemplate.update(sql, source);
    }

    @Override
    public List<Job> selectJobs(JobState state) {
        String sql = "" +
                " select id, priority, job_type, message, create_time, update_time, job_state " +
                " from job " +
                " where job_state = :job_state";
        MapSqlParameterSource source = new MapSqlParameterSource()
                .addValue("job_state", toString(state));

        return jdbcTemplate.query(sql, source, jobRowMapper);
    }
}
