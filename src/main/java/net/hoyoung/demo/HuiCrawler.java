package net.hoyoung.demo;

import cn.edu.hfut.dmic.webcollector.crawler.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.model.Links;
import cn.edu.hfut.dmic.webcollector.model.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/10/21.
 */
public class HuiCrawler extends BreadthCrawler {
    JdbcTemplate jdbcTemplate = null;
    private Map<String,String> getLoc(String url){
        Map<String,String> map = new HashMap<>();
        String[] tt = {"province","city","area"};
        String[] sstr = url.substring(url.indexOf("?")+1).split("&");
        int i=0;
        for (; i <sstr.length ; i++) {
            String id = sstr[i].split("=")[1];
            map.put(tt[i],id);
        }
        for (;i<tt.length;i++){
            map.put(tt[i],"0");
        }
        System.out.println(map);
        return map;
    }
    @Override
    public void visit(Page page, Links links) {
        String json = page.getHtml();
        ObjectMapper om = new ObjectMapper();
        Map<String,String> loc = getLoc(page.getUrl());
        try {
            Map<String, Object> map = om.readValue(json, new TypeReference<Map<String, Object>>() {});
            List<Map<String,String>> list = (List<Map<String, String>>) map.get("list");
            if(list==null){
                return;
            }
            for (Map<String,String> rs : list){
                if(rs.get("id")==null) continue;//不是最细分的
                if (jdbcTemplate != null) {
                    int updates=jdbcTemplate.update("insert into org (id,title,x,y,province,city,area) value(?,?,?,?,?,?,?)",
                            rs.get("id"),
                            rs.get("title"),
                            rs.get("x"),
                            rs.get("y"),
                            loc.get("province"),
                            loc.get("city"),
                            loc.get("area"));
                    if(updates==1){
                        System.out.println(rs.get("title")+"mysql插入成功");
                    }
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public HuiCrawler(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        List<String> urls = null;
        try {
            urls = FileUtils.readLines(new File("data/param.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        for (String url : urls){
            this.addSeed("http://www.hyi.org.cn/go/index/filter?"+url);
        }
        jdbcTemplate = JDBCHelper.createMysqlTemplate("mysql1",
                "jdbc:mysql://localhost/green_org?useUnicode=true&characterEncoding=utf8",
                "root", "", 5, 30);
        jdbcTemplate.update("delete from org where 1=1");
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        HuiCrawler crawler = new HuiCrawler("huiCrawler",false);
        crawler.setThreads(10);
//        crawler.setTopN(100);
        //crawler.setResumable(true);
        /*start crawl with depth of 4*/
        crawler.start(1);
        System.out.println("耗时："+(System.currentTimeMillis()-start)/1000+"秒");
    }
}
