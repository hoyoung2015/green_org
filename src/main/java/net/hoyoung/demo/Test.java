package net.hoyoung.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/21.
 */
public class Test {
    public static void main(String[] args) {
        String json = "{\"center\":\"\\u5317\\u4eac \\u5317\\u4eac\\u5e02 \\u987a\\u4e49\\u533a\",\"zoom\":10,\"list\":[{\"id\":\"41\",\"title\":\"\\u78d0\\u77f3\\u73af\\u5883\\u4e0e\\u80fd\\u6e90\\u7814\\u7a76\\u6240\",\"x\":\"116.559759\",\"y\":\"40.118123\"},{\"id\":\"1785\",\"title\":\"\\u6e05\\u534e\\u5927\\u5b66\\u7f8e\\u672f\\u5b66\\u9662\\u534f\\u540c\\u521b\\u65b0\\u751f\\u6001\\u8bbe\\u8ba1\\u4e2d\\u5fc3\",\"x\":\"116.40953\",\"y\":\"39.908578\"},{\"id\":\"1788\",\"title\":\"\\u5317\\u4eac\\u81ea\\u7136\\u5411\\u5bfc\\u79d1\\u666e\\u4f20\\u64ad\\u4e2d\\u5fc3\",\"x\":\"116.413554\",\"y\":\"39.907914\"},{\"id\":\"1352\",\"title\":\"\\u81ea\\u7136\\u4e4b\\u53cb\\u91ce\\u9e1f\\u4f1a\",\"x\":\"116.665851\",\"y\":\"40.137523\"},{\"id\":\"1500\",\"title\":\"\\u4e2d\\u56fd\\u519c\\u4e1a\\u5927\\u5b66\\u519c\\u533a\\u73af\\u4fdd\\u534f\\u4f1a\",\"x\":\"116.36421\",\"y\":\"40.010529\"}]}";
        ObjectMapper om = new ObjectMapper();
        try {
            Map<String, Object> map = om.readValue(json, new TypeReference<Map<String, Object>>() {});
            List<Map<String,String>> list = (List<Map<String, String>>) map.get("list");
            for (Map<String,String> rs : list){
                System.out.println(rs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
