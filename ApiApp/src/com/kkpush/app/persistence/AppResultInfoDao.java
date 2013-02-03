package com.kkpush.app.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.jdbc.core.JdbcTemplate;

/***
 * 应用统计
 * @author xb
 *
 */
public class AppResultInfoDao extends SqlSessionDaoSupport {

	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> getDataList(Map<String, Object> params) {
		List<Object> paramList = new ArrayList<Object>();
		String sql = getPageSql(params, paramList);
		return jdbcTemplate.queryForList(sql, paramList.toArray());
	}
	 

	public Integer getTotalCount(Map<String, Object> params) {
		List<Object> paramList = new ArrayList<Object>();
		String sql = "select count(1) from (" + getSql(params, paramList) + ")";
		return jdbcTemplate.queryForInt(sql, paramList.toArray());
	}

	private String getSql(Map<String, Object> params, List<Object> paramList) {
		StringBuffer sql = new StringBuffer();

		sql.append("select IDATE,FIELD as DEV_ID, kpi_field  as APP_ID , ");
		
		sql.append("sum(case when KPI_CODE = 'SENT_AFTER_PER_APP' then KPI_VALUE else 0 end) as SENT_AFTER_PER_APP,");
		sql.append("sum(case when KPI_CODE = 'SHOW_AFTER_PER_APP' then KPI_VALUE else 0  end) as SHOW_AFTER_PER_APP , ");
		sql.append("sum(case when KPI_CODE = 'MONEY_PER_APP' then KPI_VALUE else 0  end) as MONEY_PER_APP , ");
		sql.append("sum(case when KPI_CODE = 'SHOW_SENT_RATE_PER_APP' then KPI_VALUE else 0  end) as SHOW_SENT_RATE_PER_APP   ");

		sql.append("from T_GENERAL_KPI_DAY ");
		sql.append("where idate between ? and ? and field=?   ");
		logger.debug("startDate : " +params.get("startDate") );
		logger.debug("endDate : " + params.get("endDate") );
		paramList.add(params.get("startDate"));
		paramList.add(params.get("endDate"));
		paramList.add(params.get("devId")); 
		if(params.get("appId")!=null){
			sql.append(" and kpi_field = ?");
			paramList.add(params.get("appId")+"");
		}
		sql.append(" group by IDATE,FIELD,kpi_field order by idate");
		return sql.toString();
	}

	private String getPageSql(Map<String, Object> params, List<Object> paramList) {
		int startIndex = 0;
		int endIndex = 10;

		if (params.containsKey("startIndex")) {
			startIndex = (Integer) (params.get("startIndex"));
		}
		if (params.containsKey("endIndex")) {
			endIndex = (Integer) (params.get("endIndex"));
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("  select * from ( ");
//		buffer.append("select * from (select * from ( select row_.*, rownum rownum_ from (");
		buffer.append(getSql(params, paramList));
		buffer.append(" ) tb where tb.SENT_AFTER_PER_APP >0");
		
//		buffer.append(") row_ ) where rownum <= ").append(endIndex).append(") where rownum_ > ").append(startIndex);
		return buffer.toString();
	}

}