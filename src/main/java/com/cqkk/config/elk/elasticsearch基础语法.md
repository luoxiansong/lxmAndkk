1.es使用restful风格的api
备注：es的api格式基本是这个样请求方式/索引名/文档名/id?参数，但是还有很多不是这样的请求，比如_cat api等等........
2.查询所有索引get/_cat/indices
3.查看节点健康get/_cat/health?v
4.?v的意思显示列出项的title
5.?pretty结果json格式化的方式输出
6.添加索引put/test1
7.添加一条document（如果已经存在那么久全部覆盖）
    PUT /test1/d1/1
    {
    "age":1,
    "name":"zs",
    "bri":"2018-08-08"
    }
备注1:如果已经存在就全覆盖修改。
备注2:如果只需要插入，不修改着在后面加上/_create(这时候会提示已经存在),post不能带_create
8.修改一条文档
    POST /test1/d1/2
    {
    "age":1,
    "name":"zs",
    "bri":"2018-08-08"
    }
备注1:post虽然叫做修改，但是在带有id的情况下和put几乎一样（id存在就是全量修改，不存在就是新增）。
备注2:可以使用POST/test1/d1不带id的方式自动生成id，put不支持不带id的写法。
备注3:post可以指定_update,并且可以带_create。
备注4:post可以部分更新专用名字partial update
    POST /test1/d1/5/_update
    {
    "doc":{
    "age":2
    } }
9.删除一条文档      
    DELETE  test1/d1/5
10. 删除一条索引
    DELETE  test1
11.查询单条文档  
    GET user/student/2    
12.搜索文档get user/_search 或者 get user/student/_search
    备注:查询可以不指定 type 的类型     
13.查询所有
    get /user/student/_search
    {
    "query":{
    "match_all": {}
     } }
14.指定字段查询
    get /user/student/_search
    {
    "query":{
    "match": {
    "name": "n5"
    } } }
15.范围查询    
    get /user/student/_search
    {
    "query":{
    "range": {
    "bri": {
    "gte": 10,
    "lte": 20
    } } } }   
16.多条件的复合查询
    get /user/student/_search
    {
    "query":{
    "bool": {
    "must": [
    { "match": {
    "FIELD": "TEXT"
    }}],
    "should": [
    {"match": {
    "FIELD": "TEXT"
    },
    "match": {
    "FIELD": "TEXT"
    } }],
        "minimum_should_match": 1
    } } }
    备注:bool里面的都是一些条件,must必须瞒足，should只要要满足minimum_should_match个条件是ture ，filter只是过滤不计入评分
17.查询非分页
    get /user/student/_search
    {
    "query":{
    "match_all": {}
    },
    "from":3,
    "size":2 
    }
    备注1:深分页问题，效率会很低，劲量避免深分页。
    备注2:深分页:如果要查询出每页100条,第100 页数据数据（9900-10000），如果是去5个节点查询，那么会在每个节点查询出第9900-10000条数据，
    然后汇总到坐标点，然后排序后取出9900-10000条，这样做非常占资源。 
18.scroll游标查询，指定scroll=时间，指定保存的分钟数，第一次发起请求放回的不是数据，而是_scroll_id，后面通过_scroll_id去请求数据，非常适合大批量查询。
    get /user/student/_search?scroll=1m
    {
    "query":{
    "match_all": {}
    },
    "size":2
    }

    GET /_search/scroll
    {
    "scroll": "1m",
    "scroll_id" : "sajgdkjasdkasdasdasdsald"
    }
    备注:游标查询是在es里面缓存了结果，然后一次一次的去取所以发起第一次请求的时候只有size，没有from，后面的请求只有scroll_id和scroll时间
19.只显示指定结果（_source）    
    GET /user/_search
    {
    "query": {
    "match_all": {}
    },
    "_source": ["bri"]
    }
20.post_filter和query的区别，语法上没区别，唯一的在于filter不评分，所以filter比query快很多，filter和query可以共存。
    GET /user/_search
    {
    "post_filter": {
    "match_all": {}
    },
    "_source": ["bri"]
    }
21.聚合函数 求平均值和总数量
    GET  user/student/_search
    {
    "query": {
    "match_all": {}
    },
    "aggs": {
    "total_count": {
    "value_count": {
    "field": "age"
    } },
    "pjz":{
    "avg": {
    "field": "age"
    } } } }
22.分组
    GET  user/student/_search
    {
    "query": {
    "match_all": {}
    },
    "aggs": {
    "fz": {
    "terms": {
    "field": "age"
    } } } }
23.批查询 api mget 
    #批量查询
    GET /user/_mget
    {
    "docs":[{
    "_type":"student",
    "_id":1
    },
    {
    "_type":"student",
    "_id":2
    },
    {
    "_type":"student",
    "_id":21111111
    }]}
24.es的删除是假删除并且在下一次merge的时候真删除 
25.es的并发处理，使用的乐观锁在后面加上version    
    POST  /user/student/1?version=3
    {
    "name":"zyk",
    "age":0
    }
    备注:只有version=当前记录的version的时候才能修改成功
26.查询索引的设置  
    GET /user/_settings
27.查看索引的mapping  
    GET user/_mapping
28.动态的mapping  es或根据第一次存入的数据，动态的决定这个字段的mapping 类型，并且决定索引行为,后面类型不符合就没法存入，
    mapping里面的类型不能修改，只能添加新的。   
    put /test2/t/1
    {
    "age":1,
    "name":"name",
    "bri":"2017-09-09",
    "isDel":true,
    "amount":0.1
    }
    
    GET /test2/_mapping
29.指定mapping只能给新的索引指定，或者新的字段指定
    PUT /test2/_mapping/t
    {
    "properties": {
    "age": {
    "type": "long"
    },
    "amount": {
    "type": "float"
    },
    "bri": {
    "type": "date"
    },
    "isDel": {
    "type": "boolean"
    },
    "name": {
    "type": "text",
    "fields": {
    "keyword": {
    "type": "keyword",
    "ignore_above": 256
    } } } } }
    备注1:mapping 里面 keyword 的可以指定 text 的子类型
    备注2:如果字段类型是json，那么这个字段的类型就是object，或者说是document这时候mapping里面是映射了一个property
30.自定义分词器    
    PUT /user5
    {
    "settings":{
    "analysis": {
    "char_filter": {
    "my_char_filter":{
    "type":"mapping",
    "mappings":["&=> and"]
    } },
    "filter": {
    "my_filter":{
    "type":"stop",
    "stopwords":["the","a"]
    } },
    "analyzer": {
    "my_analyzer":{
    "type":"custom",
    "char_filter":["my_char_filter" ],
    "filter":["my_filter"],
    "tokenizer":"standard"
    } } } } }
    解释：定义了一个char_filter名叫my_char_filter，类型是mapping把&转成and
    定义了一个filter名叫my_filter，类型是停用词，把the，a去掉
    定义了一个分析器名叫my_analyzer,类型是自定义，它使用了char_filter是my_char_filter,它使用的filter是my_filter,它使用的分词器是标准分词器。 
例子二:
    PUT /user9
    {
    "settings":{
    "analysis": {
    "char_filter": {
    "my_char_filter":{
    "type":"mapping",
    "mappings":["& => and","pingguo => pingg"]
    } },
    "filter": {
    "my_filter":{
    "type":"stop",
    "stopwords":["the","a"]
    } },
    "analyzer": {
    "my_analyzer":{
    "type":"custom",
    "char_filter":["my_char_filter" ],
    "filter":["my_filter"],
    "tokenizer":"standard"
    } } } } }
    
    GET /user9/_analyze
    {
    "analyzer":"my_analyzer",
    "text":" a d&og is in the house pingguo"
    }
31.自定义 动态mapping
    PUT my_index1
    {
    "mappings": {
    "_doc":{
    "dynamic":"strict",
    "properties":{
    "name":{
    "type":"text"
    },
    "user":{
    "type":"object",
    "dynamic":"true"
    } } } } }

    GET my_index1/_doc/1
    {
    "name":"name1",
    "user":{
    "name":"n1",
    "age":10
    },
    "age":2
    }
    mapping的第一层，也就是properties的那一层，不允许动态映射，有新的字段就报错，
    user的那一层，允许动态映射，有新的字段就根据新的第一次的值，指定类型。
    dynamic=false的时候会存进去，但是我试了一次，不管1还是"1"都可以存进去，但是也可以查看得到，但是好像搜索不到。
32.查看 一段文本是在某个分词器上是怎么分词的
    GET /user/_analyze
    {
    "analyzer":"standard",
    "text": " a dog is in the house"
    }
33.给指定 字段指定指定的分词器
    put /user3/_mapping/student
    {
    "properties":{
    "name":{
    "type":"text",
    "analyzer":"standard"
    } } }