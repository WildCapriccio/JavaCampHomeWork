package com.lagou.edu.dao.impl;

import com.lagou.edu.pojo.Account;
import com.lagou.edu.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository("accountDao")
public class JdbcTemplateDaoImpl implements AccountDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Account queryAccountByCardNo(String cardNo) throws Exception {

        String sql = "select * from account where cardNo=?";

        /*
        * queryForObject(sql语句，怎么封装返回结果，sql语句的参数对应？)
        * */
        return jdbcTemplate.queryForObject(sql, new RowMapper<Account>() {
            @Override
            public Account mapRow(ResultSet resultSet, int i) throws SQLException {
                Account account = new Account();
                account.setName(resultSet.getString("name"));
                account.setCardNo(resultSet.getString("cardNo"));
                account.setMoney(resultSet.getInt("money"));
                return account;
            }
        }, cardNo);
    }

    @Override
    public int updateAccountByCardNo(Account account) throws Exception {

        String sql = "update account set money=? where cardNo=?";

        // update(sql语句，sql语句参数对应？)
        return jdbcTemplate.update(sql, account.getMoney(), account.getCardNo());
    }
}
