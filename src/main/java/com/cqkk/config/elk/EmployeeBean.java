package com.cqkk.config.elk;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 员工对象
 * 注解：@Document用来声明Java对象与ElasticSearch索引的关系
 * indexName 索引名称
 * shards    主分区数量，默认5
 * replicas  副本分区数量，默认1
 * createIndex 索引不存在时，是否自动创建索引，默认true
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Document(indexName = "index_em", shards = 1, replicas = 0)
public class EmployeeBean {

    @Id
    private String id;

    /**
     * 员工编码
     */
    @Field(type = FieldType.Keyword)
    private String studentCode;

    /**
     * 员工姓名
     */
    @Field(type = FieldType.Keyword)
    private String name;

    /**
     * 员工简历
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String desc;

    /**
     * 员工年龄
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private Integer age;

    /**
     * 手机号码
     */
    @Field(type = FieldType.Keyword)
    private String mobile;

}