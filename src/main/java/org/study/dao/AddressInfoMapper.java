package org.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.study.data.AddressInfoDO;

import java.util.List;

/**
 * @author fanqie
 * @date 2020/2/7
 */
@Mapper
public interface AddressInfoMapper {

    /**
     * 根据用户id查询地址信息
     * @param userId 用户id
     * @return 地址信息集合
     */
    List<AddressInfoDO> selectByUserId(@Param("userId") final Integer userId);

    /**
     * 插入新的地址信息,当主键重复时新信息替换旧信息
     * @param info 待插入信息
     * @return 影响的SQL行数
     */
    int insertOrUpdate(@Param("info") final AddressInfoDO info);

    /**
     * 根据地址id和用户id删除地址信息
     * @param infoId 地址id
     * @param userId 用户id
     * @return 影响的SQL行数
     */
    int deleteById(@Param("infoId") final Integer infoId, @Param("userId") final Integer userId);

    /**
     * 将地址信息设为默认
     * @param infoId 目标地址信息的id
     * @param userId 用户id
     * @return 影响的SQL行数
     */
    int updateSelected(@Param("infoId") final Integer infoId,
                       @Param("userId") final Integer userId);

    /**
     * 将用户的默认地址信息设置取消
     * @param userId 用户id
     * @return 影响的SQL行数
     */
    int cancelSelected(@Param("userId") final Integer userId);
}
