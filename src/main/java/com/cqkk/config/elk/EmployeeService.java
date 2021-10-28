package com.cqkk.config.elk;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component("esService")
public class EmployeeService {

    private Log log = LogFactory.getLog(EmployeeService.class);
    @Resource
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    EmployeeRepository employeeRepository;

    //索引操作

    /**
     * 判断索引是否存在
     *
     * @param indexName 索引名称
     * @return boolean
     */
    public void indexExists(String indexName) {
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(indexName));
        boolean exists = indexOperations.exists();
        if (exists) {
            log.info(indexName + "索引存在");
        } else {
            log.info(indexName + "索引不存在");
        }
    }

    //创建索引

    /**
     * 创建索引
     *
     * @param indexName 索引名称
     * @return boolean
     */
    public void createIndex(String indexName) {
        IndexOperations operations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(indexName));
        boolean b = operations.create();
        if (b) {
            log.info("创建索引成功" + indexName);
        } else {
            log.info("创建索引失败" + indexName);
        }
    }

    /**
     * 创建索引
     *
     * @return boolean
     */
    public void createIndex2() {
        IndexOperations operations = elasticsearchRestTemplate.indexOps(EmployeeBean.class);
        boolean b = operations.create();
        if (b) {
            log.info("创建索引成功" + EmployeeBean.class);
        } else {
            log.info("创建索引失败" + EmployeeBean.class);
        }
    }

    /**
     * 删除索引
     */
    public void deleteIndex() {
        IndexOperations operations = elasticsearchRestTemplate.indexOps(EmployeeBean.class);
        boolean b = operations.delete();
        if (b) {
            log.info("删除索引成功" + EmployeeBean.class);
        } else {
            log.info("删除索引失败" + EmployeeBean.class);
        }
    }

    /**
     * 索引删除
     *
     * @param indexNm 索引名称
     * @return boolean
     */
    public void deleteIndex2(String indexNm) {
        IndexOperations operations = elasticsearchRestTemplate.indexOps(IndexCoordinates.of(indexNm));
        boolean b = operations.delete();
        if (b) {
            log.info("删除索引成功" + EmployeeBean.class);
        } else {
            log.info("删除索引失败" + EmployeeBean.class);
        }
    }

    /**
     * 新增数据
     *
     * @param bean 数据对象
     */
    public void save(EmployeeBean bean) {
        elasticsearchRestTemplate.save(bean);
    }

    /**
     * 批量新增数据
     *
     * @param bean 数据集合
     */
    public void saveAll(List<EmployeeBean> bean) {
        elasticsearchRestTemplate.save(bean);
    }

    /**
     * 修改数据
     *
     * @return boolean
     */
    public void update(String indexNm, String id, EmployeeBean bean) {
        Document document = Document.create();
        // 将索引indexNm id为1的值更为新bean
        Map map = JSONObject.parseObject(JSONObject.toJSONString(bean), Map.class);
        document.putAll(map);
        elasticsearchRestTemplate.update(UpdateQuery.builder(id).withDocument(document).build(), IndexCoordinates.of(indexNm));
    }

}
