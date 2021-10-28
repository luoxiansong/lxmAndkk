package com.cqkk.config.elk;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
//Repository需要继承ElasticsearchRepository接口，参数<映射对象，主键ID的数据类型>。之后Repository类就可以使用类似JPA的方法操作es数据
public interface EmployeeRepository extends ElasticsearchRepository<EmployeeBean, String> {
}
