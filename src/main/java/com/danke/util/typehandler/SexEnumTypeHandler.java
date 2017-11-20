package com.danke.util.typehandler;

import com.danke.enums.SexEnum;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 性别枚举handler
 *
 * @date 2017年11月20日15:26:03
 */
public class SexEnumTypeHandler extends BaseTypeHandler<SexEnum> {

    private Class<SexEnum> sexEnum;

    public SexEnumTypeHandler(Class<SexEnum> sexEnum) {
        if (sexEnum == null) {
            throw new IllegalArgumentException("Type argument can not be null");
        }
        this.sexEnum = sexEnum;
    }

    public void setNonNullParameter(PreparedStatement preparedStatement, int i, SexEnum sexEnum, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i,sexEnum.getSex());
    }

    public SexEnum getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        int i = resultSet.getInt(columnName);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return getValuedEnum(i);
        }
    }

    public SexEnum getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        int i = resultSet.getInt(columnIndex);
        if (resultSet.wasNull()) {
            return null;
        } else {
            return getValuedEnum(i);
        }
    }

    public SexEnum getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        int i = callableStatement.getInt(columnIndex);
        if (callableStatement.wasNull()) {
            return null;
        } else {
            return getValuedEnum(i);
        }
    }

    private SexEnum getValuedEnum(int value) {
        SexEnum[] objs = sexEnum.getEnumConstants();
        for(SexEnum em:objs){
            if(em.getSex()==value){
                return  em;
            }
        }
        throw new IllegalArgumentException(
                "Cannot convert " + value + " to " + sexEnum.getSimpleName() + " by value.");
    }
}
