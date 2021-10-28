https://blog.24dm.cn/archives/61/
[实体类及注解] 示例代码见 Item.java
Spring Data通过注解来声明字段的映射属性，有下面的三个注解：
[@Document] 作用在类，标记实体类为文档对象，一般有四个属性
    indexName：对应索引库名称
    shards：分片数量，默认5
    replicas：副本数量，默认1
[@Id] 作用在成员变量，标记一个字段作为id主键
[@Field] 作用在成员变量，标记为文档的字段，并指定字段映射属性：
    type：字段类型，取值是枚举：FieldType
    index：是否索引，布尔类型，默认是true
    store：是否存储，布尔类型，默认是false
    analyzer：分词器名称：ik_max_word
